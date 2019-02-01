package com.github.pgebert.taskify.datasource;

import static java.util.Comparator.naturalOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.pgebert.taskify.datasource.User.Sex;
import com.google.inject.Singleton;

import lombok.extern.java.Log;

@Log
@Singleton
public class UserDataDummy implements UserDataFacade {

	private Map<Long, User> users = new HashMap<Long, User>();

	public UserDataDummy() {
		// Dummy users
		String[] firstNames = { "Noah", "Liam", "Jacob", "Mason", "William", "Ethan", "Michael", "Alexander", "Jayden", "Daniel" };
		String[] lastNames = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor" };
		String[] addresses = {"Virginia Avenue 1, 70032 New Orleans", "Atlantic Avenue 4, 94016 San Francisco", "Pennsylvania Avenue 6, 77001 Huston", "Baltic Avenue 3, 10001 New York", "Vermont Avenue 5, 21201 Baltimore", "States Avenue 3, 33101 Miami"};
		String[] phones = {"202-555-0141", "202-555-0183", "202-555-0163", "202-555-0151", "202-555-0192", "202-555-0146", "202-555-0185", "202-555-0154", "202-555-0162", "202-555-0137"};
		String[] mails = {"Liny1951@fleckens.hu", "Porwhou39@cuvox.de", "Harce1943@rhyta.com", "Taind1992@armyspy.com", "Thell1981@gustr.com", "Thaposts36@einrot.com", "Scrioned1979@dayrep.com"};
				
		int nbrUsers = 8;
		for (int i = 0; i < nbrUsers; i++) {
			String name = firstNames[i % firstNames.length] + " " + lastNames[i % lastNames.length];
			users.put((long) i, new User(
					i, 
					name, 
					Sex.MALE, 
					DigestUtils.sha256Hex("password"), 
					addresses[i % addresses.length], 
					phones[i % phones.length], 
					mails[i % mails.length], 
					AccessRights.values()[(i+1) % AccessRights.values().length]
			));
		}
	}

	@Override
	public List<User> read() {
		return new ArrayList<User>(users.values());
	}

	@Override
	public User read(int id) {
		return users.getOrDefault((long) id, null);
	}

	@Override
	public User create(User user) {
		Optional<Long> maxId = users.keySet().stream().max(naturalOrder());
		user.setId(maxId.orElse(-1L) + 1);
		users.put(user.getId(), user);
		return user;
	}

	@Override
	public User update(User user) {

		if (users.containsKey(user.getId())) {
			users.replace(user.getId(), user);
		} else {
			create(user);
		}
		return user;
	}

	@Override
	public boolean delete(User user) {
		if (users.containsKey(user.getId())) {
			users.remove(user.getId());
			return true;
		}
		return false;
	}

}
