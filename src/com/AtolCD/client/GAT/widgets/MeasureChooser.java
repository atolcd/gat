 /**
 * MeasureChooser.java
 *
 * Measure Chooser Widget
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
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

import com.AtolCD.client.GAT.utils.Observer;
import com.AtolCD.client.GAT.olap.OlapAjax;

import com.AtolCD.client.GAT.utils.BlindedPopup;

public class MeasureChooser implements Observer{
	
	private ListBox lb;
	private MapManager tp;

	public MeasureChooser(MapManager mp){
		lb = new ListBox();
		lb.setVisibleItemCount(1);
		this.tp = mp;
	}
	
	public void init(CellSetAxis m_axis){
	
		//parse axis
		int u = m_axis.getMemberCount(0);
		
		for(int k = 0;k<u;k++){
			lb.addItem(m_axis.getMemberName(0,k));
		}

        lb.addChangeListener(new ChangeListener() {

			@Override
			public void onChange(Widget sender) {
				
				ListBox box = (ListBox)sender;
				int value = box.getSelectedIndex();
				tp.setSlicer(value);

			}
		});
		
	}

	//Final listbox getter
	public ListBox getList(){
		return this.lb;
	}
	
	@Override
	public void update(){}
	
	@Override
	public void update(Object o){
		
		OlapAjax oa = (OlapAjax)o;
		lb.clear();
		CellSet a = oa.getCellSet();
		init(a.getAxis(0));
		
	}
	
}