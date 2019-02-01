package com.github.pgebert.taskify.datasource;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.datasource.TaskDataDummy;
import com.github.pgebert.taskify.datasource.UserDataDummy;

public class DummyRouteDataTest {

	@Test
	public void testGetAll() {
		Task task = new TaskDataDummy(new UserDataDummy()).read().get(0);
		assertEquals("Route 0", task.getTitle());
		assertEquals("artofclimbing", task.getOwner().getName().toString());
	}

}
