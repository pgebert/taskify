package com.github.pgebert.taskify.events;

import com.github.pgebert.taskify.datasource.Task;

import lombok.AllArgsConstructor;
import lombok.Data;


/*
 * Event bus task events are listed here as inner classes.
 */
public abstract class TaskEvents {
	
	@Data
	@AllArgsConstructor
	public static class TaskClickedEvent {		
		private Task task;
		private long itemId;
	}
	
	@Data
	@AllArgsConstructor
	public static class TaskRemovedEvent {		
		private Task task;
		private long itemId;
	}
	
	@Data
	@AllArgsConstructor
	public static class TaskSavedEvent {
		private Task task;
	}
}
