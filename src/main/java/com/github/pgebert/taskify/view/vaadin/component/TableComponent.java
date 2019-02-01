package com.github.pgebert.taskify.view.vaadin.component;

import com.github.pgebert.taskify.view.vaadin.helper.TableColumns;
import com.google.common.eventbus.EventBus;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

public class TableComponent<T> extends CustomComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5459002915593924010L;

	protected BeanContainer<Long, T> container;
	protected EventBus eventBus;
	protected Table table;
	
	/**
	 * @param table
	 */
	public TableComponent(Class<T> clazz, EventBus eventBus) {
		super();
		
		this.eventBus = eventBus;
		setSizeFull();

		container = new BeanContainer<Long, T>(clazz);
		container.setBeanIdProperty("id");

		table = new Table("", container);
		table.setSelectable(true);
		table.setImmediate(true);
		table.setWidth(100, Unit.PERCENTAGE);
		table.setHeight(100, Unit.PERCENTAGE);
		table.setVisibleColumns(TableColumns.getColumns(clazz).toArray());
		try {
			table.setColumnHeaders(TableColumns.getColumnHeaders(clazz).toArray(new String[0]));
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setCompositionRoot(table);
	}
	
	public void setVisibleColums(Object... visibleColumns) {
		table.setVisibleColumns(visibleColumns);
	}
	
	public void setColumnHeaders(String... columnHeaders) {
		table.setColumnHeaders(columnHeaders);
	}
	
	public void setCellStyleGenerator(CellStyleGenerator cellStyleGenerator) {
		table.setCellStyleGenerator(cellStyleGenerator);
	}

}
