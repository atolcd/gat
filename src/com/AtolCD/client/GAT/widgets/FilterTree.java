 /**
 * FilterTree.java
 *
 * Data displayer widget
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100922 
 **/
 
package com.AtolCD.client.GAT.widgets;

//extern imports
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ClickListener;

import org.gwtopenmaps.openlayers.client.event.EventObject;

//Openlayers
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

//interne imports
import com.AtolCD.client.GAT.olap.CellSetAxis;
import com.AtolCD.client.GAT.olap.CellSet;
import com.AtolCD.client.GAT.olap.OlapAjax;
import com.AtolCD.client.GAT.olap.Member;
import com.AtolCD.client.GAT.olap.MemberProperties;
import com.AtolCD.client.GAT.olap.MDXQuery;

import com.AtolCD.client.GAT.maps.MapManager;
import com.AtolCD.client.GAT.utils.Observer;
import com.AtolCD.client.GAT.utils.OlapConfig;
import com.AtolCD.client.GAT.utils.Config;


public class FilterTree implements Observer{
	
	private FlowPanel fpanel;
	private HTML label;
	private CellSet cellSetTab[];
	private OlapAjax cust_olapAjax[];
	private int cellCount = 0;
	private int memberCount;
	private TreeNode outerRoot;
	private Tree tree;
	private MDXQuery mdx;
	
	public FilterTree(){
		tree = new Tree();
		outerRoot = new TreeNode("Axes");
		tree.addItem(outerRoot);
	}
	
	public void init(Config cf){
	
		OlapConfig olpf = cf.getOlapConfig();
		mdx = new MDXQuery(cf);
		memberCount = olpf.getCube(0).getDimensionsCount()-1;
		
		cust_olapAjax = new OlapAjax[memberCount+1];
		
		cellSetTab = new CellSet[memberCount];
		for(int i=0;i < olpf.getCube(0).getDimensionsCount();i++){

			if(i == Integer.parseInt(olpf.getGeomIndex())){
				continue;
			}

			cust_olapAjax[i] = new OlapAjax();
			cust_olapAjax[i].addObserver(this);
			
			String str_tmp = "SELECT {[Measures].Members} ON COLUMNS, {["+olpf.getCube(0).getDimension(i).getName()+"].Members} ON ROWS FROM ["+olpf.getCube(0).getName()+"]";

			cust_olapAjax[i].setMdxQuery(str_tmp);
			cust_olapAjax[i].submitQuery();	
		}
		System.out.println("Analyse des axes terminée");
		
		
	}
	
	/**
	* Fonction de génération de l'arbre (UI + evt)
	**/
	private void treeGen(){
	
		int compteur = 0;
		//Event général pour les cases à cocher
		ClickListener listener = new ClickListener()
		{
			public void onClick(Widget sender)
			{
				TreeBox sedr = (TreeBox)sender;
				if(sedr.getValue()){
					mdx.addNode(sedr.getProperties());
				}else{
					mdx.removeNode(sedr.getProperties());
				}
				System.out.println(sedr.getProperties().getMemberName());
			}
		};
		
        TreeNode curr_node;
        // initWidget(tree);
        
		//For each axes in cellset array
		for(int i=0;i<cellSetTab.length;i++){
			
			int lvl = 0;//current depth
			int[] lvlArray;//number of children to add in this depth
			int[] posArray;
			int lvlPos[];//Position of the depth in the data set
			boolean leaf = false;
			
			
			CellSetAxis curr_csa = cellSetTab[i].getAxis(1);
			int nbc = cellSetTab[i].getAxis(1).getMemberCount(0);
			
			curr_node = outerRoot;
			
			for(int j=0;j<nbc;j++){
			
				Member curr_mem = curr_csa.getMember(0,j);
				MemberProperties curr_prop = curr_mem.getProperties();
				int lvlNum = curr_prop.getLevelNumber();
				int parLvl = curr_prop.getParentLevel();
				int cCard = curr_prop.getChildrenCardinality();
				
				//New node
				TreeNode tmp_trn = new TreeNode(curr_prop,listener);
				//add event handler
				
				
				//Ajout du nouveau noeud
				if(j==0){
					outerRoot.addItem(tmp_trn);
					curr_node = tmp_trn;
				}
				else{
					//on descend encore -> changement de noeud courant
					if(cCard > 0){

						if(lvlNum <= curr_node.getProperties().getLevelNumber()){
							TreeNode tmp = getParentCustom(curr_node,curr_node.getProperties().getLevelNumber()-lvlNum);
							tmp.addItem(tmp_trn);
							curr_node = tmp_trn;
						}else{
							curr_node.addItem(tmp_trn);
							curr_node = tmp_trn;
						}
					}
					//on reste au même niveau?
					else{
						curr_node.addItem(tmp_trn);
					}
				}
			}
		}
		outerRoot.setState(true);
		System.out.println("Tree Done");
	}
	
	private TreeNode getParentCustom(TreeNode curr,int i){
		
		TreeNode tmp_node = (TreeNode)(curr.getParentItem());
		
		if(i==0){
			return tmp_node;
		}
		else{
			return getParentCustom(tmp_node,i-1);
		}
		
	}
	
	public void init(){
	
	}

	@Override
	public void update(){
			
	}

	@Override
	public void update(Object o){

		OlapAjax oa = (OlapAjax)o;
		cellSetTab[cellCount] = oa.getCellSet();
		System.out.println("c count "+cellCount);
		if(cellCount+1==memberCount){
			System.out.println("treegen asked  "+cellCount);
			treeGen();
		}
		else{
			cellCount++;
		}
		
	}
	
	public Tree getTree(){
		return this.tree;
	}
	
	public MDXQuery getMDXObject(){
	System.out.println("getMDXObject#135");
		return this.mdx;
	}
	
	//get the cellset -> $wmd.cs 
	public native CellSet getCellSet() /*-{ return $wnd.cs[0] }-*/;

}

