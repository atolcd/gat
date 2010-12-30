 /**
 * Config.java
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100920
 **/
 
package com.AtolCD.client.GAT.utils;

import com.AtolCD.client.GAT.utils.Observer;
import com.AtolCD.client.GAT.olap.CellSet;
import com.AtolCD.client.GAT.olap.FilterAxis;
import com.AtolCD.client.GAT.olap.OlapAjax;

public class Config implements Observer{

	private OlapConfig olapConf;
	private int currentCube;
	private int currentHier;
	private int nbFilter;
	
	public Config(){}
	
	public Config(OlapConfig olcf){
		this.olapConf = olcf;
	}
	
	public OlapConfig getOlapConfig(){
		return this.olapConf;
	}
	
	public void setCube(int i){
		this.currentCube = i;
		this.setHier(0);
	}
	
	public void setHier(int i){
		this.currentHier = i;
	}
	
	public int getHier(){
		return this.currentHier;
	}
	
	public Cube getCurrentCube(){
		return this.olapConf.getCube(this.currentCube);
	}
	
	private void setFilterCard(int i){
		this.nbFilter = i;
	}

	public int getFilterCard(){
		return this.nbFilter;
	}
	
	@Override
	public void update(){
		
	}

	@Override
	public void update(Object o){
		OlapAjax oa = (OlapAjax)o;
		CellSet cs = oa.getCellSet();	
		FilterAxis fsa = (FilterAxis)cs.getFilterAxis();
		
		this.setFilterCard(fsa.getFilterNumber());
	}
	
	
}