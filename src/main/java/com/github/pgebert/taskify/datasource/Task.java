package com.github.pgebert.taskify.datasource;

import java.util.Date;

import com.github.pgebert.taskify.datasource.annotations.TableColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	
	private long id;

	@TableColumn(value = "Title", order = 2)
	private String title;

	@TableColumn(value = "Owner", order = 3)
	private User owner;

	@TableColumn(value = "Priority", order = 6)
	private int priority;

	@TableColumn(value = "Date", order = 4)
	private Date date;
	
	@TableColumn(value = "Time", order = 5)
	private int time;

	@TableColumn(value = "State", order = 1)
	private TaskState state;


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Route - " + this.title;
	}

}