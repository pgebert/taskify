package com.github.pgebert.taskify.injection;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.pgebert.taskify.datasource.TaskDataDummy;
import com.github.pgebert.taskify.datasource.TaskDataFacade;
import com.github.pgebert.taskify.datasource.UserDataDummy;
import com.github.pgebert.taskify.datasource.UserDataFacade;
import com.github.pgebert.taskify.view.api.CalendarView;
import com.github.pgebert.taskify.view.api.DashboardView;
import com.github.pgebert.taskify.view.api.LoginView;
import com.github.pgebert.taskify.view.api.MenuView;
import com.github.pgebert.taskify.view.api.TaskView;
import com.github.pgebert.taskify.view.api.UsersView;
import com.github.pgebert.taskify.view.vaadin.component.MenuViewComponent;
import com.github.pgebert.taskify.view.vaadin.impl.CalendarViewImpl;
import com.github.pgebert.taskify.view.vaadin.impl.DashboardViewImpl;
import com.github.pgebert.taskify.view.vaadin.impl.LoginViewImpl;
import com.github.pgebert.taskify.view.vaadin.impl.TaskViewImpl;
import com.github.pgebert.taskify.view.vaadin.impl.UsersViewImpl;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.google.inject.AbstractModule;

public class InjectionModule extends AbstractModule {

	private final EventBus eventBus = new EventBus(new PrintStackTraceExceptionHandler("EventBus"));
	private final MenuViewComponent menuViewComponent = new MenuViewComponent(eventBus);

	@Override
	protected void configure() {
		
		// data dummies
		bind(TaskDataFacade.class).to(TaskDataDummy.class);
		bind(UserDataFacade.class).to(UserDataDummy.class);		
		// other
		bind(DashboardView.class).to(DashboardViewImpl.class);
		bind(TaskView.class).to(TaskViewImpl.class);
		bind(UsersView.class).to(UsersViewImpl.class);		
		bind(LoginView.class).to(LoginViewImpl.class);		
		bind(CalendarView.class).to(CalendarViewImpl.class);
		bind(MenuView.class).toInstance(menuViewComponent);
		// event bus
		bind(EventBus.class).toInstance(eventBus);
	}

	/**
	 * Simple logging handler for subscriber exceptions. Copied from EventBus -
	 * prints StackTrace on exception
	 */
	private static final class PrintStackTraceExceptionHandler implements SubscriberExceptionHandler {

		private final Logger logger;
		public PrintStackTraceExceptionHandler(String identifier) {
			logger = Logger.getLogger(EventBus.class.getName() + "." + checkNotNull(identifier));
		}

		@Override
		public void handleException(Throwable exception, SubscriberExceptionContext context) {
			logger.log(Level.SEVERE,
					"Could not dispatch event: " + context.getSubscriber() + " to " + context.getSubscriberMethod(),
					exception.getCause());
			logger.log(Level.SEVERE, "Exception:", exception);
		}
	}

}
