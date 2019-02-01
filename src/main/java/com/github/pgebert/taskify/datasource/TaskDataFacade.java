package com.github.pgebert.taskify.datasource;

import java.util.Date;
import java.util.List;

/**
 * CRUD facade for task data
 * @author patri
 *
 */
public interface TaskDataFacade {

	/**
	 * Create new item in data
	 * @param item item to create
	 * @return created item
	 */
	Task create(Task item);
	
	
	/**
	 * Read all items from data
	 * @return all items from data
	 */
	List<Task> read();
	
	/**
	 * Returns all items of the given route setter bolted between the two given dates
	 * @param owner  the task owner
	 * @param beginDate earliest date of the item
	 * @param endDate latest date of the item
	 * @return a list of all fitting routes
	 */
	List<Task> read(User owner, Date beginDate, Date endDate);
	
	/**
	 * Update item or create if not in data
	 * @param item item to update or create
	 * @return updated item
	 */
	Task update(Task item);
			
	/**
	 * Deletes item from data
	 * @param item item to delete
	 * @return success of deletion
	 */
	boolean delete(Task item);
	
}
