 /**
 * OlapConfig.java
 *
 * Class representing app conf.
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100920
 **/
 
package com.AtolCD.client.GAT.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;


public class OlapConfig extends JavaScriptObject{

	protected OlapConfig(){}

	/**
	* Gets geometry dimension index
	**/
	public final native String getGeomIndex()/*-{ return this.geom }-*/;
	public final native String getIntervalsNb()/*-{ return this.base_intervals }-*/;
	public final native String getMinCol()/*-{ return this.base_min_col }-*/;
	public final native String getMaxCol()/*-{ return this.base_max_col }-*/;
	public final native String getJpivotUrl()/*-{ return this.jpivot_url }-*/;
	public final native int getCubesCount()/*-{ return this.Cubes.length }-*/;
	
	public final native String getWmsUrl()/*-{ return this.wms_layer_url }-*/;
	public final native String getWmsFormat()/*-{ return this.wms_layer_format }-*/;
	public final native String getWmsLayer()/*-{ return this.wms_layer_layer }-*/;
	public final native String getHoverColor()/*-{ return this.hover_color }-*/;
	
	public final native int getZoom()/*-{ return this.map_center_zoom }-*/;
	public final native double getLat()/*-{ return this.map_center_lat }-*/;
	public final native double getLon()/*-{ return this.map_center_lon }-*/;
	
	public final native double getOpacity()/*-{ return this.map_fill_opacity }-*/;
	public final native String getFontColor()/*-{ return this.map_font_color }-*/;
	public final native String getFontFamily()/*-{ return this.map_font_family }-*/;
	public final native String getFontSize()/*-{ return this.map_font_size }-*/;
	public final native String getStrokeColor()/*-{ return this.map_stroke_color }-*/;
	public final native String getStrokeHoverColor()/*-{ return this.map_stroke_hovercolor }-*/;

	public final native Cube getCube(int ind)/*-{ return this.Cubes[ind] }-*/;
	
	/**
	@deprecated
	**/
	public final native int getCubeName(int ind)/*-{ return this.Cubes[ind].name }-*/;
	


	
}