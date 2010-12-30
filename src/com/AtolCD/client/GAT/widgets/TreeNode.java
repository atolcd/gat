 /**
 * TreeNode.java
 *
 * Object representation of a Node for dimensions tree
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


public class TreeNode extends TreeItem{

	private MemberProperties mProps;

	public TreeNode(){
		super();
	}

	public TreeNode(String stt){
		super(stt);
	}
	
	public TreeNode(MemberProperties props,ClickListener listener){
		
		super();
		
		TreeBox treeB = new TreeBox(props);
		treeB.addClickListener(listener);
		setWidget(treeB);
		mProps = props;
	}
	
	public MemberProperties getProperties(){
		return mProps;
	}
 
 
}

