package com.github.pgebert.taskify.view.vaadin.impl;

import java.util.Date;
import java.util.List;

import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.events.ViewEvents.CurrentFilterChangeEvent;
import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.events.ViewEvents.NewClickedEvent;
import com.github.pgebert.taskify.view.api.TaskView;
import com.github.pgebert.taskify.view.vaadin.component.MenuViewComponent;
import com.github.pgebert.taskify.view.vaadin.component.TaskTableComponent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Singleton
public class TaskViewImpl extends HorizontalLayout implements TaskView,
		View {

	private static final long serialVersionUID = 2358815687796478368L;
	
	private TaskTableComponent taskTableComponent;
	CheckBox currentBtn;
	private EventBus eventBus;

	@Inject
	public TaskViewImpl(TaskTableComponent taskTableComponent,
			MenuViewComponent menuView, final EventBus eventBus) {
		
		this.eventBus = eventBus;
		
		this.taskTableComponent = taskTableComponent;
		taskTableComponent.addItemClickedListener();
				
		setSizeFull();
		VerticalLayout rightSide = new VerticalLayout();

		rightSide.addComponent(buildHeader());

		Button newRoute = new Button("New Task");
		newRoute.setIcon(FontAwesome.PLUS);
		newRoute.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		newRoute.addClickListener(event -> eventBus.post(new NewClickedEvent()));

		HorizontalLayout bottom = new HorizontalLayout();
		bottom.addStyleName("bottom");
		bottom.setWidth(85, Unit.PERCENTAGE);
		bottom.addComponent(newRoute);
		bottom.setComponentAlignment(newRoute, Alignment.MIDDLE_RIGHT);

		rightSide.addComponent(taskTableComponent);
		rightSide.setComponentAlignment(taskTableComponent, Alignment.TOP_CENTER);
		taskTableComponent.setWidth(95,Unit.PERCENTAGE);
		rightSide.addComponent(bottom);
		rightSide.setSizeFull();
		rightSide.setExpandRatio(taskTableComponent,5f);
		rightSide.setExpandRatio(bottom, 1.5f);
		addComponent(menuView);
		addComponent(rightSide);

		setExpandRatio(menuView, 1f);
		setExpandRatio(rightSide, 6f);
	}
	
	private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setWidth(95,Unit.PERCENTAGE);
        Responsive.makeResponsive(header);

        Label title = new Label("Tasks");
        title.setWidth(95,Unit.PERCENTAGE);
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        header.setComponentAlignment(title, Alignment.TOP_LEFT);
        
        this.currentBtn = new CheckBox("Hide Done", false);
        currentBtn.setValue(false);
        currentBtn.setImmediate(true);
        currentBtn.addValueChangeListener(event -> eventBus.post(new CurrentFilterChangeEvent(currentBtn.getValue())));        
                         
		HorizontalLayout tools = new HorizontalLayout();		
		tools.addComponent(currentBtn);
		tools.addStyleName("toolbar");
		header.addComponent(tools);

        return header;
    }	


	@Override
	public void showItems(List<Task> tasks) {
		this.taskTableComponent.showItems(tasks);
	}

	@Override
	public void showNewWindow() {
		this.taskTableComponent.showNewWindow();
	}
	
	@Override
	public void showNewWindow(Task placeholder) {
		this.taskTableComponent.showNewWindow(placeholder);
	}
	
	@Override
	public void updateRoute(Task task) {
		this.taskTableComponent.updateItem(task);
	}

	@Override
	public void showDetailWindow(Task task, long itemId) {
		this.taskTableComponent.showDetailWindow(task, itemId);		
	}

	@Override
	public void removeRoute(long itemId) {
		this.taskTableComponent.removeItem(itemId);		
	}

	@Override
	public void setOldestRouteDate(Date oldestRouteDate) {
		this.taskTableComponent.highlightDate(oldestRouteDate);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		this.eventBus.post(new EnterViewEvent());		
	}

	@Override
	public void resetFilter() {
		currentBtn.setValue(false);		
	}

}
