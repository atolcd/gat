 /**
 * CubeChooser.java
 *
 * Cube Chooser Widget
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100927
 **/
 
package com.AtolCD.client.GAT.widgets;

//extern imports
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;

//interne imports
import com.AtolCD.client.GAT.olap.CellSet;
import com.AtolCD.client.GAT.olap.CellSetAxis;
import com.AtolCD.client.GAT.maps.MapManager;

import com.AtolCD.client.GAT.utils.Observable;
import com.AtolCD.client.GAT.utils.Observer;
import com.AtolCD.client.GAT.utils.Config;
import com.AtolCD.client.GAT.olap.OlapAjax;

public class CubeChooser{
	
	private ListBox lb;
	private Config cf;
	private Observable cubeObs;
	
	public CubeChooser(){
		lb = new ListBox();
		lb.setVisibleItemCount(1);
	}
	
	public void init(Config conf){

		cubeObs = new Observable();
		
		this.cf = conf;
		int nb_cube = cf.getOlapConfig().getCubesCount();
		
		for(int i=0;i<nb_cube;i++){
			String name = cf.getOlapConfig().getCube(i).getName();
			lb.addItem(name);
		}
	
		lb.addChangeListener(new ChangeListener() {

			@Override
			public void onChange(Widget sender) {
				ListBox box = (ListBox)sender;
				int value = box.getSelectedIndex();
				//regen query
				cubeObs.update();
				String name = cf.getOlapConfig().getCube(value).getName();
			}
		});
	
	}
	
	public ListBox getList(){
		return this.lb;
	}
	
	public void addObserver(Observer o){
		cubeObs.addObserver(o);
	}
	
	public int getSelectedIndex(){
		return this.lb.getSelectedIndex();
	}
}