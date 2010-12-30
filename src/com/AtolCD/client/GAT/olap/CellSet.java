 /**
 * CellSet.java
 *
 * Class representing an OLAP response CellSet
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

public class CellSet extends JavaScriptObject{

	protected CellSet(){
	}
	
	/**
	* @param index Integer representing axis index
	**/
	public final CellSetAxis getAxis(int index){
	
		if (index == -1) {
			return getFilterAxis();
		}
		else {
			return getAxisTab(index);
		}
	}

	public final String getCell(int[] coordinates) {

		// we have coordinates (position ordinals for each axis), retrieve
		// the corresponding cell
		int ordinal = this.coordinatesToOrdinal(coordinates);
		return getCell(ordinal);	
	}
	
	
	private int coordinatesToOrdinal(int[] coords){

		int nbAxes = this.getNumberOfAxes();
		
		if(coords.length == nbAxes) {
			int modulo = 1;
			int ordinal = 0;
			int i;
			
			for (i = 0 ; i < nbAxes ; i++ ) {

				CellSetAxis axis = this.getAxisTab(i);
				int coordinate = coords[i];
				
				if (coordinate < 0 || coordinate > axis.getPosLength()) {
					// invalid coordinates
					System.out.println("Coordinates error "+coordinate+" "+axis.getPosLength());
					return 0;
				}
				
				ordinal += coordinate * modulo;
				modulo *= axis.getPosLength();

			}
			return ordinal;
		}
		return 0;
	}

	//Recuperation de la cellule
	public final native String getCell(int coordinates) /*-{ 
		var a = (this.cellSet.cells[coordinates]!=null)?(this.cellSet.cells[coordinates]):(0);a=""+a+"";
		return a;
	}-*/;
	
	//Accesseurs Javascript
	public final native CellSetAxis getFilterAxis()/*-{return this.cellSet.filterAxis}-*/;
	public final native CellSetAxis getAxisTab(int ordinal)/*-{return this.cellSet.axes[ordinal]}-*/;
	public final native int getNumberOfAxes()/*-{return this.cellSet.axes.length}-*/;
	
	
}