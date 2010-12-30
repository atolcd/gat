 /**
 * GeomAxis.java
 *
 * Class representing geometric axis from the CellSet
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 **/

package com.AtolCD.client.GAT.olap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;

public class GeomAxis extends CellSetAxis{

	protected GeomAxis(){
	}
		
	/**
	* Direct Javascript accessor for intern use
	*
	* @param index int representing wanted geomtry's index
	*
	* @return JavaScriptObject which is a javascript representation of the geometry
	**/
	private final native JavaScriptObject getGeo(int index) /*-{ 
		return this.members[0][index].properties.geom 
	}-*/;
	
	/**
	* @return number of geometries in this axe
	**/
	public final native int getGeoNumber() /*-{ 
		return this.members[0].length
	}-*/;
	
	/**
	* @param index int representing wanted geomtry's index
	*
	* @return JavaScriptObject which is a GeoJSON representation of the geometry
	**/
	public final String getGeoJSON(int index) {   
		JavaScriptObject jso = getGeo(index);
        return new JSONObject(jso).toString();
	}
}