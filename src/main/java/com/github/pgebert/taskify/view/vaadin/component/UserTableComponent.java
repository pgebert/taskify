package com.github.pgebert.taskify.view.vaadin.component;

import java.util.List;

import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.events.UserEvents.UserClickedEvent;
import com.google.common.eventbus.EventBus;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.UI;

import lombok.extern.java.Log;

@Log
public class UserTableComponent extends TableComponent<User> {

	private static final long serialVersionUID = 180805683298389810L;
	
	public UserTableComponent(final EventBus eventBus) {
		super(User.class, eventBus);
	}	
	
	@SuppressWarnings("serial")
	public void addItemClickedListener() {
		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
		    @Override
		    public void itemClick(ItemClickEvent itemClickEvent) {
		    	long itemId = (long) itemClickEvent.getItemId();
		        eventBus.post(new UserClickedEvent(container.getItem(itemId).getBean(), itemId));
		    }
		});
		
	}

	public void showUsers(List<User> users) {
		container.removeAllItems();
		container.addAll(users);
	}
	
	
	public void showNewWindow() {
		UI.getCurrent().addWindow(new UserDetailWindow(eventBus));
	}
	
	public void showDetailWindow(User user, long itemId) {
		UI.getCurrent().addWindow(new UserDetailWindow(user, itemId, eventBus));		
	}
	
	public void updateUser(User user) {
		container.addBean(user);
		table.refreshRowCache();
	}
	
	public void removeUser(long itemId) {
		container.removeItem(itemId);
		table.refreshRowCache();
	}

}