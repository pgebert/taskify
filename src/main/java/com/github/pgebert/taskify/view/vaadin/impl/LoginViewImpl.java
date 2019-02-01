package com.github.pgebert.taskify.view.vaadin.impl;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.events.LoginEvent;
import com.github.pgebert.taskify.view.api.LoginView;
import com.github.pgebert.taskify.view.vaadin.helper.LoginUser;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.extern.java.Log;

@Log
public class LoginViewImpl extends VerticalLayout implements LoginView, View {

	private static final long serialVersionUID = 5720657319198329569L;
	
	private EventBus eventBus;

	@Inject
	public LoginViewImpl(EventBus eventBus) {
		this.eventBus = eventBus;
		
		setSizeFull();
        setMargin(false);
        setSpacing(false);
        addStyleName("loginview");

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);		
        
        // TODO: Disable in production
        showNotification();
	}
	
    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
//        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
//        loginPanel.addComponent(new CheckBox("Remember me", true));
        return loginPanel;
    }
    
    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.addStyleName("fields");

        final TextField username = new TextField("Username");
        username.setValue("Noah Smith");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Password");
        password.setValue("password");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 8927868740206541820L;

			@Override
            public void buttonClick(final ClickEvent event) {
            	
            	LoginUser user = new LoginUser(username.getValue(), DigestUtils.sha256Hex(password.getValue()));
            	eventBus.post(new LoginEvent(user));
            }
        });
        return fields;
    }
    
    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label(FontAwesome.DATABASE.getHtml() + "&nbsp;&nbsp;Taskify", ContentMode.HTML);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }
    
    private void showNotification() {
    	Notification notification = new Notification(
                "Welcome to Taskify");
        notification
                .setDescription("<span>This is a demo ToDo list application build with vaadin.</span> <span>Login with 'Noah Smith' and 'password' to continue.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(30000);
        notification.show(Page.getCurrent());
    }

	@Override
	public void enter(ViewChangeEvent event) {
		((BaseUI) UI.getCurrent()).getAccessControl().logout();
		log.info("User logged out!");
	}

}
