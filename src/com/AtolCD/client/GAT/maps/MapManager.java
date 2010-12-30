 /**
 * MapManager.java
 *
 * Class for managing map widget
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 * 
 * import org.gwtopenmaps.openlayers.client.layer.GoogleOptions;
 * import org.gwtopenmaps.openlayers.client.layer.Google;
 **/
 
package com.AtolCD.client.GAT.maps;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Icon;
import org.gwtopenmaps.openlayers.client.util.JObjectArray;
import org.gwtopenmaps.openlayers.client.util.JSObject;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Marker;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.control.LayerSwitcher;
import org.gwtopenmaps.openlayers.client.control.MousePosition;
import org.gwtopenmaps.openlayers.client.control.MouseToolbar;
import org.gwtopenmaps.openlayers.client.control.PanZoomBar;
import org.gwtopenmaps.openlayers.client.control.Scale;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeatureOptions;
import org.gwtopenmaps.openlayers.client.event.EventHandler;
import org.gwtopenmaps.openlayers.client.event.EventObject;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.format.KML;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.LinearRing;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.geometry.Polygon;
import org.gwtopenmaps.openlayers.client.event.Events;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Markers;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;
import org.gwtopenmaps.openlayers.client.popup.AnchoredBubble;
import org.gwtopenmaps.openlayers.client.popup.Popup;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.http.client.Header;
import com.google.gwt.core.client.JsArray;

import com.google.gwt.json.client.JSONObject;

import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.AtolCD.client.GAT.olap.CellSet;
import com.AtolCD.client.GAT.olap.GeomAxis;
import com.AtolCD.client.GAT.olap.OlapAjax;

import com.AtolCD.client.GAT.utils.BlindedPopup;
import com.AtolCD.client.GAT.utils.Observable;
import com.AtolCD.client.GAT.utils.Observer;
import com.AtolCD.client.GAT.utils.OlapConfig;
import com.AtolCD.client.GAT.utils.Config;

import java.util.ArrayList;

import com.google.gson.*;

/*
*	Classe de gestion de la map / mapWidget
*   TODO : add methods for add/remove markers when markers layer is activated
*/

public class MapManager implements Observer{

	//Openlayers vars
    private MapWidget mapWidget;
	private Map map;
	private WMS wmsLayer;
	private Markers markers;
	private Popup popup;	
	private Vector OLAPLayer;
	private VectorOptions OLAPVectorOptions;
	private VectorFeature[] polygonFeature2;
	private SelectFeature select_multiple;
	private SelectFeature hover_simple;
	
	//GAT vars
	private Featurizer ft;
	private ArrayList selectedFeatures;
	private boolean isMarks;
	private Observable changeObs,endLoadObs;
	private CellSet currentCellSet;
	private OlapAjax oljx;
	private int outil;
	private Config main_conf;
	private OlapConfig olap_conf;
	
	
	//Map Config vars
	private String color;//+ extent, wms
	
