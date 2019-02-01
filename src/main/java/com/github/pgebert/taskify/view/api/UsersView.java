package com.github.pgebert.taskify.view.api;

import java.util.List;

import com.github.pgebert.taskify.datasource.User;

public interface UsersView {
	
	void showUsers(List<User> users);	

	void updateUser(User user);
	
	void removeUser(long itemId);

	void showDetailWindow(User user, long itemId);	
	
	void showNewWindow();

}