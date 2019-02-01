package com.github.pgebert.taskify.view.vaadin.impl;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.view.api.CalendarView;
import com.github.pgebert.taskify.view.vaadin.component.MenuViewComponent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.calendar.DateConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.themes.ValoTheme;

import lombok.extern.java.Log;

//@JavaScript({"jquery-min.js", "highcharts.js", "highcharts-connector.js"})
@Log
@Singleton
public class CalendarViewImpl extends HorizontalLayout implements CalendarView, View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6344297481728183358L;
	private Calendar calendar;
	final BeanItemContainer<BasicEvent> container;
	private EventBus eventBus;

	@Inject
	public CalendarViewImpl(MenuViewComponent menuView, final EventBus eventBus) {
		
		this.eventBus = eventBus;

		// Use a container of built-in BasicEvents
		this.container = new BeanItemContainer<BasicEvent>(BasicEvent.class);

		setSizeFull();
		VerticalLayout rightSide = new VerticalLayout();

		rightSide.addComponent(buildHeader());
		rightSide.setSizeFull();
		rightSide.setStyleName("scrollable");

		addComponent(menuView);
		setExpandRatio(menuView, 1f);

		Component calendarCmp = buildCalendarView();
		rightSide.addComponent(calendarCmp);
		rightSide.setExpandRatio(calendarCmp, 9f);

//        tray = buildTray();
//        rightSide.addComponent(tray);

//        injectMovieCoverStyles();

		addComponent(rightSide);
		setExpandRatio(rightSide, 6f);
	}
	
	private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setWidth(95,Unit.PERCENTAGE);
        Responsive.makeResponsive(header);

        Label title = new Label("Kalender");
        title.setWidth(95,Unit.PERCENTAGE);
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        header.setComponentAlignment(title, Alignment.TOP_LEFT);                             

        return header;
    }	

	private Component buildCalendarView() {
		VerticalLayout calendarLayout = new VerticalLayout();
		calendarLayout.setCaption("");
		calendarLayout.setSpacing(false);
		this.calendar = new Calendar();
		calendar.setImmediate(true);
		calendar.setWidth(100.0f, Unit.PERCENTAGE);
		calendar.setHeight(1000.0f, Unit.PIXELS);

//        calendar.setHandler(new EventClickHandler() {
//            @Override
//            public void eventClick(final EventClick event) {
//                setTrayVisible(false);
//                MovieEvent movieEvent = (MovieEvent) event.getCalendarEvent();
//                MovieDetailsWindow.open(movieEvent.getMovie(),
//                        movieEvent.getStart(), movieEvent.getEnd());
//            }
//        });
		calendarLayout.addComponent(this.calendar);
		
		calendar.setFirstVisibleHourOfDay(9);
		calendar.setLastVisibleHourOfDay(16);
		
		calendar.setHandler(new BasicDateClickHandler() {

            /**
			 * 
			 */
			private static final long serialVersionUID = 8352171502353029095L;

			public void dateClick(DateClickEvent event) {
              Calendar cal = event.getComponent();
              
              // Check if the current range is already one day long
              long currentCalDateRange = cal.getEndDate().getTime() -
                                         cal.getStartDate().getTime();

              // From one-day view, zoom out to week view
              if (currentCalDateRange <= DateConstants.DAYINMILLIS) {
                  // Change the date range to the current week
                  GregorianCalendar weekstart = new GregorianCalendar();
                  GregorianCalendar weekend   = new GregorianCalendar();
                  weekstart.setTime(event.getDate());
                  weekend.setTime(event.getDate());
                  weekstart.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
                  weekstart.set(java.util.Calendar.HOUR_OF_DAY, 0);
                  weekstart.set(java.util.Calendar.DAY_OF_WEEK,
                               java.util.Calendar.SUNDAY);
                  weekend.set(java.util.Calendar.HOUR_OF_DAY, 23);
                  weekend.set(java.util.Calendar.DAY_OF_WEEK,
                               java.util.Calendar.SATURDAY);
                  cal.setStartDate(weekstart.getTime());
                  cal.setEndDate(weekend.getTime());
              } else {
                // Default behavior, change date range to one day
                super.dateClick(event);
              }
            }
          });

//	        calendar.setHandler(new BasicEventMoveHandler() {
//	            @Override
//	            public void eventMove(final MoveEvent event) {
//	                CalendarEvent calendarEvent = event.getCalendarEvent();
//	                if (calendarEvent instanceof MovieEvent) {
//	                    MovieEvent editableEvent = (MovieEvent) calendarEvent;
//
//	                    Date newFromTime = event.getNewStart();
//
//	                    // Update event dates
//	                    long length = editableEvent.getEnd().getTime()
//	                            - editableEvent.getStart().getTime();
//	                    setDates(editableEvent, newFromTime,
//	                            new Date(newFromTime.getTime() + length));
//	                    setTrayVisible(true);
//	                }
//	            }
//
//	            protected void setDates(final MovieEvent event, final Date start,
//	                    final Date end) {
//	                event.start = start;
//	                event.end = end;
//	            }
//	        });
//	        calendar.setHandler(new BasicEventResizeHandler() {
//	            @Override
//	            public void eventResize(final EventResize event) {
//	                Notification.show(
//	                        "You're not allowed to change the movie duration");
//	            }
//	        });

		java.util.Calendar initialView = java.util.Calendar.getInstance();
		initialView.add(java.util.Calendar.DAY_OF_WEEK, -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
		calendar.setStartDate(initialView.getTime());

		initialView.add(java.util.Calendar.DAY_OF_WEEK, 6);
		calendar.setEndDate(initialView.getTime());

		return calendarLayout;
	}

	@Override
	public void showItems(List<Task> items) {
		
		container.removeAllItems();

		for (Task item : items) {
			Date startTime = Date.from(item.getDate().toInstant().truncatedTo(ChronoUnit.DAYS).plus(9, ChronoUnit.HOURS));
			Date endTime = Date.from(startTime.toInstant().plus(item.getTime(), ChronoUnit.HOURS));

			BasicEvent event = new BasicEvent(item.getOwner().getName(), item.toString(), startTime, endTime);						
			container.addBean(event);
		}

		container.sort(new Object[] { "start" }, new boolean[] { true });
//
		calendar.setContainerDataSource(container, "caption", "description", "start", "end", "styleName");
		calendar.markAsDirty();

	}

	@Override
	public void enter(ViewChangeEvent event) {
		this.eventBus.post(new EnterViewEvent());
	}

}
