 /**
 * Member.java
 *
 * Class for managing axeis members
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

public class Member extends JavaScriptObject{

	protected Member(){
	
	}
	
	public final native MemberProperties getProperties()/*-{ return this.properties }-*/;

}