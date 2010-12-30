 /**
 * FilterAxis.java
 *
 * Class representing Filter axis from the CellSet
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

public class FilterAxis extends CellSetAxis{

	protected FilterAxis(){
	}
		
	/**
	* @return number of filter
	**/
	public final native int getFilterNumber() /*-{ 
		return this.members.length
	}-*/;

}