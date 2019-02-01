package com.github.pgebert.taskify.presenter;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.datasource.AccessRights;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.datasource.UserDataFacade;
import com.github.pgebert.taskify.datasource.User.Sex;
import com.github.pgebert.taskify.events.LoginEvent;
import com.github.pgebert.taskify.events.LoginSuccessEvent;
import com.github.pgebert.taskify.view.vaadin.helper.LoginUser;
import com.github.pgebert.taskify.view.vaadin.helper.ViewType;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import lombok.extern.java.Log;

@Log
public class LoginPresenter {

	private final static Logger logger = Logger.getLogger(LoginPresenter.class.getName());
	private List<User> users;
	private EventBus eventBus;
	private UserDataFacade userDataFacade;

	@Inject
	public LoginPresenter(EventBus eventBus, UserDataFacade users) {

		userDataFacade = users;
		this.users = users.read();
		this.eventBus = eventBus;
		eventBus.register(this);
	}

	public boolean checkLogin(LoginUser loginUser, User dbUser) {
		 return dbUser.getName() != null && dbUser.getName().equals(loginUser.getName()) 
				&& dbUser.getPassword() != null &&  dbUser.getPassword().equals(loginUser.getPassword());
	}

	@Subscribe
	public void subscribeLogin(LoginEvent event) {
		LoginUser currentUser = event.getUser();

		User dbUser = findUserInDB(currentUser).orElse(new User(0, null, Sex.MALE, null, null, null, null, AccessRights.USER));

		if (checkLogin(currentUser, dbUser)) {
			BaseUI currentUI = ((BaseUI) UI.getCurrent());
			currentUI.getAccessControl().login(dbUser);
			eventBus.post(new LoginSuccessEvent());
			currentUI.getNavigator().navigateTo(ViewType.DASHBOARD.getViewLink());
		} else  {
			Notification.show("Wrong name or password!", Notification.Type.TRAY_NOTIFICATION);
		}
	}

	public Optional<User> findUserInDB(LoginUser loginUser) {
		this.users = userDataFacade.read();
		return users.stream().filter(u -> u.getName().equals(loginUser.getName())).findFirst();
	}

}
