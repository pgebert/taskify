package com.github.pgebert.taskify.view.api;

import java.util.Date;
import java.util.List;

import com.github.pgebert.taskify.datasource.Task;

public interface TaskView {
	
	void showItems(List<Task> tasks);

	void updateRoute(Task task);

	void removeRoute(long itemId);
	
	void showDetailWindow(Task task, long itemId);	
	
	void showNewWindow(Task placeholder);

	void showNewWindow();

	void setOldestRouteDate(Date oldestRouteDate);
	
	void resetFilter();

}