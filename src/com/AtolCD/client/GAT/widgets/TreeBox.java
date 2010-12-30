 /**
 * TreeBox.java
 *
 * Object 
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20101001
 **/
package com.AtolCD.client.GAT.widgets;

import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.AtolCD.client.GAT.olap.MemberProperties;


public class TreeBox extends CheckBox{

	private MemberProperties mem;

	public TreeBox(){
		super();
	}

	public TreeBox(String stt){
		super(stt);
	}
	
	public TreeBox(MemberProperties str){
		super(str.getMemberName());
		mem = str;
	}
	
	public MemberProperties getProperties(){
		System.out.println("Getting Properties of "+mem.getMemberUniqueName());
		return mem;
	}
 
}