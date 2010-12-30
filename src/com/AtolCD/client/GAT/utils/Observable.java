 /**
 * Observable.java
 *
 * Pattern Observable
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 **/
 
package com.AtolCD.client.GAT.utils;

import java.util.ArrayList;

public class Observable{
	
	private ArrayList arr;
	
	public Observable(){
		arr = new ArrayList(2);
	}
	
	// Adds an observer to the list
	public void addObserver(Observer o){
		arr.add(o);
	}
	
	public void update(){
		for(int t=0;t<arr.size();t++){
			((Observer)(arr.get(t))).update();
		}
	}
	
	public void update(Object o){
		for(int t=0;t<arr.size();t++){
			((Observer)(arr.get(t))).update(o);
		}
	}
	
}