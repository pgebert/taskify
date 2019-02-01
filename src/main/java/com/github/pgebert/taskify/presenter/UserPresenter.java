package com.github.pgebert.taskify.presenter;

import static com.github.pgebert.taskify.presenter.MainPresenter.onView;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.datasource.UserDataFacade;
import com.github.pgebert.taskify.events.LoginSuccessEvent;
import com.github.pgebert.taskify.events.UserEvents.UserClickedEvent;
import com.github.pgebert.taskify.events.UserEvents.UserRemovedEvent;
import com.github.pgebert.taskify.events.UserEvents.UserSavedEvent;
import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.events.ViewEvents.NewClickedEvent;
import com.github.pgebert.taskify.view.api.UsersView;
import com.github.pgebert.taskify.view.vaadin.helper.AccessControl;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.vaadin.ui.UI;

import lombok.extern.java.Log;


/**
 * Presenter class for to handle user view 
 * @author patri
 *
 */
@Log
public class UserPresenter {
	
	private UserDataFacade userData;
	
	private UsersView usersView;
	
	private EventBus eventBus;
	
	
	/*
	 * Constructor for user view presenter to control the user data
	 */
	@Inject
	public UserPresenter(EventBus eventBus, UserDataFacade userData, UsersView usersView) {
		
		this.eventBus = eventBus;
		this.userData = userData;
		
		this.usersView = usersView;
		
		this.eventBus.register(this);		
			
	}
	
	/**
	 * Listener to catch events after view change and refresh data.
	 * 
	 * @param event
	 */
	@Subscribe
	public void enter(EnterViewEvent event) {
		if (onView(this.usersView)) {
			this.usersView.showUsers(userData.read());
		}
	}
	
	/**
	 * Listener to catch events from create new user button
	 * @param event 
	 */
	@Subscribe
	public void subscribeAddNewUserClicked(NewClickedEvent event) {
		if (onView(this.usersView)) {
			this.usersView.showNewWindow();
		}
	}
	
	/**
	 * Listener to catch events from the new user form save button
	 * @param event
	 */
	@Subscribe
	public void subscribeUserSaved(UserSavedEvent event) {
		this.usersView.updateUser(this.userData.update(event.getUser()));
		// Fallback - Refresh all user data 
		if (onView(this.usersView)) {
			this.usersView.showUsers(userData.read());
		}
		// Refresh menu bar
		AccessControl control = ((BaseUI) UI.getCurrent()).getAccessControl();
		if (event.getUser().equals(control.getUser())) {
			this.eventBus.post(new LoginSuccessEvent());
		}
	}
	
	/**
	 * Listener to catch events from the new user form delete button
	 * @param event
	 */
	@Subscribe
	public void subscribeUserRemoved(UserRemovedEvent event) {
		if (this.userData.delete(event.getUser())) {
			this.usersView.removeUser(event.getItemId());
		}
	}
	
	/**
	 * Listener to catch events from clicks on user table items
	 * @param event
	 */
	@Subscribe
	public void subscribeUserClicked(UserClickedEvent event) {
		this.usersView.showDetailWindow(event.getUser(), event.getItemId());
	}
	
}
