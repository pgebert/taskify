package com.github.pgebert.taskify.datasource;

import java.util.Arrays;
import java.util.List;

public enum AccessRights {

	USER, ADMIN;

	public static boolean hasRight(User user, AccessRights right) {

		boolean access = false;

		if (user != null && right != null) {
			List<AccessRights> rights = Arrays.asList(AccessRights.values());
			access = rights.indexOf(user.getRights()) >= rights.indexOf(right);
		}
		return access;
	}

}
