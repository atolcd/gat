 /**
 * DataDisplayer.java
 *
 * Data displayer widget
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

//Openlayers
import org.gwtopenmaps.openlayers.client.event.EventObject;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

//Internal imports
import com.AtolCD.client.GAT.olap.CellSetAxis;
import com.AtolCD.client.GAT.olap.CellSet;
import com.AtolCD.client.GAT.maps.MapManager;
import com.AtolCD.client.GAT.utils.Observer;

public class DataDisplayer implements Observer{
	
	//UI element to return
	private FlowPanel fpanel;
	private HTML label;
	
	//Map Manager reference
	private MapManager mapRef;
	
	//Constructor
	public DataDisplayer(){
		this.fpanel = new FlowPanel();
		this.label = new HTML("<b>Informations</b>\r\n");
		this.fpanel.add(this.label);
	}
	
	public DataDisplayer(MapManager map){
		this();
		mapRef = map;
	}
	
	public void init(){
	
	}

	@Override
	public void update(){
			this.label.setText("updated without any args...");
	}


	@Override
	public void update(Object o){

		//Object o is a feature
		EventObject tmp_mp = (EventObject)o;

		int axisOrd = tmp_mp.getJSObject().getProperty("feature").getProperty("attributes").getPropertyAsInt("axisOrdinal");
		int ordPos = tmp_mp.getJSObject().getProperty("feature").getProperty("attributes").getPropertyAsInt("positionOrdinal");

		System.out.println("Axes :  "+axisOrd+" , "+ordPos);
		
		//CellSet a = getCellSet();
		CellSet a = mapRef.getCS();
		
		String g = "<p><b> Informations </b><br><br>"+a.getAxis(axisOrd).getMemberName(0,ordPos)+ "  <br><br>";
		
		int axesNb = a.getAxis(0).getMemberCount(0);
		
		for(int i=0;i<axesNb;i++){

			int ords[] = {i,ordPos};
			String nm = a.getAxis(0).getMemberName(0,i);
			double val = Double.parseDouble(a.getCell(ords));
			g += nm+" : "+val+" <br>";
		}
		
		g += "</p>";
		
		this.label.setHTML(g);
	}
	
	//get the cellset -> $wmd.cs 
	public native CellSet getCellSet() /*-{ return $wnd.cs[0] }-*/;

	public com.google.gwt.user.client.ui.FlowPanel getDataPanel(){
		return this.fpanel;
	}
}