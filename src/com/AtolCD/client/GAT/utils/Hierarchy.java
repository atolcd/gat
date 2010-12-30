 /**
 * Hierarchy.java
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

public class Hierarchy extends JavaScriptObject{

	protected Hierarchy(){}
	
	public final native String getName()/*-{return this.name }-*/;
	public final native String getAllMembersName()/*-{return this.allmn }-*/;
	
	
}