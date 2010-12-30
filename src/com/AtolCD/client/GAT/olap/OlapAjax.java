 /**
 * OlapAjax.java
 *
 * Olap Querier
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100927
 **/
 
package com.AtolCD.client.GAT.olap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.http.client.Header;
import com.AtolCD.client.GAT.olap.CellSet;
import com.AtolCD.client.GAT.olap.MDXQuery;
import com.google.gwt.user.client.ui.Frame;

import com.google.gwt.core.client.JavaScriptObject;

import com.AtolCD.client.GAT.utils.Observable;
import com.AtolCD.client.GAT.utils.Observer;
import com.AtolCD.client.GAT.utils.BlindedPopup;

public class OlapAjax{

	//Requete MDX texte
	private String mdxQuery;
	private MDXQuery mdxQueryObj;
	//CellSet reponse
	private CellSet cs;
	private Frame insidePivot;
	private String pUrl;
	//Observable item for notifying response(s)
	private Observable changeObs;
	BlindedPopup bpp;
	/**
	* Constructor
	**/
	public OlapAjax(){
		changeObs = new Observable();
		
	}

	
	/**
	* Public access for Observable item
	@param obs Any class that implements update() and update(Object o) methods
	**/
	public void addObserver(Observer obs){
		this.changeObs.addObserver(obs);
		//System.out.println("Observer Added "+obs.getClass().getName());
	}
	
	public void attachPivot(Frame pivot, String pUrl){
		insidePivot = pivot;
		pUrl = pUrl;
	}
	
	
	public void setMdxQuery(String query){
			this.mdxQuery = query;
	}
	
	public void setMdxQuery(MDXQuery query){
			
			this.mdxQueryObj = query;
			this.mdxQuery = query.getQueryString();
	}
	
	public MDXQuery getMDXObject(){ return this.mdxQueryObj;}
	
	
	public void submitQuery(){
	bpp = new BlindedPopup("Asking GeoMondrian",true, false);
 	System.out.println("launch req ");
		String url = "SpatialyticsServlet";
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		String request_main = "mdxQuery="+this.mdxQuery+"&outputFormat=olapJSON";

		String values = com.google.gwt.http.client.URL.encode(request_main);
		
		builder.setHeader("Content-length",""+values.length());
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		builder.setHeader("X-Requested-With", "XMLHttpRequest");

	  try {
			
			Request request = builder.sendRequest(values, new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				System.out.println("Couldn't retrieve JSON");
			}

			public void onResponseReceived(Request request, Response response) {
			  if (200 == response.getStatusCode()) {
				String json = response.getText();
				setCellSet("["+json+",]");
				System.out.println("update req ");
				
				
				updateMap();
				
			  } else {
			System.out.println("Couldn't retrieve JSON (" + response.getStatusText() + response.getStatusCode() + ")");

			  }
        }
      });

	  
		} catch (RequestException e) {
		  System.out.println("Couldn't retrieve JSON");
		}
	}
	
	public CellSet getCellSet(){
		return this.cs;
	}	
	
	public native CellSet getJSObject(String json) /*-{
		$wnd.cells = eval(json);
		return $wnd.cells[0];
	}-*/;

	private void setCellSet(String jsonSet){
		this.cs = getJSObject(jsonSet);	
	}
	
	private void updateMap(){
		String tps = URL.encode(this.mdxQuery);
		if(insidePivot!=null){
		insidePivot.setUrl(pUrl+tps);}
		changeObs.update(this);
		bpp.fermer();
	}
}