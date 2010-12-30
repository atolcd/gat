 /**
 * HierarchyChooser.java
 *
 * Hierarchy Chooser Widget
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
import com.AtolCD.client.GAT.utils.Observable;
import com.AtolCD.client.GAT.utils.Observer;
import com.AtolCD.client.GAT.utils.Config;

public class HierarchyChooser{
	
	private ListBox lb;
	private Config cf;
	private Observable hierObs;
	
	public HierarchyChooser(){
		lb = new ListBox();
		lb.setVisibleItemCount(1);
	}
	
	public void init(Config conf){
	
		
		hierObs = new Observable();
		
		this.cf = conf;
		int geomInd = Integer.parseInt(cf.getOlapConfig().getGeomIndex());
		int nb_hier = cf.getCurrentCube().getDimension(geomInd).getHierarchiesCount();
		
		for(int i=0;i<nb_hier;i++){
			String name = cf.getCurrentCube().getDimension(geomInd).getHierarchy(i).getName();
			lb.addItem(name);
		}
	
		lb.addChangeListener(new ChangeListener() {

			@Override
			public void onChange(Widget sender) {
				ListBox box = (ListBox)sender;
				int value = box.getSelectedIndex();
				hierObs.update();
			}
		});
	
	}
	
	public ListBox getList(){
		return this.lb;
	}
	
	public void addObserver(Observer o){
		hierObs.addObserver(o);
	}
	
	public int getSelectedIndex(){
		return this.lb.getSelectedIndex();
	}
	
}