	public MapManager(){

		//Are we using markers layers?
		isMarks = false;
		outil = -1;

		// let's create map options
		MapOptions mapOptions = new MapOptions();
		mapOptions.setControls(new JObjectArray(new JSObject[] {}));
		mapOptions.setNumZoomLevels(16);
		mapOptions.setProjection("EPSG:4326");
		selectedFeatures = new ArrayList();
		
		//Options de la map
		
		//Definition des styles de la map
		OLAPVectorOptions = new VectorOptions();
		Style main_style = new Style();
		main_style.setFillColor("${docol}");//Get this one from a var in geometry object definition
		//style par defaut puis recharger si on a des styles définis plus tards
		main_style.setLabel("${label}");
		Style hover_style = new Style();
		hover_style.setFillColor("blue");
		Style special_style = new Style();
		special_style.setFillColor("pink");
		
		//Style de la map, Normal, Hover/selected, 
		StyleMap styleMap = new StyleMap(main_style,hover_style,special_style);
		OLAPVectorOptions.setStyleMap(styleMap);

		// let's create map widget and map objects
		mapWidget = new MapWidget("950px", "650px", mapOptions);
		map = mapWidget.getMap();
		if(isMarks){markers = new Markers("Markers layer");}
		OLAPLayer = new Vector("GAT Layer",OLAPVectorOptions);//options

		//Paramètres de la couche WMS
		WMSParams wmsParams = new WMSParams();
		wmsParams.setFormat("image/png");
		wmsParams.setLayers("communes");
		wmsParams.setStyles("");

		//Couche WMS de la carte / fond de carte
		wmsLayer = new WMS("Communes", "http://www.geosignal.org/cgi-bin/wmsmap?", wmsParams);

		//Centrer la carte
		LonLat center = new LonLat(6.11131, 48.54128);

		//Ajout de la couche WMS à la carte
		map.addLayers(new Layer[] {wmsLayer,OLAPLayer});

		//Echelle de zoom
		map.addControl(new PanZoomBar());
		//Coordonées de la souris
		map.addControl(new MousePosition());
		//Bar d'outils souris
		map.addControl(new MouseToolbar());
		//Selecteur de couche(s)
		map.addControl(new LayerSwitcher());

		//Options de la map (centre de la map, niveau de zoom)	
		map.setCenter(center, 8);

		//GeoJson Reader
        GeoJSON b =  new GeoJSON();

		//Option de selection (multiple, hover,... )
        SelectFeatureOptions sfo = new SelectFeatureOptions();
        SelectFeatureOptions sfo2 = new SelectFeatureOptions();

		sfo2.setHover();
		sfo.setMultiple();
		sfo.setToggle();

		select_multiple = new SelectFeature(OLAPLayer, sfo);
		hover_simple = new SelectFeature(OLAPLayer, sfo2);

		map.addControl(select_multiple);
		map.addControl(hover_simple);
		select_multiple.activate();	

		//Observable objects fo handling events and map updates
		changeObs = new Observable();
		endLoadObs = new Observable();

		//We'll handle hover/click in this part
		
		/**
		* Registers click handler for featuresselected
		**/
		OLAPLayer.getEvents().register("featureselected", select_multiple, new EventHandler() {
		
			@Override
			public void onHandle(EventObject eventObject) {

				changeObs.update(eventObject);
				//gere l'ajout a l'arraylist si on est en mode requesting

				int in = eventObject.getJSObject().getProperty("feature").getProperty("attributes").getPropertyAsInt("axisOrdinal");
				int on = eventObject.getJSObject().getProperty("feature").getProperty("attributes").getPropertyAsInt("positionOrdinal");

				CellSet cset = getCS();
				String uus = cset.getAxis(in).getMember(0,on).getProperties().getMemberUniqueName();
				boolean hasParent = (cset.getAxis(in).getMember(0,on).getProperties().getParentLevel() == 0)?false:true;
				boolean hasChild = (cset.getAxis(in).getMember(0,on).getProperties().getChildrenCardinality() > 0)?true:false;
				
				//if outil == 1 ou 2 alors on applique l'operateur olap
				//sinon on add
				if(outil > -1){// alors on applique l'operateur olap
					
					if(checkNav(outil,hasChild,hasParent)){
						String newReq = oljx.getMDXObject().olapNav(uus,outil);
						oljx.setMdxQuery(newReq);
						oljx.submitQuery();				
					}else{
						BlindedPopup pp = new BlindedPopup("Request error : no more levels",true, true);
					}
				}
				else{
					selectedFeatures.add(uus);
				}
			}
		});
		
		/**
		* Registers click handler for features unselected
		**/
		OLAPLayer.getEvents().register("featureunselected", select_multiple, new EventHandler() {

		@Override
		public void onHandle(EventObject eventObject) {

			int in = eventObject.getJSObject().getProperty("feature").getProperty("attributes").getPropertyAsInt("axisOrdinal");
			int on = eventObject.getJSObject().getProperty("feature").getProperty("attributes").getPropertyAsInt("positionOrdinal");

		
			
			CellSet cset = getCS();
			String uus = cset.getAxis(in).getMember(0,on).getProperties().getMemberUniqueName();
			boolean hasParent = ((cset.getAxis(in).getMember(0,on).getProperties().getParentLevel() == 0)?false:true);
			boolean hasChild = ((cset.getAxis(in).getMember(0,on).getProperties().getChildrenCardinality() > 0)?true:false);
				
				
			if(outil > -1){// alors on applique l'operateur olap
				if(checkNav(outil,hasChild,hasParent)){
					String newReq = oljx.getMDXObject().olapNav(uus,outil);
					oljx.setMdxQuery(newReq);
					oljx.submitQuery();
				}else{
						BlindedPopup pp = new BlindedPopup("Request error : no more levels",true,true);
				}
			}
			else{
				selectedFeatures.remove(uus);
			}
		}
	});
	}

	/**
	* Getter for current widget
	**/
	public MapWidget getMW(){
		return this.mapWidget;
	}

	/**
	* Mesure changed (on repositionne le slicer)
	**/
	public void setSlicer(int i){
	System.out.println("set slicer form map manager : "+i);
		this.ft.setSlicer(i);
		this.maj();
	}

	/**
	* Inner observer modifier
	**/
	public void obsadd(Observer o){
		changeObs.addObserver(o);
	}

	public void obsloadadd(Observer o){
		endLoadObs.addObserver(o);
	}

	public ArrayList getSelectedFeatures(){
		return this.selectedFeatures;
	}

