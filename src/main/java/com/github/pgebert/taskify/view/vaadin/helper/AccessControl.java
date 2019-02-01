package com.github.pgebert.taskify.view.vaadin.helper;

import com.github.pgebert.taskify.datasource.AccessRights;
import com.github.pgebert.taskify.datasource.User;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Log
public class AccessControl {
	
	@Getter
	@Setter
	private User user = null;
	
	public void login(User user) {
		this.user = user;
	}
	
	public void logout() {
		this.user = null;
	}
	
	public boolean isLoggedIn() {
		return this.user != null;
	}
	
	public boolean hasAccessRights(AccessRights right) {
		return isLoggedIn() && right.equals(this.user.getRights());
	}

}
