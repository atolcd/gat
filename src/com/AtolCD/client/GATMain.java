 /**
 * GATMain.java
 *
 * Main App Class
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 **/
 
package com.AtolCD.client;

//Imports gwt
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabPanel;

import com.google.gson.*;

import com.AtolCD.client.GAT.GATModule ;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
 
public class GATMain implements EntryPoint{

	private GATModule  mainGat;
	private TabPanel tp;
	private VerticalPanel stc, stc2, stc3;

	/**
	* Point d'entrée du module
	*/
	public void onModuleLoad() {

		mainGat = new GATModule();
		tp = new TabPanel();
		DockPanel dockPanel = new DockPanel();
		
		// Ajout de la Map 
		dockPanel.add(mainGat.getMap(), DockPanel.CENTER);
		dockPanel.setBorderWidth(1);
		TabPanel panel = new TabPanel();
		FlowPanel flowpanel;
		flowpanel = new FlowPanel();
		panel.add(flowpanel, "Map");
		flowpanel.add(dockPanel);
		flowpanel = new FlowPanel();
		flowpanel.add(mainGat.getFrame());
		panel.add(flowpanel, "Details");
		panel.selectTab(0);
		panel.setSize("800px","600px");
		panel.addStyleName("table-center");
		RootPanel.get("demo").add(panel);
		
		// Ajout de la toolbar
		dockPanel.add(mainGat.getToolBar(), DockPanel.NORTH);
		VerticalPanel loadingPanel = new VerticalPanel();
		stc =  new VerticalPanel();
		stc.add(new HTML("<b>Mesure</b><br>"));
		stc.add(mainGat.getMeasureChooser());
		stc.add(mainGat.getDataDisplayer());
		stc.add(mainGat.getLegendDisplayer());
		tp.add(stc,"Infos");
		tp.selectTab(0);
		
		// Ajout des panels
		dockPanel.add(tp, DockPanel.EAST);
		
		// Panel de droite
		stc3 =  new VerticalPanel();
		stc3.add(new HTML("<b>Cube</b><br><br>"));
		stc3.add(mainGat.getCubeChooser());
		stc3.add(new HTML(" <br><b>Hierarchie Geometrique</b><br><br>"));
		stc3.add(mainGat.getHierarchyChooser());
		System.out.println("Response update");
		stc2 =  new VerticalPanel();
		stc2.add(new HTML("<b>Abre</b><br>"));
		stc2.add(mainGat.getFilterTree());
		tp.add(stc2,"Arbre");
		tp.add(stc3,"Config");
  }
}
