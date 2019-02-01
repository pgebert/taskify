package com.github.pgebert.taskify.datasource;

import static java.util.Comparator.naturalOrder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.pgebert.taskify.datasource.Task.TaskBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class TaskDataDummy implements TaskDataFacade {

	private Map<Long, Task> tasks = new HashMap<Long, Task>();

	@Inject
	public TaskDataDummy(UserDataFacade users) {		
		int amount = 140;		
		
		List<User> owners = users.read();			
		TaskBuilder builder = new TaskBuilder();		
		
		for (int i = 0; i < amount; i++) {
			builder.id(i);
			builder.title("Task " + i);
			builder.owner(owners.get(i % owners.size()));
			builder.priority((i % 5)+1);
			builder.date(Date.from(ZonedDateTime.now().minusDays((i+1)*(int)((Math.random() * 10)+1)).toInstant()));
			builder.time(((i%3)+1));
			builder.state(TaskState.values()[i%TaskState.values().length]);
			
			tasks.put((long) i, builder.build());
		}
	}

	@Override
	public List<Task> read() {
		return new ArrayList<Task>(tasks.values());
	}

	@Override
	public Task create(Task task) {
		Optional<Long> maxId = tasks.keySet().stream().max(naturalOrder());
		task.setId(maxId.orElse(-1L) + 1);
		tasks.put(task.getId(), task);
		return task;
	}

	@Override
	public Task update(Task task) {
		
		if (tasks.containsKey(task.getId())) {
			tasks.replace(task.getId(), task);
		} else {
			create(task);
		}
		return task;
	}

	@Override
	public boolean delete(Task task) {
		if (tasks.containsKey(task.getId())) {
			tasks.remove(task.getId());
			return true;
		} 
		return false;
	}
	
	public static Date removeTime(Date date) {    
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime(); 
    }
	
	@Override
	public List<Task> read(User owner, Date beginDate, Date endDate) {
		
		beginDate = removeTime(beginDate);
		endDate = removeTime(endDate);
		
		List<Task> matches = new ArrayList<Task>();
		
		for (Task task : this.tasks.values()) {
			if (task.getOwner().equals(owner)
					&& task.getDate().compareTo(beginDate) >= 0
					&& task.getDate().compareTo(endDate) <= 0) {
				matches.add(task);
			}
		}		
		return matches;
	}

	
}
