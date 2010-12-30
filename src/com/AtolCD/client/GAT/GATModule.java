 /**
 * GATModule.java
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 **/
 
 package com.AtolCD.client.GAT;

//Imports GWT
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.http.client.Header;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;

//Imports GAT
import com.AtolCD.client.GAT.widgets.MeasureChooser;
import com.AtolCD.client.GAT.widgets.CubeChooser;
import com.AtolCD.client.GAT.widgets.HierarchyChooser;
import com.AtolCD.client.GAT.widgets.DataDisplayer;
import com.AtolCD.client.GAT.widgets.LegendDisplayer;
import com.AtolCD.client.GAT.widgets.FilterTree;
import com.AtolCD.client.GAT.widgets.TreeNode;
import com.AtolCD.client.GAT.widgets.ToolBar;

import com.AtolCD.client.GAT.utils.OlapConfig;
import com.AtolCD.client.GAT.utils.Config;
import com.AtolCD.client.GAT.utils.Observer;
import com.AtolCD.client.GAT.utils.Observable;
import com.AtolCD.client.GAT.utils.BlindedPopup;

import com.AtolCD.client.GAT.maps.MapManager;

import com.AtolCD.client.GAT.olap.OlapAjax;
import com.AtolCD.client.GAT.olap.MDXQuery;
import java.util.ArrayList;

public class GATModule implements Observer{

	//Modules
	private BlindedPopup bpp;
	private Frame pivot;
	private MapManager mMap;
	private OlapConfig olcf;
	private Config cf;
	private OlapAjax oljx;

	private MeasureChooser mc;
	private DataDisplayer dd;
	private LegendDisplayer ld;
	
	private FilterTree ft;
	
	private CubeChooser cc;
	private HierarchyChooser hc;
	
	private Observable obs_c;

	private ToolBar tb;


	public GATModule (){
		
		//Module iframe
		pivot = new Frame("");
		//Default size
		pivot.setSize("1200px", "800px");

		//Map Manager et olapAjax, core
		mMap = new MapManager();

		// Mesures, Détails des données et légende
		mc = new MeasureChooser(mMap);
		dd = new DataDisplayer(mMap);
		ld = new LegendDisplayer(mMap);
		
		// Arbre de sélection
		tb = new ToolBar(this);
		ft = new FilterTree();
		cc = new CubeChooser();
		hc = new HierarchyChooser();
		
		//lancer le chargement du fichier de conf et instancier les widgets
		obs_c = new Observable();
		obs_c.addObserver(this);
		
		//Le data displayer ecoute les MAJ de la map / du curseur
		mMap.obsadd(dd);
		mMap.obsloadadd(ld);
		
		getConfFile();
	}
	
	private void setJSConf(String conf){
		
		System.out.println("setJsConf");
		this.olcf = (OlapConfig)getJSFromJson(conf);
		this.cf = new Config(olcf);
		this.cf.setCube(0);
		mMap.updateConf(this.olcf);
		
		//Olap Ajax settings
		oljx = new OlapAjax();
		oljx.attachPivot(pivot,this.olcf.getJpivotUrl());
		oljx.addObserver(mMap);
		oljx.addObserver(mc);
		oljx.addObserver(cf);
		
		//Widgets initialisation
		cc.init(cf);
		hc.init(cf);
		
		//Attach olapAjax object with Map Manager
		mMap.attachOljx(oljx);
		update();
		System.out.println("Response update");
		ft.init(cf);
		cc.addObserver(this);
		hc.addObserver(this);
	}

	private void getConfFile(){
	
		String urlConf = getCurrentUrl();
		RequestBuilder build = new RequestBuilder(RequestBuilder.GET, urlConf+"getConf.jsp");


		try{
			System.out.println(""+build.getUrl());
			Request request1= build.sendRequest("", new RequestCallback(){
			
			public void onError(Request request1, Throwable exception) {
				System.out.println("Couldn't retrieve JSON");
			}

			public void onResponseReceived(Request request1, Response response) {
				if (200 == response.getStatusCode()) {
					String rsp = response.getText();
					System.out.println("Response OK : "+rsp);
					setJSConf("["+rsp+",]");
				}
				else{
					System.out.println("Connexion fail "+response.getStatusCode());
				}
			}
			});
		} 
		catch (RequestException e){
			System.out.println("Couldn't retrieve JSON Config");
		}
	}
	
	//Converts the Json representation into a Java-Encapsulated-Javascript-Object
	public native JavaScriptObject getJSFromJson(String json) /*-{
		$wnd.conf = eval(json);
		console.log(eval(json));
		return $wnd.conf[0];
	}-*/;
		
	public native String getCurrentUrl() /*-{
		console.log($wnd.document.URL);
		return $wnd.document.URL;
	}-*/;
		
	@Override
	public void update(){
	
		//On recupère le cube
		cf.setCube(cc.getSelectedIndex());
		
		//On recupère la hierarchie
		cf.setHier(hc.getSelectedIndex());
		
		//Reconstruction de la requête
		int geo = Integer.parseInt(cf.getOlapConfig().getGeomIndex());
		int hi = cf.getHier();
		boolean nonEmpty = false;
		String nonEmp = (nonEmpty)?"NON EMPTY":"";
		String init_request = "SELECT {AddCalculatedMembers([Measures].Members)} ON COLUMNS, "+nonEmp+" {[";
		init_request += cf.getCurrentCube().getDimension(geo).getName()+".";
		init_request += cf.getCurrentCube().getDimension(geo).getHierarchy(hi).getName()+"].[";
		init_request += cf.getCurrentCube().getDimension(geo).getHierarchy(hi).getAllMembersName();
		init_request += "].Children} ON ROWS FROM "+cf.getCurrentCube().getName();
		
		System.out.println(init_request);
		this.write(init_request);
		
		String tps = URL.encode(init_request);
		pivot.setUrl(this.olcf.getJpivotUrl()+tps);
		
		// set query Olapajax
		oljx.setMdxQuery(init_request);
		
		// Submit the query
		oljx.submitQuery();
		
	}
	
	@Override
	public void update(Object o){}
	
	public native void write(String json) /*-{
		console.log(json);
	}-*/;
	
	//TODO : ajouter des verifs
	public void execute(){
	ArrayList tmp_array = mMap.getSelectedFeatures();
	if(tmp_array.isEmpty()){
		//popup select a feature
		bpp = new BlindedPopup("No feature selected",true,true);
	}else{
			String request_main = ft.getMDXObject().getQuery(mMap.getSelectedFeatures());
			MDXQuery mdxo = ft.getMDXObject().getQueryObject(mMap.getSelectedFeatures());
			oljx.setMdxQuery(mdxo);
			this.pivot.setUrl(this.olcf.getJpivotUrl()+mdxo.getQueryString());
			oljx.submitQuery();
		}
	}
	
	public Widget getMap(){ return this.mMap.getMW(); }
	public MapManager getMapWidget(){ return this.mMap; }
	public Widget getMeasureChooser(){ return this.mc.getList(); }
	public Widget getDataDisplayer(){ return this.dd.getDataPanel(); }
	public Widget getLegendDisplayer(){ return this.ld.getDataPanel(); }
	public Widget getFilterTree(){ return this.ft.getTree(); }
	public Widget getCubeChooser(){ return this.cc.getList(); }
	public Widget getHierarchyChooser(){ return this.hc.getList(); }
	public Widget getToolBar(){ return this.tb.getToolbar(); }
	public Widget getFrame(){ return this.pivot; }
	
}