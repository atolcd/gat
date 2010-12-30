 /**
 * ToolBar.java
 *
 * Object representation of the ToolBar
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20101025
 **/
 
package com.AtolCD.client.GAT.widgets;

import com.AtolCD.client.GAT.maps.MapManager;
import com.AtolCD.client.GAT.GATModule;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ToolBar{

	private ToggleButton ruButton, ddButton;
	private ToggleButton outilButton, selectButton;
	private Button filterButton;
	private HorizontalPanel hpanel;
	private MapManager mapRef;
	private GATModule instance;
	
	private ToggleButton selectedOne;
	
	public ToolBar(){}

	public ToolBar(GATModule instance){

		this.instance = instance;
		this.mapRef = instance.getMapWidget();

		this.hpanel =  new HorizontalPanel();

		filterButton = new Button("Filter");
		outilButton = new ToggleButton("Detail");
		selectButton = new ToggleButton("Selection");
		ruButton = new ToggleButton("Roll-Up");
		ddButton = new ToggleButton("Drill-Down");

		hpanel.add(outilButton);
		hpanel.add(ddButton);
		hpanel.add(ruButton);
		hpanel.add(selectButton);
		hpanel.add(filterButton);

		CursHandler mhandler = new CursHandler();
		DDHandler dhandler = new DDHandler();
		RUHandler rhandler = new RUHandler();
		selectHandler shandler = new selectHandler();
	    submitHandler suhandler = new submitHandler();
		
		outilButton.addClickHandler(mhandler);
		ddButton.addClickHandler(dhandler);
		ruButton.addClickHandler(rhandler);
		selectButton.addClickHandler(shandler);
		filterButton.addClickHandler(suhandler);
	}
	
	public HorizontalPanel getToolbar(){
		return this.hpanel;
	}

	private void resetTool(){
		if(selectedOne!=null){
			selectedOne.setDown(false);
		}
	}
	//Inner classes
 	class CursHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			resetTool();
			selectedOne = outilButton;
			mapRef.setHover();
			mapRef.setOlapTool(-1);
      }
	}

	class DDHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			resetTool();
			selectedOne = ddButton;
			mapRef.setSelec();
			mapRef.setOlapTool(0);
      }
	}

	class RUHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			resetTool();
			selectedOne = ruButton;
			mapRef.setSelec();
			mapRef.setOlapTool(1);
      }
	}

	class selectHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			resetTool();
			selectedOne = selectButton;
			mapRef.setSelec();
			mapRef.setOlapTool(-1);
      }
	}

	class submitHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			instance.execute();
      }
	}
}

