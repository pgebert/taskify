package com.github.pgebert.taskify.view.vaadin.component;

import static com.github.pgebert.taskify.presenter.MainPresenter.isSameDayMonthYear;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.datasource.TaskState;
import com.github.pgebert.taskify.events.TaskEvents.TaskClickedEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

public class TaskTableComponent extends TableComponent<Task> {

	private static final long serialVersionUID = -4068681664852999156L;
	

	@Inject
	public TaskTableComponent(final EventBus eventBus) throws NoSuchFieldException, SecurityException {
		super(Task.class, eventBus);
		
		table.addGeneratedColumn("state", (source, itemId, columnId) -> StateToIconGenerator(source, itemId, columnId));
		table.setConverter("start", new StringToMediumDateFormatConverter());
		table.setConverter("end", new StringToMediumDateFormatConverter());
	}
	
	public void setColumnHeaders(String... columnHeaders) {
		table.setColumnHeaders(columnHeaders);
	}
	
	@SuppressWarnings("serial")
	public void addItemClickedListener() {
		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
		    @Override
		    public void itemClick(ItemClickEvent itemClickEvent) {
		    	long itemId = (long) itemClickEvent.getItemId();
		        eventBus.post(new TaskClickedEvent(container.getItem(itemId).getBean(), itemId));
		    }
		});
		
	}

	public void showItems(List<Task> tasks) {
		container.removeAllItems();
		container.addAll(tasks);
	}
	
	
	public void showNewWindow() {
		UI.getCurrent().addWindow(new TaskDetailWindow(eventBus));
	}
	
	public void showNewWindow(Task placeholder) {
		UI.getCurrent().addWindow(new TaskDetailWindow(placeholder, eventBus));
	}
	
	public void showDetailWindow(Task task, long itemId) {
		UI.getCurrent().addWindow(new TaskDetailWindow(task, itemId, eventBus));		
	}
	
	public void updateItem(Task task) {
		container.addBean(task); 
		table.refreshRowCache();
	}
	
	public void removeItem(long itemId) {
		container.removeItem(itemId);
		table.refreshRowCache();
	}

	public void highlightDate(Date highlightDate) {
		table.setCellStyleGenerator(new HighlightTasksCellStyleGenerator(highlightDate));
	}
	
	/**
	 * Style generator to highlight oldest task
	 * @author patri
	 *
	 */
	static final class HighlightTasksCellStyleGenerator implements Table.CellStyleGenerator {
		private static final long serialVersionUID = -581001138128483460L;
		
		private Date oldestTaskDate;

		public HighlightTasksCellStyleGenerator(Date oldestTaskDate) {
			this.oldestTaskDate = oldestTaskDate;
		}
		@Override
		public String getStyle(Table source, Object itemId, Object propertyId) {
			Item item = source.getItem(itemId);
			Date date = (Date) item.getItemProperty("start").getValue();
			TaskState state = (TaskState) item.getItemProperty("state").getValue();

			if (oldestTaskDate != null && isSameDayMonthYear(date, oldestTaskDate) && state.equals(TaskState.OPEN)) {
				return "highlight-red";
			} 
			return null;
		}
	}
	
	/**
	 * Converter for date formatting
	 * @author patri
	 *
	 */
	static final class StringToMediumDateFormatConverter extends StringToDateConverter {
		private static final long serialVersionUID = 2132195415703958387L;

		@Override
		protected DateFormat getFormat(Locale locale) {
			return SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
		}
	}
	
		
	/**
	 * Converter for strings to label
	 * @author patri
	 *
	 */
	public Object StateToIconGenerator(Table source, Object itemId, Object columnId) {
        TaskState state = (TaskState) source.getItem(itemId).getItemProperty("state").getValue();
		Label label = new Label(state.getIcon().getHtml(), ContentMode.HTML);
        return label;
    }
	
}