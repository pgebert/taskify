package com.github.pgebert.taskify.view.vaadin.impl;

import java.util.List;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.datasource.AccessRights;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.events.ViewEvents.NewClickedEvent;
import com.github.pgebert.taskify.view.api.UsersView;
import com.github.pgebert.taskify.view.vaadin.component.MenuViewComponent;
import com.github.pgebert.taskify.view.vaadin.component.UserTableComponent;
import com.github.pgebert.taskify.view.vaadin.helper.AccessControl;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.extern.java.Log;


@Singleton
@Log
public class UsersViewImpl extends HorizontalLayout implements UsersView, View {

	private static final long serialVersionUID = 2358815687796478368L;

	private UserTableComponent userTableComponent;
	private Button newUser;
	private EventBus eventBus;

	@Inject
	public UsersViewImpl(MenuViewComponent menuView, final EventBus eventBus) {
		this.eventBus = eventBus;
		
		this.userTableComponent = new UserTableComponent(eventBus);
		this.userTableComponent.addItemClickedListener();

		setSizeFull();
		VerticalLayout rightSide = new VerticalLayout();

		Label title = new Label("Users");
		title.addStyleName("h1");
		title.setWidth(95, Unit.PERCENTAGE);

		newUser = new Button("Create User");
		newUser.setIcon(FontAwesome.PLUS);
		newUser.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		newUser.addClickListener(event -> eventBus.post(new NewClickedEvent()));
		

		HorizontalLayout bottom = new HorizontalLayout();
		bottom.addStyleName("bottom");
		bottom.setWidth(85, Unit.PERCENTAGE);
		bottom.addComponent(newUser);
		bottom.setComponentAlignment(newUser, Alignment.MIDDLE_RIGHT);

		rightSide.addComponent(title);
		rightSide.addComponent(userTableComponent);
		rightSide.setComponentAlignment(userTableComponent, Alignment.TOP_CENTER);
		userTableComponent.setWidth(95, Unit.PERCENTAGE);
		rightSide.addComponent(bottom);
		rightSide.setComponentAlignment(title, Alignment.TOP_CENTER);
		rightSide.setSizeFull();
		rightSide.setExpandRatio(title, 1.5f);
		rightSide.setExpandRatio(userTableComponent, 5f);
		rightSide.setExpandRatio(bottom, 1.5f);
		
		addComponent(menuView);
		addComponent(rightSide);

		setExpandRatio(menuView, 1f);
		setExpandRatio(rightSide, 6f);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		this.eventBus.post(new EnterViewEvent());	
		// TODO: Remove this snippet
		AccessControl control = ((BaseUI) UI.getCurrent()).getAccessControl();
		newUser.setVisible(control.hasAccessRights(AccessRights.ADMIN));
	}

	@Override
	public void showUsers(List<User> users) {
		this.userTableComponent.showUsers(users);
	}

	@Override
	public void showNewWindow() {
		this.userTableComponent.showNewWindow();
	}
	
	@Override
	public void updateUser(User user) {
		this.userTableComponent.updateUser(user);
	}

	@Override
	public void showDetailWindow(User user, long itemId) {
		this.userTableComponent.showDetailWindow(user, itemId);		
	}

	@Override
	public void removeUser(long itemId) {
		this.userTableComponent.removeUser(itemId);		
	}

}
