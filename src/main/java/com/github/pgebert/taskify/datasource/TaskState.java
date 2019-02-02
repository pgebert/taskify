package com.github.pgebert.taskify.datasource;

import com.vaadin.server.FontAwesome;

public enum TaskState {
	
	OPEN("Open", FontAwesome.SQUARE_O), DONE("Done", FontAwesome.CHECK_SQUARE_O);
	
	private String state;
	private FontAwesome icon;

	TaskState(String state, FontAwesome icon) {
		this.state = state;
		this.icon = icon;
	}
	
	public String getTaskState() {
		return this.state;
	}
	
	public static TaskState findByRouteState(String state){
	    for(TaskState s : values()){
	        if( s.getTaskState().equals(state)){
	            return s;
	        }
	    }
	    return null;
	}
		
	public FontAwesome getIcon() {
		return this.icon;
	}
	
	public static TaskState findByIcon(FontAwesome icon){
	    for(TaskState s : values()){
	        if(s.getIcon().equals(icon)){
	            return s;
	        }
	    }
	    return null;
	}

}
