package com.github.pgebert.taskify;

import javax.servlet.annotation.WebServlet;

import com.github.pgebert.taskify.datasource.Task;
import com.github.pgebert.taskify.events.ViewEvents.PostViewChangeEvent;
import com.github.pgebert.taskify.injection.InjectionModule;
import com.github.pgebert.taskify.presenter.CalendarPresenter;
import com.github.pgebert.taskify.presenter.DashboardPresenter;
import com.github.pgebert.taskify.presenter.LoginPresenter;
import com.github.pgebert.taskify.presenter.MainPresenter;
import com.github.pgebert.taskify.presenter.TaskPresenter;
import com.github.pgebert.taskify.presenter.UserPresenter;
import com.github.pgebert.taskify.view.vaadin.helper.AccessControl;
import com.github.pgebert.taskify.view.vaadin.helper.ViewType;
import com.github.pgebert.taskify.view.vaadin.impl.LoginViewImpl;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.Getter;
import lombok.Setter;

@Theme("mytheme")
@JavaScript({"vaadin://js/jquery-1.7.1.min.js", "vaadin://js/highcharts.js", "vaadin://js/highcharts-more.js", "vaadin://js/highcharts-connector.js"})
@SuppressWarnings("serial")
@PreserveOnRefresh
@Getter
@Setter
public class BaseUI extends UI {

	Navigator navigator;	
	AccessControl accessControl;

	private EventBus eventBus;
	private Injector injector;

	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = BaseUI.class, widgetset = "com.github.pgebert.taskify.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Taskify");
		Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

		navigator = new Navigator(this, this);
		accessControl = new AccessControl();

		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		
		InjectionModule module = new InjectionModule();
		this.injector = Guice.createInjector(module);
		this.eventBus = injector.getInstance(EventBus.class);
		
		// register presenter 
		injector.getInstance(LoginPresenter.class);
		injector.getInstance(MainPresenter.class);
		injector.getInstance(DashboardPresenter.class);
		injector.getInstance(UserPresenter.class);
		injector.getInstance(TaskPresenter.class);
		injector.getInstance(CalendarPresenter.class);
		// register other classes
		injector.getInstance(Task.class);	
		
		
				
		for (final ViewType viewType : ViewType.values()) {
			navigator.addView(viewType.getViewLink(), injector.getInstance(viewType.getViewClass()));
		}

		navigator.addViewChangeListener(new ViewChangeListener() {
			
			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {				
				if (!LoginViewImpl.class.isInstance(event.getNewView()) && !accessControl.isLoggedIn()) {
					// Show the login when the user is not logged in
					navigator.navigateTo(ViewType.LOGIN.getViewLink());
					return false;
				} 				
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {				
				ViewType viewType = ViewType.getByViewLink(event.getViewName());
				View view = event.getNewView();
                // Appropriate events get fired after the view is changed.
                 eventBus.post(new PostViewChangeEvent(viewType, view));

			}
		});
	}

}
