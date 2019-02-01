package com.github.pgebert.taskify.presenter;

import static java.lang.Boolean.TRUE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import com.github.pgebert.taskify.datasource.AccessRights;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.datasource.UserDataDummy;
import com.github.pgebert.taskify.datasource.UserDataFacade;
import com.github.pgebert.taskify.datasource.User.Sex;
import com.github.pgebert.taskify.presenter.LoginPresenter;
import com.github.pgebert.taskify.view.vaadin.helper.LoginUser;
import com.google.common.eventbus.EventBus;

public class MainPresenterTest {

	UserDataFacade userData = new UserDataDummy();
	LoginPresenter loginPresenter = new LoginPresenter(new EventBus(), userData);

	@Test
	public void canCreateNewAccount() {
		String name = "Mr. X";
		String password = DigestUtils.sha256Hex("genPassword");

		User user = new User(0, name, Sex.MALE, password, null, null, null, AccessRights.USER);
		userData.create(user);

		Optional<User> findUserInDB = loginPresenter.findUserInDB(new LoginUser(name, password));
		assertThat(findUserInDB.isPresent(), is(TRUE));
		assertThat(findUserInDB.get().getId(), is(10L));
	}

}
