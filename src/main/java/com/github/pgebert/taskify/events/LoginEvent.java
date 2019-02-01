package com.github.pgebert.taskify.events;

import com.github.pgebert.taskify.view.vaadin.helper.LoginUser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginEvent {

	private LoginUser user;
	
}
