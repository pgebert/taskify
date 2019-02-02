package com.github.pgebert.taskify.view.vaadin.impl;

import com.github.pgebert.taskify.events.ViewEvents.EnterViewEvent;
import com.github.pgebert.taskify.view.api.DashboardView;
import com.github.pgebert.taskify.view.vaadin.component.MenuViewComponent;
import com.github.pgebert.taskify.view.vaadin.helper.HighChart;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import lombok.extern.java.Log;


//@JavaScript({"jquery-min.js", "highcharts.js", "highcharts-connector.js"})
@Log
@Singleton
public class DashboardViewImpl extends HorizontalLayout implements DashboardView, View {

	private static final long serialVersionUID = 5720657319198329845L;
	
	private HighChart barChart;
	private HighChart donutChart;
	private HighChart splineChart;
	private EventBus eventBus;


	@Inject
	public DashboardViewImpl(MenuViewComponent menuView, final EventBus eventBus) {
		
		this.eventBus = eventBus;
		
		setSizeFull();		
		
		VerticalLayout rightSide = new VerticalLayout();
		rightSide.setSizeFull();
		rightSide.setStyleName("scrollable");
		
		Label title = new Label("Dashboard");
		title.addStyleName("h1");
		title.setSizeFull();
		title.setWidth(95, Unit.PERCENTAGE);			
		
		rightSide.addComponent(title);
		rightSide.setExpandRatio(title, 1f);
		rightSide.setComponentAlignment(title, Alignment.TOP_LEFT);	
		
		addComponent(menuView);
		setExpandRatio(menuView, 1f);
		
		addComponent(rightSide);
		setExpandRatio(rightSide, 6f);
		
//		rightSide.addComponent(buildChart());
		
		Layout customLayout = new CssLayout();
		customLayout.setSizeFull();
		
		this.barChart = new HighChart();
		this.barChart.setHeight("48%");
		this.barChart.setWidth(48.0f, Unit.PERCENTAGE);
		this.barChart.addStyleName("layout-with-border");
		customLayout.addComponent(this.barChart);
		
		this.donutChart = new HighChart();
		this.donutChart.setHeight("48%");
		this.donutChart.setWidth(48.0f, Unit.PERCENTAGE);
		this.donutChart.addStyleName("layout-with-border");
		customLayout.addComponent(this.donutChart);
				
		this.splineChart = new HighChart();
		this.splineChart.setHeight("48%");
		this.splineChart.setWidth(96.0f, Unit.PERCENTAGE);
		this.splineChart.addStyleName("layout-with-border");
		customLayout.addComponent(this.splineChart);
		
		rightSide.addComponent(customLayout);
		rightSide.setExpandRatio(customLayout, 6f);
		
	}
		
	@Override
	public void updateSplineChart(String options) {
		this.splineChart.setHcjs(options);
	}
	
	@Override
	public void updateDonutChart(String options) {
		this.donutChart.setHcjs(options);
	}
	
	
	@Override
	public void updateBarChart(String options) {
		this.barChart.setHcjs(options);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		this.eventBus.post(new EnterViewEvent());
	}

}
