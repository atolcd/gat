 /**
 * MemberProperties.java
 *
 * Mapped class for accessing member properties
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

public class MemberProperties extends JavaScriptObject{

	protected MemberProperties(){}
	
	
	public final native int getChildrenCardinality()/*-{return this.CHILDREN_CARDINALITY; }-*/;
	
	//Level Properties
	public final native int getLevelNumber()/*-{return this.LEVEL_NUMBER; }-*/;

	//Member properties
	public final native String getMemberCaption()/*-{return this.MEMBER_CAPTION }-*/;
	public final native String getMemberGuid()/*-{return this.MEMBER_GUID }-*/;
	public final native String getMemberKey()/*-{return this.MEMBER_KEY }-*/;
	public final native String getMemberName()/*-{return this.MEMBER_NAME }-*/;
	public final native int getMemberOrdinal()/*-{return this.MEMBER_ORDINAL }-*/;
	public final native String getMemberType()/*-{return this.MEMBER_TYPE }-*/;
	public final native String getMemberUniqueName()/*-{return this.MEMBER_UNIQUE_NAME }-*/;
	
	//Parent properties
	public final native int getParentCount()/*-{return this.PARENT_COUNT; }-*/;
	public final native int getParentLevel()/*-{return this.PARENT_LEVEL; }-*/;
	public final native String getParentUniqueName()/*-{return this.PARENT_UNIQUE_NAME; }-*/;
	
	public final native String getSchemaName()/*-{return this.SCHEMA_NAME; }-*/;
	public final native String getDimensionUniqueName()/*-{return this.DIMENSION_UNIQUE_NAME; }-*/;

}