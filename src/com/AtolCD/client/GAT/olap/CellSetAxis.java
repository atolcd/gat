 /**
 * CellSetAxis.java
 *
 * Class representing any axe from the CellSet
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

import com.AtolCD.client.GAT.olap.Member;

public class CellSetAxis extends JavaScriptObject{

	protected CellSetAxis(){
	}
	
	public final native CellSetAxis getAx() /*-{ return this }-*/;
	public final native int getPosLength() /*-{ return this.positions.length }-*/;
	public final native int getPosition(int i) /*-{ return this.positions[i][0] }-*/;
	public final native Member getMember(int i) /*-{ return this.members[i] }-*/;
	public final native Member getMember(int i,int j) /*-{ return this.members[i][j] }-*/;
	public final native int getMemberCount(int i) /*-{ return this.members[i].length }-*/;

	
	/**
	@deprecated Use getName() method in Member object
	*/
	public final native String getMemberName(int i,int j) /*-{ return this.members[i][j].name }-*/;
	
	/**
	*
	* @return JSON String representation of current object
	*
	**/
	public final String toJSON() {          
        return new JSONObject(this).toString();
	}
	
}