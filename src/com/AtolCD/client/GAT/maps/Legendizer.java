 /**
 * Legendizer.java
 *
 * Legendizer creates gradients in order to prepare legend
 *
 * Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
 * See LICENSE file under this distribution for licensing information
 * 
 * @author vpl
 * @version %I%, %G%
 * @since 20100322 
 **/
 
 package com.AtolCD.client.GAT.maps;

//Openlayers-gwt imports
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.Style;
import com.AtolCD.client.GAT.utils.OlapConfig;

public class Legendizer{

	//Configuration variables
	private int numberOfIntervals;
	private String colMin, colMax;

	private VectorFeature[] featurestore;
	private double[] values;
	private double min, max;
	private double[] vales;
	private OlapConfig olcf;
	
	/* 	* * Methodes de calcul
		0 - Linear
		1 - Quadratique
		2 - Logarithmique
	*/
	private int method;
	
	String[] grad;
	String[] gradient;
	
	public Legendizer(String a, String b){
		this.method = 0;
		colMin = a;
		colMax = b;
	}
	
	public Legendizer(double[] fea){
		makeColors(fea);
	}
	
	public void reMake(double[] fea){
		makeColors(fea);
	}

	public Legendizer(VectorFeature[] features){
		this.featurestore = features;
		this.values= new double[this.featurestore.length];
	}
	
	public void setFeatures(VectorFeature[] features){
		this.featurestore = features;	
	}
	
	public void setMethod(int value){
		this.method = value;
	}

	//prepares colors gradient
	public String[] makeColors(double[] fea){

		String[] cols = new String[fea.length];

		setMinMax(fea);
		this.numberOfIntervals = 6;
		int intervals = this.numberOfIntervals;
		int n = 3;

		double tmp = Math.round(this.max-this.min);
		vales = new double[intervals];
		double pas = Math.round((this.max-this.min)/intervals);

		for (int i = 0 ; i < intervals ; i++) {
			double tmp_val = 0.0;
			
			if(this.method == 0){
				tmp_val = this.min+(i*pas);
			}
			if(this.method == 1){
				tmp_val = Math.floor(Math.round(this.min)+(Math.round((tmp)*Math.pow(i,n))/(Math.pow(intervals,n))));
			}
			if(this.method == 2){
				tmp_val = Math.floor(Math.round(this.min)+(Math.round((tmp)*Math.log(i))/(Math.log(intervals))));
			}
			vales[i]=tmp_val;
		}

		this.grad = makeLinearColorGradient(this.colMin,this.colMax,intervals);

		return this.grad;
	}
	
	public void setColors(String min, String max){
		this.colMin = min;
		this.colMax = max;
		
	}
	
	private void setMinMax(double[] low){
		
		this.min = low[0];
		this.max = low[0];
		
		for(int i =0;i<low.length;i++){
		
			this.min = Math.min(this.min,low[i]);
			this.max = Math.max(this.max,low[i]);
		
		}
		System.out.println("min : "+this.min);
		System.out.println("max : "+this.max);
	}
	
	private String[] makeLinearColorGradient(String minColor, String maxColor,int intervals) {
	
		int[] minRgb = HTMLtoRGB(minColor);
		int[] maxRgb = HTMLtoRGB(maxColor);
		
		gradient = new String[intervals];
		
		for (int i = 0 ; i < intervals ; i++) {
		
			double intervals2  = (double)intervals;
			double i2  = (double)i;
			double pos = i2/(intervals2-1.0);
			int[] intervalRgb = new int[3];
			
			intervalRgb[0] = (int)(Math.floor(minRgb[0]*(1.0-pos) + maxRgb[0] * pos));	
			intervalRgb[1] = (int)(Math.floor(minRgb[1]*(1.0-pos) + maxRgb[1] * pos));
			intervalRgb[2] = (int)(Math.floor(minRgb[2]*(1.0-pos) + maxRgb[2] * pos));
			
			gradient[i] = RGBtoHTML(intervalRgb);
		}

		return gradient;
	}
	
	public String getColor(double value){
	
		double val = Math.ceil(value);
		String color = this.grad[getIntervalIdx(val)];
		
		return color;
	}
	
	/**
	*
	@param String html String representing HTML color such as #FF00FF including # character
	*
	@return int[] rgb Colour as an array of Red-Green-Blue codes
	**/
	private int[] HTMLtoRGB(String html){

		int[] rgb = new int[3];
		
		rgb[0] = Integer.parseInt(html.substring(1,3),16);
		rgb[1] = Integer.parseInt(html.substring(3,5),16);
		rgb[2] = Integer.parseInt(html.substring(5,7),16);

		return rgb;
	}
	
	private String RGBtoHTML(int[] rgb){
	
		String sr[] = new String[3];
		sr[0] = Integer.toHexString(rgb[0]);
		sr[1] = Integer.toHexString(rgb[1]);
		sr[2] = Integer.toHexString(rgb[2]);
	
	
		for(int y =0;y<sr.length;y++){
			if(sr[y].length()!=2){sr[y]="0"+sr[y];}
		}
		
		StringBuffer hexstr = new StringBuffer("#").append(sr[0]).append(sr[1]).append(sr[2]);
		
		return hexstr.toString();
	}
	
	private int getIntervalIdx(double value) {
		double theValue = value;

		for (int h = 0 ; h < this.vales.length ; h++) {
				
				if(theValue<this.vales[h]){
					int j = (h==0)?(0):(h-1);
					return j;
				}
			}
		return (this.vales.length-1);
	}
	
	public String[] getGrad(){
		return this.grad;
	}
	
	public double getMin(){
		return this.min;
	}
	
	public double getMax(){
		return this.max;
	}
	
	public int getStepNb(){
		return this.numberOfIntervals;
	}
	public double[] getVales(){
		return this.vales;
	}

}