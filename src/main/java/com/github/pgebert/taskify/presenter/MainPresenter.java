package com.github.pgebert.taskify.presenter;

import java.util.Calendar;
import java.util.Date;

import com.github.pgebert.taskify.TimeIgnoringComparator;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.vaadin.ui.UI;

public class MainPresenter {
	
	private EventBus eventBus;

	@Inject
	public MainPresenter(EventBus eventBus) {	
		
		this.eventBus = eventBus;
		eventBus.register(this);
	}
	
	/**
	 * Method to check whether the currently shown view equals the given view
	 * @param view to check
	 * @return whether the given view is shown or not
	 */
	public static <V> boolean onView(V view) {
		return UI.getCurrent().getNavigator().getCurrentView().getClass().equals(view.getClass());
	}	

	public static Calendar dateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static boolean isSameDayMonthYear(Date date1, Date date2) {
		Calendar c1 = MainPresenter.dateToCalendar(date1);
		Calendar c2 = MainPresenter.dateToCalendar(date2);
		TimeIgnoringComparator timeIgnoringComparator = new TimeIgnoringComparator();
		return timeIgnoringComparator.compare(c1, c2) == 0;
	}
	
}