	/**
	* Mise à jour sans nouvelle requête (mesure)
	**/
	public void maj(){

		VectorFeature[] polygonFeatures = ft.buildFeatures(this.currentCellSet);

		//Empty OLAP layer
		OLAPLayer.destroyFeatures();

		//Add new features
		OLAPLayer.addFeatures(polygonFeatures);
		endLoadObs.update();
	}
		
	/**
	* Mise à jour avec nouveau CellSet
	@param geo_set CellSet to be reproduced on the map (geometric features)
	**/
	public void maj(CellSet geo_set){

		if(ft == null){
			ft = new Featurizer("#FFFF00","#FF0000");//default colors, should not be used if correct conf
		}
		this.currentCellSet = geo_set;

		//Recuperation des éléments géométriques
		VectorFeature[] polygonFeatures = ft.buildFeatures(geo_set);

		//Empty OLAP layer
		OLAPLayer.destroyFeatures();
		
		//Add new features
		OLAPLayer.addFeatures(polygonFeatures);
		this.selectedFeatures = new ArrayList();
		endLoadObs.update();
	}

	/**
	@deprecated car la fonction n'est pas implémentée dans l'interfaçage GWT/openlayers
	* pourra etre ré-utilisée plus tard en lieu et place des listeners/arraylist
	**/ 
	public VectorFeature[] getCurrHover(){

		VectorFeature[] p = OLAPLayer.getSelectedFeatures();
		
		if(p==null){
			System.out.println("Error : Feature is NULL");
		}else{
			System.out.println("hover : "+p.getClass().getName());
		}
		
		return p;
	}
	
	public native void setJSCells(String json) /*-{
		$wnd.cs = eval(json);
	}-*/;

	public String setCells(JavaScriptObject js){ 
		return new JSONObject(js).toString();
	}
	
	private boolean checkNav(int tool, boolean childs, boolean parent){

		if(tool == 0 && childs){return true;}//We can only drill down when we have childs
		if(tool == 1 && parent){return true;}//We can only roll up if this geomertry has a parent
		
		return false;//this operation is not possible
	}
	
	
	public void updateConf(OlapConfig cf){
	
		//WMS layer - from config file
		WMSParams wmsParams = new WMSParams();
		wmsParams.setFormat(cf.getWmsFormat());
		wmsParams.setLayers(cf.getWmsLayer());
		wmsParams.setStyles("");

		//Couche WMS de la carte / fond de carte
		WMS wmsLayer2 = new WMS("Communes", cf.getWmsUrl(), wmsParams);
	
		LonLat center = new LonLat(cf.getLat(),cf.getLon());
		map.setCenter(center, cf.getZoom());

		OLAPVectorOptions = new VectorOptions();
		Style main_style = new Style();
		main_style.setFillColor("${docol}");//Get this one from a var in geometry object definition
		//if (label ->) faire un style par defaut puis recharger si on a des styles définis plus tards
		main_style.setLabel("${label}");
		main_style.setFontSize(cf.getFontSize());
		main_style.setFontColor(cf.getFontColor());
		main_style.setFontFamily(cf.getFontFamily());
		main_style.setStrokeColor(cf.getStrokeColor());
		main_style.setFillOpacity(cf.getOpacity());
		
		Style hover_style = new Style();
		hover_style.setFillColor(cf.getHoverColor());
		hover_style.setStrokeColor(cf.getStrokeHoverColor());
		hover_style.setFillOpacity(cf.getOpacity());
		Style special_style = new Style();
		special_style.setFillColor("pink");// not used
		//if exists maj sinon creer
		ft = new Featurizer(cf.getMinCol(),cf.getMaxCol());

		//Style de la map, Normal, Hover/selected, 
		StyleMap styleMap = new StyleMap(main_style,hover_style,special_style);
		OLAPVectorOptions.setStyleMap(styleMap);
		
		OLAPLayer.addOptions(OLAPVectorOptions);
		map.addLayer(wmsLayer2);//add the new WMS layer
		map.removeLayer(wmsLayer);//remove basic layer
	}
	
	/**
	* Current CellSet getter
	**/
	public CellSet getCS(){ 
		return this.currentCellSet;
	}

	@Override
	public void update(){
	}
	
	@Override
	public void update(Object o){
		OlapAjax oa = (OlapAjax)o;
		CellSet a = oa.getCellSet();	
		this.maj(a);
	}

	//selection for request
	public void setSelec(){
		hover_simple.deactivate();
		select_multiple.activate();
	}
	
	public void setHover(){
		select_multiple.deactivate();
		hover_simple.activate();
	}
	
	public void setClick(){
	
	}
	
	public void attachOljx(OlapAjax oljx){
		this.oljx = oljx;//add modif couleurs et intervalles
	}
	
	public void setOlapTool(int tool){
		this.outil = tool;
	}
	
	public Legendizer getLegendizer(){
		return this.ft.getLegendizer();
	}
}