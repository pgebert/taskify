package com.github.pgebert.taskify.view.vaadin.helper;

import java.util.stream.Stream;

import com.github.pgebert.taskify.datasource.AccessRights;
import com.github.pgebert.taskify.view.vaadin.impl.CalendarViewImpl;
import com.github.pgebert.taskify.view.vaadin.impl.DashboardViewImpl;
import com.github.pgebert.taskify.view.vaadin.impl.LoginViewImpl;
import com.github.pgebert.taskify.view.vaadin.impl.TaskViewImpl;
import com.github.pgebert.taskify.view.vaadin.impl.UsersViewImpl;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum ViewType {
	
	DASHBOARD("Dashboard", "dashboard", DashboardViewImpl.class, FontAwesome.HOME, AccessRights.USER),
	TASKS("Tasks", "tasks", TaskViewImpl.class, FontAwesome.PAPER_PLANE, AccessRights.USER),
	CALENDAR("Calendar", "calendar", CalendarViewImpl.class, FontAwesome.CALENDAR, AccessRights.USER),	
	LOGIN("Logout", "", LoginViewImpl.class, FontAwesome.SIGN_OUT, AccessRights.USER),	
	USERS("Users", "users", UsersViewImpl.class, FontAwesome.USERS, AccessRights.ADMIN);
		
    private final String viewName;
    private final String viewLink;
    private final Class<? extends View> viewClass;
    private final Resource icon;
	private final AccessRights accessRight;

	
	private ViewType(final String viewName, final String viewLink,
            final Class<? extends View> viewClass, final Resource icon,
            final AccessRights accessRight) {
        this.viewName = viewName;
        this.viewLink = viewLink;
        this.viewClass = viewClass;
        this.icon = icon;
        this.accessRight = accessRight;
	}

    public String getViewName() {
        return viewName;
    }
    
    public String getViewLink() {
        return viewLink;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }
    
    public AccessRights getAccessRight() {
        return accessRight;
    }

    public static ViewType getByViewName(final String viewName) {
        ViewType result = null;
        for (ViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }
    
    public static ViewType getByViewLink(final String viewLink) {
        ViewType result = null;
        for (ViewType viewType : values()) {
            if (viewType.getViewLink().equals(viewLink)) {
                result = viewType;
                break;
            }
        }
        return result;
    }
    
    public static Stream<ViewType> stream() {
        return Stream.of(ViewType.values()); 
    }

	
}
