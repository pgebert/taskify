package com.github.pgebert.taskify.events;

import java.util.Date;

import com.github.pgebert.taskify.view.vaadin.helper.ViewType;
import com.vaadin.navigator.View;

import lombok.AllArgsConstructor;
import lombok.Data;


/*
 * Event bus view events are listed here as inner classes.
 */
public abstract class ViewEvents {
	
	@Data
	@AllArgsConstructor
	public static final class PostViewChangeEvent {
        private final ViewType viewType;
        private final View view;
    }
	
	@Data
	@AllArgsConstructor
	public static final class CurrentFilterChangeEvent {
        private final boolean currentFilter;
    }
	
	@Data
	@AllArgsConstructor
	public static final class DateFilterChangeEvent {
        private final Date beginDate;
        private final Date endDate;
    }
	
	@Data
	@AllArgsConstructor
	public static final class CalendarFilterChangeEvent {
        private final String item;
    }
	
	@Data
	@AllArgsConstructor
	public static final class NewClickedEvent {
    }
	
	@Data
	@AllArgsConstructor
	public static final class EnterViewEvent {
    }

}
