package com.github.pgebert.taskify.datasource;

import com.github.pgebert.taskify.datasource.annotations.TableColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	public static enum Sex {
		MALE, FEMALE, UNKNOWN;
	}

	private long id;
	
	@TableColumn(value = "Name", order = 0)
	private String name;	

	private Sex sex;	

	private String password;	
	
	@TableColumn(value = "Address", order = 1)
	private String address;
	
	@TableColumn(value = "Phone", order = 2)
	private String phone;
	
	@TableColumn(value = "Mail", order = 3)
	private String mail;
	
	@TableColumn(value = "Access", order = 4)
	private AccessRights rights;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
