package com.github.pgebert.taskify.view.api;

import java.util.List;

import com.github.pgebert.taskify.datasource.Task;

public interface CalendarView {

	void showItems(List<Task> items);

}