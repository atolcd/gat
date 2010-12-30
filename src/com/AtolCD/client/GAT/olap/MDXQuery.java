/**
* MDXQuery.java
*
* Object representatino of a MDX Query
*
* Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
* See LICENSE file under this distribution for licensing information
* 
* @author vpl
* @version %I%, %G%
* @since 20101001
**/

package com.AtolCD.client.GAT.olap;

import java.util.ArrayList;

import com.AtolCD.client.GAT.utils.OlapConfig;
import com.AtolCD.client.GAT.utils.Config;

public class MDXQuery{

	ArrayList nl;
	String MDXQuery;
	String _head,_queue;
	private String _geomPart;
	private String _endPart;
	int _rowcard,_colcard,_filcard;
	private Config conf;
	
	ArrayList _filters[];
	String dims[];
	
	public MDXQuery(Config olcf){
			
		this.conf = olcf;
		
		MDXQuery = "";
		_geomPart = "";
		
		_rowcard = 0;
		_colcard = 0;
		_filcard = 0;

		nl = new ArrayList();
	}

	public String getQuery(ArrayList feats) {

		String[] features = (String[])feats.toArray(new String[feats.size()]);
		this.setOrds();
		this.handleGeom(features);
		this._regenQuery();
		return this.MDXQuery;
	}
	
	public MDXQuery getQueryObject(ArrayList feats) {

		String[] features = (String[])feats.toArray(new String[feats.size()]);
		this.setOrds();
		this.handleGeom(features);
		this._regenQuery();
		return this;
	}
	
	public String getQueryString() {
		return this.MDXQuery;
	}
	
	private void handleGeom(String[] feats){
		
		_endPart="";
		String geom_part ="";
		
		if(feats.length > 0){
			if(feats.length == 1){// une seule geom
				geom_part+="{"+feats[0]+"}";
			}
			else{
				if(feats.length >= 2){//We need to do an union
					for(int j=0;j< feats.length;j++){
						if(j<feats.length-1){
							geom_part+="Union({"+feats[j]+"},";
							_endPart+=")";
						}
						else{
							geom_part+="{"+feats[j]+"}";
						}		
					}
					geom_part+=_endPart;
				}
			}
		}
		
		this._geomPart = geom_part;
	}
	
	public void addNode(MemberProperties mp){
		this.nl.add(mp);
	}
 
	public void removeNode(MemberProperties mp){
		this.nl.remove(mp);
	}

	//Add a members to the filter
	private void addFilter(MemberProperties node,int ord) {
		this._filters[ord].add(node);
		this._filcard++;
	}
	 
	private void setOrds(){
	
		this.resetQuery();
		this._filters = new ArrayList[this.conf.getFilterCard()];
		System.out.println("order done : "+this.conf.getFilterCard());
		for(int y = 0;y<this._filters.length;y++){
			this._filters[y] = new ArrayList();
		}
		
		// this.dims = new String[this.conf.getFilterCard()];
		int order = 0;
		MemberProperties[] _vals = (MemberProperties[])(this.nl.toArray(new MemberProperties[nl.size()]));
		
		//pour chaque dim 
		boolean isDim = false;
		
		for(int m=0;m < this.conf.getCurrentCube().getDimensionsCount();m++){
			this._filcard=0;
			
			for(int j=0;j < _vals.length;j++){
				if(_vals[j].getDimensionUniqueName().equals("["+this.conf.getCurrentCube().getDimension(m).getName()+"]")){
					isDim = true;
							System.out.println("order asked : "+order);
					this.addFilter(_vals[j],order);
				}
			}
			if(isDim){
				order++;
				isDim = false;
			}
		}
	}

	private void resetQuery(){
		this._filcard = 0;	
	}

	private void _regenQuery(){
		String head = "";
		String queue = "";
		String request = " SELECT ";
		String par = "";
		
		boolean more_rows = false;

		request+=this._geomPart;

		if(this._filters!=null){
			head+="WITH";
			queue+=" WHERE (";

			for(int i=0;i < this._filters.length;i++){//each dimension used on rows

			MemberProperties[] intraFilters21 = new MemberProperties[this._filters[i].size()];
			Object[] olol = this._filters[i].toArray(intraFilters21);
			MemberProperties[] intraFilters = (MemberProperties[])(olol);

				String curr_dim = intraFilters[0].getDimensionUniqueName();

				head+=" MEMBER "+curr_dim+".[Aggreg] AS 'Aggregate({";
				
				if(intraFilters.length==1){//OK
					head+=intraFilters[0].getMemberUniqueName()+"})'";
				}
				else 
				{
					if(intraFilters.length>=2){
					for(int j=0;j < intraFilters.length;j++){
						
						if(j < intraFilters.length-1){
							head+=intraFilters[j].getMemberUniqueName()+",";
						}
						else{
							head+=intraFilters[j].getMemberUniqueName()+"})'";
						}		
					}
					}
				}

				if(((""+queue.charAt(queue.length()-1)).equals("("))==false){
					queue+=",";
				}
				queue+=curr_dim+".[Aggreg]";
			}

			queue+=")";
		}

		 request+=" ON ROWS, AddCalculatedMembers([Measures].Members) ON COLUMNS FROM "+this.conf.getCurrentCube().getName();

		this._head = head;
		this._queue = queue;
		
		request = head+request+queue;
		this.MDXQuery = request;

	}

/**
*
 * 
 * @param {String} geoMember Unique Name of the Geographique composed member
 * @param {String} olapNavOp String describing the olap opeartion to be used (drill down || roll up)
 *
 * @return {String} MDX request to be executed
 */
 
	public String olapNav(String geoMember, int olapNavOp){

		String tmpRequest = "SELECT ";
		
		//Make the olap nav op
		if(olapNavOp==0){
			this._geomPart = geoMember+".Children";
		}
		else if(olapNavOp==1){
			this._geomPart = geoMember+".Parent.Siblings";
		}

		//tmpRequest+="NON EMPTY "+this._geomPart;
		tmpRequest+=" "+this._geomPart;
		tmpRequest+=" ON ROWS, AddCalculatedMembers([Measures].Members) ON COLUMNS FROM "+this.conf.getCurrentCube().getName();
		tmpRequest = this._head+tmpRequest+this._queue;
		this.MDXQuery = tmpRequest;
		
		return tmpRequest;
	}


}