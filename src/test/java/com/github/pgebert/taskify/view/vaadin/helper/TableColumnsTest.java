package com.github.pgebert.taskify.view.vaadin.helper;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.pgebert.taskify.datasource.annotations.TableColumn;
import com.github.pgebert.taskify.view.vaadin.helper.TableColumns;

public class TableColumnsTest {

	static class Columns {

		@TableColumn(value = "Column 1", order = 1)
		private String column1;

		@TableColumn(value = "Column 2", order = 3)
		private String column2;

		@TableColumn(value = "Column 3", order = 2)
		private String column3;

		@SuppressWarnings("unused")
		private String column4;
	}

	@Test
	public void getColumnsTest() throws IntrospectionException {
		List<String> expected = new ArrayList<String>();
		expected.add("column1");
		expected.add("column3");
		expected.add("column2");

		assertThat(TableColumns.getColumns(Columns.class), equalTo(expected));
	}

	@Test
	public void getColumnHeadersTest() throws Exception {
		List<String> expected = new ArrayList<String>();
		expected.add("Column 1");
		expected.add("Column 3");
		expected.add("Column 2");

		Class<Columns> clazz = Columns.class;
		assertThat(TableColumns.getColumnHeaders(clazz), equalTo(expected));
	}

}
