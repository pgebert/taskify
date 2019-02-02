package com.github.pgebert.taskify.presenter;

import static com.github.pgebert.taskify.presenter.MainPresenter.onView;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.datasource.TaskDataFacade;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.view.api.CalendarView;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.UI;


/**
 * Presenter class for to handle route views
 * @author patri
 *
 */
public class CalendarPresenter {
	
	private TaskDataFacade taskData;	
	private CalendarView calendarView;	
	private EventBus eventBus;
	
	/*
	 * Constructor for calendar view presenter to control the calendar data
	 */
	@Inject
	public CalendarPresenter(EventBus eventBus, TaskDataFacade taskData, CalendarView calendarView) {		
		
		this.taskData = taskData;	
		this.calendarView = calendarView;
		
		this.eventBus = eventBus;
		eventBus.register(this);					
	}	
	
	/**
	 * Listener to catch events from calendar filter change
	 * 
	 * @param event
	 */
	@Subscribe
	public void enter(EnterViewEvent event) {
		if (onView(this.calendarView)) {
			User user = ((BaseUI) UI.getCurrent()).getAccessControl().getUser();
			this.calendarView.showItems(taskData.read(user));
		}
	}
}
