package com.presentation.searchUI;

import java.util.ArrayList;

import com.business.TUser;
import com.presentation.card.Card;
import com.presentation.commands.CommandEnum.Commands;
import com.presentation.commands.Pair;
import com.presentation.controller.Controller;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


public class SearchUI extends UI{
	
	private ArrayList<TUser> results;
	private GridLayout secondaryLayout;
	private VerticalLayout resultsLayout;

	@Override
	protected void init(VaadinRequest request) {
		//main layout
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();
			
		//main helper
		VerticalLayout mainVertical = new VerticalLayout();
		Panel panel = new Panel();
		panel.setSizeFull();
		panel.setContent(mainVertical);
		mainLayout.addComponent(panel);
		
		
		
		//navbar
		HorizontalLayout navarLayout = createNavBar();
		
		mainVertical.addComponent(navarLayout);
		//if that's false the navbar is on the top of the screen, else - there is a margin
		mainVertical.setMargin(false);
		//secondary layout for the 2 parts
		
		secondaryLayout = new GridLayout(2, 1);
		
		mainVertical.addComponent(secondaryLayout);
		
		
		this.resultsLayout = new VerticalLayout();
		this.resultsLayout.setSizeUndefined();
		
		//search options layout
		VerticalLayout searchOptionsLayout = new VerticalLayout();
		
		searchOptionsLayout.addComponent(this.createSearchOptions());
		searchOptionsLayout.setSizeUndefined();
		searchOptionsLayout.setMargin(false);
		
		final Styles styles = Page.getCurrent().getStyles();
		String css = ".layout-with-border {\n" 
											+ "    border: 3px solid #FF6F61;\n" 
											+ "    border-radius: 5px; \n"
											+ "}";
		styles.add(css);
		searchOptionsLayout.addStyleName("layout-with-border");
		
		secondaryLayout.addComponent(searchOptionsLayout);

		secondaryLayout.addComponent(resultsLayout);
		//resultsLayout.setSizeFull();
		
		
		this.setContent(mainLayout);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Panel createSearchOptions() {
		Panel optionsPanel = new Panel();
		optionsPanel.setHeight("100%");
		optionsPanel.setWidth("20%");
	
		//checkbox
		CheckBox hostCheckbox = new CheckBox("Host");
		hostCheckbox.setId("checkBoxHost");
		//Button Accept
		Button accept = new Button("Accept");
		accept.setId("acceptButton");
		accept.addClickListener(event->{
			SearchUI.this.secondaryLayout.removeComponent(1, 0);
			if(hostCheckbox.getValue()) {
				Pair<Integer, Object> filtered = Controller.getInstance().action(Commands.CommandSearchHost, null);
				if(filtered.getLeft() == 0) {
					Notification notif = new Notification( "There are no users matching your criteria.");
					notif.setDelayMsec(10000);
					notif.setPosition(Position.MIDDLE_CENTER);
					notif.show(Page.getCurrent());
				}
				else {
					
					SearchUI.this.results = new ArrayList<>();
					resultsLayout = createResultPanel(results);
					SearchUI.this.secondaryLayout.addComponent(resultsLayout);
					
					SearchUI.this.results = (ArrayList)filtered.getRight();
					resultsLayout = createResultPanel(results);
					SearchUI.this.secondaryLayout.addComponent(resultsLayout);
					
						//secondaryLayout.setWidth("50%");
//					else {
//						Notification notif = new Notification( "There are no users matching your criteria.");
//						notif.setDelayMsec(2000);
//						notif.setPosition(Position.MIDDLE_CENTER);
//						notif.show(Page.getCurrent());
//					}
				}
				
			}
		});
		accept.setVisible(true);
		
		//
		VerticalLayout v = new VerticalLayout();
		v.addComponent(hostCheckbox);
		v.addComponent(accept);
		
		optionsPanel.setContent(v);
		
		return optionsPanel;
	}

	private VerticalLayout createResultPanel(ArrayList<TUser> users) {
		VerticalLayout result = new VerticalLayout();
		result.setMargin(false);
		result.setSizeFull();
		result.removeAllComponents();
		int counter = 1;
		for(TUser u: users) {
			Card card = new Card(u.getNickname(), u.getDescription());
			card.setId("card" + counter++);
			result.addComponent(card);
			result.setComponentAlignment(card, Alignment.TOP_LEFT);
		}
		result.setHeight("100%");
		return result;
	}
	
	private HorizontalLayout createNavBar() {
		HorizontalLayout navBarLayout = new HorizontalLayout();
		navBarLayout.setWidth("100%");
		//navBarLayout.setMargin(false);
		
		final Styles styles = Page.getCurrent().getStyles();
		String css = ".valo .v-menubar {\n" 
											+ "    height: 50px;\n" 
											+ "    padding: 5px; \n"
											+ "    border: white; \n"
											+ "	   background-color: white; \n"
											+ "}";
		styles.add(css);
		
		MenuBar menu = new MenuBar();
		menu.setStyleName("valo .v-menubar");
		MenuItem profile = menu.addItem("My Profile");
		
		//created search working button
		MenuItem search = menu.addItem("Search");
		//this is the redirection of the pages.
		search.setCommand(new Command() {
			
			//this method redirects you to the page HostAbtoad/chosenLocation
			@Override
			public void menuSelected(MenuItem selectedItem) {
			SearchUI.this.getUI().getPage().setLocation("search");
			}
		});
		MenuItem logIn = menu.addItem("Log in/ Sign in");
		navBarLayout.addComponent(menu);
		navBarLayout.setComponentAlignment(menu, Alignment.TOP_LEFT);
		navBarLayout.setMargin(false);
		
		return navBarLayout;
	}
}