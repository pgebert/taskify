package com.github.pgebert.taskify.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.GregorianCalendar;

import org.junit.Test;

import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.datasource.TaskState;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.datasource.UserDataDummy;
import com.github.pgebert.taskify.presenter.MainPresenter;

public class HighlightOldestRoutesTest {
	
	UserDataDummy userDataDummy = new UserDataDummy();
	User owner = userDataDummy.read().get(0);
	
	Task task = new Task(0, "Test Task 1", owner, 1, new GregorianCalendar(2016, 9, 1, 6, 15).getTime(), new GregorianCalendar(2016, 9, 1, 6, 15).getTime(), TaskState.DONE);
	Task task2 = new Task(1, "Test Task 2", owner, 1, new GregorianCalendar(2016, 9, 1, 6, 15).getTime(), new GregorianCalendar(2016, 9, 1, 6, 15).getTime(), TaskState.DONE);
	Task task3 = new Task(2, "Test Task 3", owner, 1, new GregorianCalendar(2016, 9, 1, 6, 15).getTime(), new GregorianCalendar(2016, 9, 1, 6, 15).getTime(), TaskState.DONE);

	@Test
	public void compareOnlyDayMonthAndYear() {
		assertThat(MainPresenter.isSameDayMonthYear(task2.getStart(), task3.getStart()), is(true));
	}

}
