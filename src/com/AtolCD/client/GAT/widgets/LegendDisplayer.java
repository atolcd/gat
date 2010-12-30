 /**
 * LegendDisplayer.java
 *
 * Legend displayer widget
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 **/
 
package com.AtolCD.client.GAT.widgets;

//GWT imports
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;

//Openlayers
import org.gwtopenmaps.openlayers.client.event.EventObject;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

//Internal imports
import com.AtolCD.client.GAT.olap.CellSetAxis;
import com.AtolCD.client.GAT.olap.CellSet;
import com.AtolCD.client.GAT.maps.MapManager;
import com.AtolCD.client.GAT.utils.Observer;

public class LegendDisplayer implements Observer{

	//UI element to return
	private FlowPanel fpanel;
	private HTML flabel;
	private HTML label;
	private ListBox methodList;
	
	//Map Manager reference
	private MapManager mapRef;
	private ListBox lb;
	
	//Constructor
	public LegendDisplayer(){
		this.fpanel = new FlowPanel();
		this.methodList = new ListBox();

		lb = new ListBox();
		lb.setVisibleItemCount(1);
		lb.addItem("Linear");
		lb.addItem("Non-Linear");
		lb.addItem("Logarithmic");
		this.flabel = new HTML("<b>Legende</b><br>");
		this.label = new HTML("");
		this.fpanel.add(this.flabel);
		this.fpanel.add(lb);
		this.fpanel.add(this.label);
		lb.addChangeListener(new ChangeListener() {

			@Override
			public void onChange(Widget sender) {
				ListBox box = (ListBox)sender;
				int value = box.getSelectedIndex();
				mapRef.getLegendizer().setMethod(value);
				mapRef.maj();
			}
		});
	}
	
	public LegendDisplayer(MapManager map){
		this();
		mapRef = map;
		
	}
	
	@Override
	public void update(){

		int stepnb = mapRef.getLegendizer().getStepNb();
		double[] vls = mapRef.getLegendizer().getVales();

		
		String legend = "<table>";

		for(int i=0;i<stepnb;i++){
			String fir = (vls[i] <= 0)?(""+0):(""+Math.ceil(vls[i]));
			String sec = (i+1<stepnb)?(" - "+(Math.ceil(vls[i+1]))):" et +";
			legend += "<tr><td><div style=\"width:30px;height:20px;border: 1px black solid;margin:5px;background-color:";
			legend += mapRef.getLegendizer().getColor((int)(vls[i]+1));
			legend += "\"></div></td><td>";
			legend += fir+sec;
			legend += "</td></tr>";
		}		
		legend+="</table>";
		this.label.setHTML(legend);
	}


	@Override
	public void update(Object o){

	}

	public FlowPanel getDataPanel(){
		return this.fpanel;
	}
}