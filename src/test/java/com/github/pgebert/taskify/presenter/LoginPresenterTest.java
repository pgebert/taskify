package com.github.pgebert.taskify.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.pgebert.taskify.datasource.AccessRights;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.datasource.UserDataDummy;
import com.github.pgebert.taskify.datasource.User.Sex;
import com.github.pgebert.taskify.presenter.LoginPresenter;
import com.github.pgebert.taskify.view.vaadin.helper.LoginUser;
import com.google.common.eventbus.EventBus;

public class LoginPresenterTest {

	LoginPresenter presenter = new LoginPresenter(new EventBus(), new UserDataDummy());
	LoginUser loginUser = new LoginUser("artofclimbing", "password");
	@Test
	public void loginFailsOnWrongData() {
		User user = new User(0, "testuser", Sex.MALE, "testpassword", null, null, null, AccessRights.ADMIN);
		assertThat(presenter.checkLogin(loginUser, user), is(false));
	}
	
	@Test
	public void loginWorksWithExistingUser() {
		User user = new User(0, "artofclimbing", Sex.MALE, "password", null, null, null, AccessRights.ADMIN);
		assertThat(presenter.checkLogin(loginUser, user), is(true));
	}
	
	@Test
	public void loginFailsWithWrongPassword() {
		User user = new User(0, "artofclimbing", Sex.MALE, "password1", null, null, null, AccessRights.ADMIN);
		assertThat(presenter.checkLogin(loginUser, user), is(false));
	}
	
}
