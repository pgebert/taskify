package com.github.pgebert.taskify.view.vaadin.component;


import org.apache.commons.codec.digest.DigestUtils;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.datasource.AccessRights;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.datasource.User.Sex;
import com.github.pgebert.taskify.events.UserEvents.UserRemovedEvent;
import com.github.pgebert.taskify.events.UserEvents.UserSavedEvent;
import com.github.pgebert.taskify.view.vaadin.helper.AccessControl;
import com.google.common.eventbus.EventBus;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class UserDetailWindow extends Window {

	private static final long serialVersionUID = -6151147481596115592L;
	
	private long itemId;

	public UserDetailWindow(EventBus eventBus) {
		super("Add new user");
		center();
		setModal(true);

		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		setContent(content);
		
		User user = new User(-1L, "", Sex.MALE, "", "", "", "", AccessRights.USER);
		BeanItem<User> item = new BeanItem<User>(user);
		content.addComponent(new UserForm(item, false, eventBus));
	}
	
	public UserDetailWindow(User user, long itemId, EventBus eventBus) {		
		super(user.getName());
		center();
		setModal(true);
		this.itemId = itemId;

		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		setContent(content);

		BeanItem<User> item = new BeanItem<User>(user);		
		content.addComponent(new UserForm(item, true, eventBus));
	}
	
	private class UserForm extends CustomComponent {

		private static final long serialVersionUID = 4005767512857836946L;
		
		@PropertyId("name")
		TextField name  = new TextField("Name");
		
		@PropertyId("password")
		PasswordField password  = new PasswordField("Password");
		
		@PropertyId("sex")
		NativeSelect sex = new NativeSelect("Sex");
		
		@PropertyId("address")
		TextField address = new TextField("Address");		
		
		@PropertyId("phone")
		TextField phone  = new TextField("Phone");		
		
		@PropertyId("mail")
		TextField mail  = new TextField("Mail");
		
		@PropertyId("rights")
		NativeSelect accessRights = new NativeSelect("Access rights");	
		
		
		public UserForm(final BeanItem<User> item, boolean updateMode, final EventBus eventBus) {
			
			AccessControl control = ((BaseUI) UI.getCurrent()).getAccessControl();
			
			for (User.Sex s : User.Sex.values()) {
				sex.addItem(s);
				sex.setItemCaption(s, s.name());
			}
			sex.setNullSelectionAllowed(false);
			
			for (AccessRights r : AccessRights.values()) {
				accessRights.addItem(r);
				accessRights.setItemCaption(r, r.name());
			}
			accessRights.setNullSelectionAllowed(false);
			
			FormLayout layout = new FormLayout();
			name.setRequired(true);
			layout.addComponent(name);
			
			if (!updateMode) {
				password.setRequired(true);
			}	
			
			if (updateMode && !AccessRights.hasRight(control.getUser(), AccessRights.ADMIN)) {
				accessRights.setVisible(false);
			}
			
			layout.addComponent(password);
			layout.addComponent(sex);
			layout.addComponent(address);
			layout.addComponent(phone);
			layout.addComponent(mail);			
			layout.addComponent(accessRights);

			final FieldGroup binder = new FieldGroup(item);
			binder.bindMemberFields(this);
			
			HorizontalLayout bottomToolbox = new HorizontalLayout();			
			
			Button saveBtn = new Button("Save");
			saveBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			saveBtn.addClickListener(event -> {
				User bean = item.getBean();
				
				try {							
					// encrypt password
					if (!updateMode) {
						password.setValue(DigestUtils.sha256Hex(password.getValue()));
					}	else {
						// password changed
						if (password.isModified()) {
							password.setValue(DigestUtils.sha256Hex(password.getValue()));
						}
					}
					
					binder.commit();
					
					eventBus.post(new UserSavedEvent(bean));
				} catch (CommitException e) {
					// do nothing, eventually show error message
				}
				close();
			});
			bottomToolbox.addComponent(saveBtn);
			
			// do not delete current user
			if (updateMode && !control.getUser().equals(item.getBean())) {
				Button removeBtn = new Button("Delete");
				removeBtn.addStyleName(ValoTheme.BUTTON_DANGER);
				removeBtn.addClickListener(event -> {
					User bean = item.getBean();
					
					try {
						binder.commit();
						eventBus.post(new UserRemovedEvent(bean, itemId));
					} catch (CommitException e) {
						// do nothing, eventually show error message
					}
					close();
				});
				bottomToolbox.addComponent(removeBtn);
			}			
			
			layout.addComponent(bottomToolbox);
			setCompositionRoot(layout);
		}
	}

}