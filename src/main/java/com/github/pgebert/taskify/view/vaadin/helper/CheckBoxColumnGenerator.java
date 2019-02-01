package com.github.pgebert.taskify.view.vaadin.helper;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class CheckBoxColumnGenerator implements ColumnGenerator {

	private static final long serialVersionUID = 1450612096575337025L;
	private String originalColumn;

	public CheckBoxColumnGenerator(String originalColumn) {
		this.originalColumn = originalColumn;
	}

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Property<Boolean> prop = source.getItem(itemId).getItemProperty(
				originalColumn);
		CheckBox checkBox = new CheckBox("", prop.getValue());
		checkBox.setReadOnly(true);
		return checkBox;
	}
}