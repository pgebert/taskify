package com.github.pgebert.taskify.view.vaadin.helper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.pgebert.taskify.datasource.annotations.TableColumn;
import com.google.common.collect.Lists;

public class TableColumns {

	public static List<String> getColumnHeaders(Class<?> clazz)
			throws NoSuchFieldException, SecurityException {
		return getColumnHeaders(clazz, TableColumns.getColumns(clazz));
	}

	public static List<String> getColumnHeaders(Class<?> clazz,
			List<String> fields) throws NoSuchFieldException, SecurityException {
		List<String> headers = Lists.newArrayList();
		for (String field : fields) {
			headers.add(clazz.getDeclaredField(field)
					.getAnnotation(TableColumn.class).value());
		}
		return headers;
	}

	public static List<String> getColumns(Class<?> clazz) {
		Map<Integer, String> visibleColumns = new TreeMap<Integer, String>();
		for (Field field : clazz.getDeclaredFields()) {
			TableColumn annotation = field.getAnnotation(TableColumn.class);
			if (annotation != null) {
				visibleColumns.put(annotation.order(), field.getName());
			}
		}
		return Lists.newArrayList(visibleColumns.values());
	}

}
