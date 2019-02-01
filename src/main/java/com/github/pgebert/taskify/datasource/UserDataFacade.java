package com.github.pgebert.taskify.datasource;

import java.util.List;

/**
 * CRUD facade for user data
 * @author patri
 *
 */
public interface UserDataFacade {
	
	/**
	 * Create new user in data
	 * @param user user to create
	 * @return created user
	 */
	User create(User user);
	
	/**
	 * Read all users from data
	 * @return all users from data
	 */
	List<User> read();	
	
	/**
	 * Read user with given ID
	 * @return user with given ID
	 */
	User read(int id);	
	
	/**
	 * Update user or create if not in data
	 * @param user user to update or create
	 * @return updated user
	 */
	User update(User user);
		
	/**
	 * Deletes user from data
	 * @param user user to delete
	 * @return success of deletion
	 */
	boolean delete(User user);
}
