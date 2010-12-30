 /**
 * Observer.java
 *
 * Pattern Observer update method called by Observable class
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 **/
 
 package com.AtolCD.client.GAT.utils;

public interface Observer{
	
	public void update();
	public void update(Object o);

}