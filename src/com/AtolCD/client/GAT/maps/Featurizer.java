 /**
 * Featurizer.java
 *
 * Class factory for features
 * La version actuelle du slicer ne gere pas un nombre infini de dimensions
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 **/

package com.AtolCD.client.GAT.maps;

//imports externes
//imports gwt openlayers
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;

//Imports javascript
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JavaScriptObject;

//Imports internes
import com.AtolCD.client.GAT.olap.CellSet;
import com.AtolCD.client.GAT.olap.GeomAxis;
import com.AtolCD.client.GAT.utils.OlapConfig;

import com.AtolCD.client.GAT.olap.MemberProperties;
import java.util.ArrayList;

public class Featurizer{

	int[] slicerArray;
	double[] val;
	private Legendizer lg;
	private OlapConfig olcf;
	
	/**
	* Constructor
	**/
	public Featurizer(String a, String b){
		slicerArray = new int[1];
		slicerArray[0] = 0;
		lg = new Legendizer(a,b);
	}

	public VectorFeature[] buildFeatures(CellSet cs){
	
		//Gets the Geometric axis from the CellSet
		GeomAxis ga = (GeomAxis)cs.getAxis(1);
		
		//GeoJSON converter
		GeoJSON b =  new GeoJSON();
		
		int nb = ga.getGeoNumber();
		
		StringBuffer jsonFeatures = new StringBuffer("{\"type\":\"FeatureCollection\",\"features\":[");
		
		
		int nbAxes = cs.getNumberOfAxes();
		int coordArray[];
		int geomAxisOrdinal =1;

		coordArray = new int[nbAxes];
		
		for(int i=0;i<nbAxes;i++){

			if (i == geomAxisOrdinal) {
				coordArray[i] = 0;
			}
			else if (this.slicerArray.length-1 <= i) {//a verifier
				coordArray[i] = this.slicerArray[i];//multi dimensions...
			}
			else {
				coordArray[i] = 0;
			}
			
		}

		val = new double[nb];
		
		for(int i =0;i<nb;i++){
			coordArray[geomAxisOrdinal] = i;
			try{
				val[i] = Double.parseDouble(cs.getCell(coordArray));
			}catch(java.lang.IllegalArgumentException e){
				val[i] = 0.0;
			}
		}

		
		lg.reMake(val);

		
		//
		MemberProperties[] mpp = new MemberProperties[nb];
		for(int l =0;l<nb;l++){
			mpp[l] = ga.getMember(0,l).getProperties();
		}
		//
		for(int l =0;l<nb;l++){
			int opl = 1;

			String okr = lg.getColor(val[l]);
			
			System.out.println("Designing shape #"+l);

			
			jsonFeatures.append("{\"type\":\"Feature\",\"id\":\"OpenLayers.Feature.Vector_287\",\"properties\":{");
			jsonFeatures.append("\"docol\":\"");
			jsonFeatures.append(okr);
			jsonFeatures.append("\",\"value\":");
			jsonFeatures.append(val[l]);
			jsonFeatures.append(",\"axisOrdinal\":");
			jsonFeatures.append(geomAxisOrdinal);
			jsonFeatures.append(",\"positionOrdinal\":");
			jsonFeatures.append(l);
			jsonFeatures.append(",\"hasChilds\":");
			jsonFeatures.append((mpp[l].getChildrenCardinality() > 0)?"1":"0");
			jsonFeatures.append(",\"hasParents\":");
			jsonFeatures.append((mpp[l].getParentLevel() == 0)?"0":"1");
			jsonFeatures.append(",\"label\":\"");
			jsonFeatures.append(mpp[l].getMemberName());
			jsonFeatures.append("\"},\"geometry\":");
			jsonFeatures.append(ga.getGeoJSON(l));
			jsonFeatures.append("},");
		}


		jsonFeatures.append("]}");
		
		VectorFeature[] polygonFeature = b.read(jsonFeatures.toString());

		return polygonFeature;
	}
	
	public Legendizer getLegendizer(){
		return this.lg;
	}
	
	public void setSlicer(int i){
		this.slicerArray[0] = i;
	}
	

}