package com.github.pgebert.taskify.view.vaadin.component;


import java.util.Date;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.datasource.TaskState;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.events.TaskEvents.TaskRemovedEvent;
import com.github.pgebert.taskify.events.TaskEvents.TaskSavedEvent;
import com.github.pgebert.taskify.view.vaadin.helper.AccessControl;
import com.google.common.eventbus.EventBus;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


public class TaskDetailWindow extends Window {

	private static final long serialVersionUID = -6151147481596115592L;
	
	private long itemId;
	
	private VerticalLayout content;
	
	public TaskDetailWindow(EventBus eventBus) {
		super("Add new task");
		initLayout();	
		
		User owner = ((BaseUI) UI.getCurrent()).getAccessControl().getUser();		
		Task task = new Task(-1L, "", owner, 1, new Date(), 0, TaskState.DONE);
		BeanItem<Task> item = new BeanItem<Task>(task);
		content.addComponent(new TaskForm(item, false, eventBus));
	}
	
	public TaskDetailWindow(Task placeholder, EventBus eventBus) {
		super("Add new task");
		initLayout();
		
		BeanItem<Task> item = new BeanItem<Task>(placeholder);
		content.addComponent(new TaskForm(item, false, eventBus));
	}
	
	public TaskDetailWindow(Task task, long itemId, EventBus eventBus) {
		super(task.getTitle());
		initLayout();
		this.itemId = itemId;

		BeanItem<Task> item = new BeanItem<Task>(task);
		content.addComponent(new TaskForm(item, true, eventBus));
	}
	
	private void initLayout() {
		center();
		setModal(true);
		
		this.content = new VerticalLayout();
		content.setMargin(true);
		setContent(content);
	}

	private class TaskForm extends CustomComponent {

		private static final long serialVersionUID = 4005767512857836946L;

		@PropertyId("title")
		TextField title = new TextField("Title");
		
		@PropertyId("owner")
		NativeSelect owner = new NativeSelect("Owner");

		@PropertyId("date")
		DateField date = new DateField("Date");
		
		@PropertyId("time")
		TextField time = new TextField("time");
		
		@PropertyId("priority")
		NativeSelect priority = new NativeSelect("Priority");
		
		@PropertyId("state")
		NativeSelect state = new NativeSelect("Status");

		public TaskForm(final BeanItem<Task> item, boolean updateMode, final EventBus eventBus) {
									
			for (TaskState s : TaskState.values()) {
				state.addItem(s);
				state.setItemCaption(s, s.getTaskState());
			}
			state.setNullSelectionAllowed(false);
			
			for (int i = 0; i < 5; i++) {
				priority.addItem(i+1);
				priority.setItemCaption(i+1, "Priotity " + (i+1));
			}
			priority.setNullSelectionAllowed(false);
			
			User user = ((BaseUI) UI.getCurrent()).getAccessControl().getUser();
			owner.addItem(user);
			owner.setItemCaption(user, user.getName());
			owner.setNullSelectionAllowed(false);

			FormLayout layout = new FormLayout();
			layout.addComponent(title);
			layout.addComponent(owner);
			layout.addComponent(date);
			layout.addComponent(time);
			layout.addComponent(priority);
			layout.addComponent(state);

			final FieldGroup binder = new FieldGroup(item);
			binder.bindMemberFields(this);
			
			HorizontalLayout bottomToolbox = new HorizontalLayout();
			
			// Add save button
			Button saveBtn = new Button("Save");
			saveBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			saveBtn.addClickListener(event -> {
				Task bean = item.getBean();
				// Set current user as route setter
				// TODO: Write select form
				AccessControl control = ((BaseUI) UI.getCurrent()).getAccessControl();				
				bean.setOwner(control.getUser());
				
				try {
					binder.commit();
					eventBus.post(new TaskSavedEvent(bean));
				} catch (CommitException e) {
					// do nothing, eventually show error message
				}
				close();
			});
			bottomToolbox.addComponent(saveBtn);
			
			// Add remove button
			if (updateMode) {
				Button removeBtn = new Button("Delete");
				removeBtn.addStyleName(ValoTheme.BUTTON_DANGER);
				removeBtn.addClickListener(event -> {
					Task bean = item.getBean();
					
					try {
						binder.commit();
						eventBus.post(new TaskRemovedEvent(bean, itemId));
					} catch (CommitException e) {
						// do nothing, eventually show error message
					}
					close();
				});
				bottomToolbox.addComponent(removeBtn);
			}			
			
			layout.addComponent(bottomToolbox);
			setCompositionRoot(layout);
		}
	}

}