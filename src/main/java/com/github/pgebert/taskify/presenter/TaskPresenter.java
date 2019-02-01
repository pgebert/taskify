package com.github.pgebert.taskify.presenter;

import static com.github.pgebert.taskify.presenter.MainPresenter.onView;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.datasource.TaskDataFacade;
import com.github.pgebert.taskify.datasource.TaskState;
import com.github.pgebert.taskify.events.TaskEvents.TaskClickedEvent;
import com.github.pgebert.taskify.events.TaskEvents.TaskRemovedEvent;
import com.github.pgebert.taskify.events.TaskEvents.TaskSavedEvent;
import com.github.pgebert.taskify.events.ViewEvents.CurrentFilterChangeEvent;
import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.events.ViewEvents.NewClickedEvent;
import com.github.pgebert.taskify.view.api.TaskView;
import com.google.common.base.Predicate;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

/**
 * Presenter class for to handle task view
 * 
 * @author patri
 *
 */
public class TaskPresenter {

	private TaskDataFacade taskData;
	private TaskView taskView;

	private EventBus eventBus;

	/*
	 * Constructor for user view presenter to control the user data
	 */
	@Inject
	public TaskPresenter(EventBus eventBus, TaskDataFacade taskData, TaskView taskView) {
		
		this.taskData = taskData;
		this.taskView = taskView;

		this.eventBus = eventBus;
		this.eventBus.register(this);	
	}
	
	/**
	 * Listener to catch events after view change and refresh data.
	 * 
	 * @param event
	 */
	@Subscribe
	public void enter(EnterViewEvent event) {
		if (onView(this.taskView)) {
			List<Task> currentTasks = getCurrentTasks();
			this.taskView.setOldestRouteDate(oldestTaskDate(currentTasks));
			this.taskView.showItems(currentTasks);
		}
	}

	/**
	 * Listener to catch events from create new task button
	 * 
	 * @param event
	 */
	@Subscribe
	public void subscribeAddNewTaskClicked(NewClickedEvent event) {
		if (onView(this.taskView)) {
			this.taskView.showNewWindow();
		}
	}

	/**
	 * Listener to catch events from the new task form save button
	 * 
	 * @param event
	 */
	@Subscribe
	public void subscribeTaskSaved(TaskSavedEvent event) {
		this.taskData.update(event.getTask());
		this.taskView.showItems(getCurrentTasks());
	}

	/**
	 * Listener to catch events from the new task form delete button
	 * 
	 * @param event
	 */
	@Subscribe
	public void subscribeRouteRemoved(TaskRemovedEvent event) {
		this.taskData.delete(event.getTask());
		this.taskView.showItems(getCurrentTasks());
	}

	/**
	 * Listener to catch events from clicks on task table items
	 * 
	 * @param event
	 */
	@Subscribe
	public void subscribeRouteClicked(TaskClickedEvent event) {
		if (onView(this.taskView)) {
			this.taskView.showDetailWindow(event.getTask(), event.getItemId());
		}
	}

	public static Date oldestTaskDate(List<Task> tasks) {
		// don't change the order of the original List
		ArrayList<Task> routesCopy = new ArrayList<>(tasks);
		Collections.sort(routesCopy, boltedAtComparator());
		return routesCopy.size() > 0 ? routesCopy.get(0).getDate() : new Date();
	}

	public static Comparator<Task> boltedAtComparator() {
		return new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		};
	}
	
	/**
	 * Listener to catch events from current toggle button
	 * 
	 * @param event
	 */
	@Subscribe
	public void subscribeCurrentFilterChangeEvent(CurrentFilterChangeEvent event) {		
		if (onView(this.taskView)) {
			if (event.isCurrentFilter()) {				
				this.taskView.showItems(getCurrentTasks());
			} else {
				this.taskView.showItems(taskData.read());
			}
		}
	}

	/**
	 * Get all currently bolted routes from database which are not planned or
	 * removed
	 * 
	 * @return currently bolted routes from database
	 */
	public List<Task> getCurrentTasks() {
		return newArrayList(filter(this.taskData.read(), isBolted()));
	}

	/**
	 * Filter method for currently bolted routes which are not removed or planned.
	 * 
	 * @return whether the route is currently bolted
	 */
	public static Predicate<Task> isBolted() {
		return new Predicate<Task>() {
			@Override
			public boolean apply(Task task) {
				return task.getState().equals(TaskState.REALIZED);
			}
		};
	}

}
