package com.github.pgebert.taskify.view.vaadin.component;

import com.github.pgebert.taskify.BaseUI;
import com.github.pgebert.taskify.datasource.AccessRights;
import com.github.pgebert.taskify.datasource.User;
import com.github.pgebert.taskify.events.LoginSuccessEvent;
import com.github.pgebert.taskify.events.ViewEvents.PostViewChangeEvent;
import com.github.pgebert.taskify.view.api.MenuView;
import com.github.pgebert.taskify.view.api.UsersView;
import com.github.pgebert.taskify.view.vaadin.helper.AccessControl;
import com.github.pgebert.taskify.view.vaadin.helper.ViewType;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import lombok.extern.java.Log;

@Log
public class MenuViewComponent extends CustomComponent implements MenuView {

	private static final long serialVersionUID = 2818220141543350798L;
	
	private static final String AVATAR_MALE = "img/avatar_male_0.png";
	private static final String AVATAR_FEMALE = "img/avatar_female_0.png";
	private static final String AVATAR_UNKNOWN = "img/profile-pic-300px.jpg";
	
	private EventBus eventBus;
	
	// User picture and information
	private MenuItem settingsItem;
	private static final String STYLE_VISIBLE = "valo-menu-visible";

	@Inject
	public MenuViewComponent(final EventBus eventBus) {	
		
		setPrimaryStyleName("valo-menu");
		setSizeUndefined();
		
		this.eventBus = eventBus;
		eventBus.register(this);
		
		setCompositionRoot(buildContent());
	}
	
	private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());  
        
        return menuContent;
    }
	
    private Component buildTitle() {
        Label logo = new Label(FontAwesome.DATABASE.getHtml() + "&nbsp;&nbsp;&nbsp;Taskify", ContentMode.HTML);
        logo.setSizeFull();;
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("menu-title");
        logoWrapper.setSpacing(false);
        return logoWrapper;
    }
    
    private User getCurrentUser() {
    	AccessControl control = ((BaseUI) UI.getCurrent()).getAccessControl();
    	return control.getUser();
    }
    
	private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        settingsItem = settings.addItem("", new ThemeResource(AVATAR_UNKNOWN), null);
        settingsItem.addItem("Edit Profile", new Command() {       	
        	
            @Override
            public void menuSelected(final MenuItem selectedItem) {
            	
    			UsersView usersView = ((BaseUI) UI.getCurrent()).getInjector().getInstance(UsersView.class);        			
            	usersView.showDetailWindow(getCurrentUser(), getCurrentUser().getId());
                			
            }
        });
//        settingsItem.addItem("Preferences", new Command() {
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                ProfilePreferencesWindow.open(user, true);
//            }
//        });
//        settingsItem.addSeparator();
//        settingsItem.addItem("Sign Out", new Command() {
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                DashboardEventBus.post(new UserLoggedOutEvent());
//            }
//        });
        return settings;
    }
    
    private Component buildMenuItems() {
    	
    	Styles styles = Page.getCurrent().getStyles();
        // inject css to disable flickering transitions
    	// TODO: export to css
        styles.add(".mytheme .valo-menu-item { "
        		+ "-webkit-transition: none;"
        		+ "-moz-transition: none;"
        		+ "transition: none;"
        		+ "}");
    	
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
        menuItemsLayout.setWidth(95, Unit.PERCENTAGE);
        for (AccessRights right : AccessRights.values()) {
        	
        	menuItemsLayout.addComponent(new ValoMenuItemLabel(right));
        	
        	for (final ViewType view : ViewType.stream().filter(d -> d.getAccessRight().equals(right)).toArray(ViewType[]::new)) {
                Component menuItemComponent = new ValoMenuItemButton(view);
                menuItemComponent.setPrimaryStyleName("valo-menu-item");
                menuItemComponent.addStyleName(ValoTheme.MENU_ITEM);
                menuItemsLayout.addComponent(menuItemComponent);
            }
        }
        
        return menuItemsLayout;

    }
    
    @Subscribe
    public void postViewChange(final PostViewChangeEvent event) {
        // After a successful view change the menu can be hidden in mobile view.
        getCompositionRoot().removeStyleName(STYLE_VISIBLE);
    }
    
    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -4406581715547595406L;

			@Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName()
                        .contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }



//    @Override
//    public void attach() {
//        super.attach();
////        updateNotificationsCount(null);
//        log.info("Attached");
//    }
    
    public class ValoMenuItemLabel extends Label {
	
		private static final long serialVersionUID = 1L;

        private final AccessRights right;

        public ValoMenuItemLabel(final AccessRights right) {
            this.right = right;
            addStyleName(ValoTheme.LABEL_LARGE);             
                        
            setCaption("&nbsp;&nbsp;" + right.toString());        
            setCaptionAsHtml(true);
            eventBus.register(this);
           
        }

        @Subscribe
        public void postViewChange(final PostViewChangeEvent event) {         			
           setVisible(AccessRights.hasRight(getCurrentUser(), right));
        }        
       
    }
    
    public class ValoMenuItemButton extends Button {

        /**
		 * 
		 */    	
		private static final long serialVersionUID = 1L;

		private static final String STYLE_SELECTED = "selected";

        private final ViewType view;

        public ValoMenuItemButton(final ViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");    
            
            setIcon(view.getIcon());
            setCaption(view.getViewName());
            eventBus.register(this);
            addClickListener(new ClickListener() {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().getNavigator().navigateTo(view.getViewLink());
                }
            });     

        }

        @Subscribe
        public void postViewChange(final PostViewChangeEvent event) {
            removeStyleName(STYLE_SELECTED);
            setVisible(false);
            if (event.getViewType() == view) {
                addStyleName(STYLE_SELECTED);
            }
            User user = getCurrentUser();
           setVisible(AccessRights.hasRight(user, view.getAccessRight()));
        }        
       
    }
    
	
	/**
	 * Updates the menu bar e.g. to adjust component visibility for changed access rights
	 */
	@Subscribe
	public void update(LoginSuccessEvent event) {
		AccessControl control = ((BaseUI) UI.getCurrent()).getAccessControl();
		User user = control.getUser();	
		
//		TODO: Initialize button and labels and uncomment the bellow  
//		adminLabel.setVisible(control.hasAccessRights(AccessRights.ADMIN));
//		usersViewButton.setVisible(control.hasAccessRights(AccessRights.ADMIN));	
		
//		// Update user info in top panel 	
		settingsItem.setText(user.toString());	
		if (user.getSex().equals(User.Sex.MALE)) {
			settingsItem.setIcon(new ThemeResource(AVATAR_MALE));
		} else if (user.getSex().equals(User.Sex.FEMALE)) {
			settingsItem.setIcon(new ThemeResource(AVATAR_FEMALE));
		} else {
			settingsItem.setIcon(new ThemeResource(AVATAR_UNKNOWN));
		}
		
	}
	
}
