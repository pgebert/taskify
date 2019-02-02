package com.github.pgebert.taskify.presenter;

import static com.github.pgebert.taskify.presenter.MainPresenter.onView;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.datasource.TaskDataFacade;
import com.github.pgebert.taskify.datasource.TaskState;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.datasource.UserDataFacade;
import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.view.api.DashboardView;
import com.github.pgebert.taskify.view.vaadin.helper.AccessControl;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.vaadin.ui.UI;


/**
 * Presenter class for to handle the dash board view
 * @author patri
 *
 */
public class DashboardPresenter {
	
	private DashboardView dashboardView;
	private TaskDataFacade taskData;
	private UserDataFacade userData;
	private EventBus eventBus;
	
	
	/*
	 * Constructor for dash board presenter to control the dash board view.
	 */
	@Inject
	public DashboardPresenter(EventBus eventBus, TaskDataFacade taskData, UserDataFacade userData, DashboardView dashboardView) {
		
		this.eventBus = eventBus;
		this.dashboardView = dashboardView;
		this.taskData = taskData;
		this.userData = userData;
		
		this.eventBus.register(this);
			
	}
	
	
	/**
	 * Listener to catch events after view change and refresh data.
	 * 
	 * @param event
	 */
	@Subscribe
	public void enter(EnterViewEvent event) {
		if (onView(this.dashboardView)) {
			String optionsBarChart = "var options = { chart: { type: 'bar' }, title: { text: 'Task priorities' }, xAxis: { text: 'Open Tasks' , categories: " + Arrays.toString(getPriorityLabels()) + "}, yAxis: { title: {  }, allowDecimals:false }, tooltip: { headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>', pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' + '<td style=\"padding:0\"><b>{point.y}</b></td></tr>', footerFormat: '</table>', shared: true, useHTML: true }, plotOptions: { column: { pointPadding: 0.2, borderWidth: 0 } }, series: [{ name: 'Open tasks', data: " + Arrays.toString(getBarChartData()) + "}]};";
			this.dashboardView.updateBarChart(optionsBarChart);
			
			String optionsSplineChart = "var options = { chart: { type: 'spline' }, title: { text: 'Personal Monthly Average' }, xAxis: { categories: " + Arrays.toString(getMonthLabels()) + ", crosshair: true }, yAxis: { min: 0, title: { text: 'Realized Tasks' } }, tooltip: { headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>', pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' + '<td style=\"padding:0\"><b>{point.y}</b></td></tr>', footerFormat: '</table>', shared: true, useHTML: true }, plotOptions: { column: { pointPadding: 0.2, borderWidth: 0 } }, series: [{ name: 'Tasks', data: " + Arrays.toString(getColumnChartData()) + "}]};";
			this.dashboardView.updateSplineChart(optionsSplineChart);
			
			String optionsDonutChart = "var options = { chart: { type: 'pie' }, title: { text: 'Task progress' }, yAxis: { title: { text: 'Number of setted routes' } }, plotOptions: { pie: { shadow: false } }, tooltip: { formatter: function() { return '<b>'+ this.point.name +'</b>: '+ this.y +' routes'; } }, series: [{ name: 'Browsers', data: [['ToDo', " + getTasksOpen() + "],['Done', " + getTasksDone() + "]], size: '60%', innerSize: '20%', showInLegend:true, dataLabels: { enabled: false } }] }";
			this.dashboardView.updateDonutChart(optionsDonutChart);
		}	
	}
	
	/**
	 * Gets the number of tasks done per month by this owner for the last 12 months.
	 * @return array sorted backwards from the current month with the number of done tasks by this user
	 */
	private int[] getColumnChartData() {		
		
		int[] results = new int[12];
		
		AccessControl control = ((BaseUI) UI.getCurrent()).getAccessControl();
		User user = control.getUser();
		
		for (int i = 0; i < 12; i++) {			
			Date beginDate = Date.from(ZonedDateTime.now().minusMonths(i).with(TemporalAdjusters.firstDayOfMonth()).toInstant());
			Date endDate = Date.from(ZonedDateTime.now().minusMonths(i).with(TemporalAdjusters.lastDayOfMonth()).toInstant());
			results[i] = taskData.read(user, beginDate, endDate).stream().filter(t -> t.getState().equals(TaskState.DONE)).collect(Collectors.toList()).size();			
		}		
		ArrayUtils.reverse(results);		
		return results;		
	}
		
	/**
	 * Generates the corresponding month labels for a dash board chart. The months are sorted backwards starting with the current month and 
	 * the labels contain the last 12 months.
	 * @return array of month labels of the last year
	 */
	private String[] getMonthLabels() {
		List<String> months = Arrays.asList(new  String[] { "'Jan'", "'Feb'", "'Mar'", "'Apr'", "'May'", "'Jun'", "'Jul'", "'Aug'", "'Sep'", "'Oct'", "'Nov'", "'Dec'" });		
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		Collections.rotate(months, 12 - month);		
		String[] rotated = (String[]) months.toArray();
		
		return rotated;
	}
	
	/**
	 * Gets the number of tasks open per priority
	 * @return number of tasks open per priority
	 */
	private int[] getBarChartData() {		
		
		int[] results = new int[5];
		
		User user = ((BaseUI) UI.getCurrent()).getAccessControl().getUser();
		List<Task> tasks = taskData.read(user).stream().filter(t -> t.getState().equals(TaskState.OPEN)).collect(Collectors.toList());
		
		for (Task t : tasks) {			
			results[t.getPriority()-1]++;			
		}		
		return results;		
	}
	
	/**
	 * Generates the corresponding priority labels
	 * @return array of priority labels
	 */
	private String[] getPriorityLabels() {
		return new  String[] { "'Priority 1'", "'Priority 2'", "'Priority 3'", "'Priority 4'", "'Priority 5'"};	
	}
	
	/**
	 * Generates the number of open tasks for current user
	 * @return number of open tasks
	 */
	private int getTasksOpen() {			
		User user = ((BaseUI) UI.getCurrent()).getAccessControl().getUser();	
		List<Task> tasks = taskData.read(user);		
		List<Task> open = tasks.stream().filter(t -> t.getState().equals(TaskState.OPEN)).collect(Collectors.toList());		
		return open.size();
	}
	
	/**
	 * Generates the number of done tasks for current user
	 * @return number of done tasks
	 */
	private int getTasksDone() {		
		User user = ((BaseUI) UI.getCurrent()).getAccessControl().getUser();
		List<Task> tasks = taskData.read(user);		
		List<Task> done = tasks.stream().filter(t -> t.getState().equals(TaskState.DONE)).collect(Collectors.toList());		
		return done.size();
	}

}
