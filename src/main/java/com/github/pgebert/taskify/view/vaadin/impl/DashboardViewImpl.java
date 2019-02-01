package com.github.pgebert.taskify.view.vaadin.impl;

import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.view.api.DashboardView;
import com.github.pgebert.taskify.view.vaadin.component.MenuViewComponent;
import com.github.pgebert.taskify.view.vaadin.helper.HighChart;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import lombok.extern.java.Log;


//@JavaScript({"jquery-min.js", "highcharts.js", "highcharts-connector.js"})
@Log
@Singleton
public class DashboardViewImpl extends HorizontalLayout implements DashboardView, View {

	private static final long serialVersionUID = 5720657319198329845L;
	
	private HighChart barChart;
	private HighChart donutChart;
	private HighChart histogramChart;
	private EventBus eventBus;


	@Inject
	public DashboardViewImpl(MenuViewComponent menuView, final EventBus eventBus) {
		
		this.eventBus = eventBus;
		
		setSizeFull();		
		
		VerticalLayout rightSide = new VerticalLayout();
		rightSide.setSizeFull();
		rightSide.setStyleName("scrollable");
		
		Label title = new Label("Dashboard");
		title.addStyleName("h1");
		title.setSizeFull();
		title.setWidth(95, Unit.PERCENTAGE);			
		
		rightSide.addComponent(title);
		rightSide.setExpandRatio(title, 1f);
		rightSide.setComponentAlignment(title, Alignment.TOP_LEFT);	
		
		addComponent(menuView);
		setExpandRatio(menuView, 1f);
		
		addComponent(rightSide);
		setExpandRatio(rightSide, 6f);
		
//		rightSide.addComponent(buildChart());
		
		Layout customLayout = new CssLayout();
		customLayout.setSizeFull();
		
		this.barChart = new HighChart();
		this.barChart.setHeight("48%");
		this.barChart.setWidth(48.0f, Unit.PERCENTAGE);
		this.barChart.addStyleName("layout-with-border");
		customLayout.addComponent(this.barChart);
		
		this.donutChart = new HighChart();
		this.donutChart.setHeight("48%");
		this.donutChart.setWidth(48.0f, Unit.PERCENTAGE);
		this.donutChart.addStyleName("layout-with-border");
		customLayout.addComponent(this.donutChart);
				
		this.histogramChart = new HighChart();
		this.histogramChart.setHeight("48%");
		this.histogramChart.setWidth(96.0f, Unit.PERCENTAGE);
		this.histogramChart.addStyleName("layout-with-border");
		customLayout.addComponent(this.histogramChart);
		
		rightSide.addComponent(customLayout);
		rightSide.setExpandRatio(customLayout, 6f);
		
	}
	
	private HighChart buildChart() {
		HighChart chart = new HighChart();
		chart.setHcjs("var options = { chart: { type: 'column' }, title: { text: 'Monthly Average Rainfall' }, subtitle: { text: 'Source: WorldClimate.com' }, xAxis: { categories: [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ], crosshair: true }, yAxis: { min: 0, title: { text: 'Rainfall (mm)' } }, tooltip: { headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>', pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' + '<td style=\"padding:0\"><b>{point.y:.1f} mm</b></td></tr>', footerFormat: '</table>', shared: true, useHTML: true }, plotOptions: { column: { pointPadding: 0.2, borderWidth: 0 } }, series: [{ name: 'Tokyo', data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4] }, { name: 'New York', data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3] }, { name: 'London', data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2] }, { name: 'Berlin', data: [42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1] }]};");
		return chart;
	}
	
	@Override
	public void updateHistogramChart(String options) {
		this.histogramChart.setHcjs(options);
	}
	
	@Override
	public void updateDonutChart(String options) {
		this.donutChart.setHcjs(options);
	}
	
	
	@Override
	public void updateBarChart(String options) {
		this.barChart.setHcjs(options);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		this.eventBus.post(new EnterViewEvent());
	}

}
