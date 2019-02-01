package com.github.pgebert.taskify.events;

import com.github.pgebert.taskify.datasource.User;

import lombok.AllArgsConstructor;
import lombok.Data;


/*
 * Event bus user events are listed here as inner classes.
 */
public abstract class UserEvents {
	
	@Data
	@AllArgsConstructor
	public static class UserClickedEvent {		
		private User user;
		private long itemId;
	}
	
	@Data
	@AllArgsConstructor
	public static class UserRemovedEvent {		
		private User user;
		private long itemId;
	}
	
	@Data
	@AllArgsConstructor
	public static class UserSavedEvent {
		private User user;
	}
}
