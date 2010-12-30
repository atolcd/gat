<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="saxbean.*" %>
<%@ page import="org.xml.sax.*" %>

<jsp:useBean id="saxparser" 
class="saxbean.MySAXParserBean" />

<%
String pt = application.getContextPath();

java.util.Properties property = null;

String propFile = ".\\webapps\\"+pt.substring(1)+"\\gat.properties";
FileInputStream fis = new FileInputStream(propFile) ;
Properties gat_props =new Properties();
gat_props.load(fis);

String message = gat_props.getProperty("catalog.path");

// Collection stocks = saxparser.parse("C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\Spatialytics\\WEB-INF\\aguram.xml");
Collection stocks = saxparser.parse(message);
Vector vect = (Vector)stocks;
Iterator ir = stocks.iterator();
int st = 0;
boolean first = true;
String t_gm = "";
String dime = "{";

dime += "\"Cubes\": [";

for(int y=0;y<vect.size();y++){
  MyElement element = (MyElement) vect.get(y); 
  String tag = element.getQname();
  String name = element.getAttributesI().getValue("name");

  
if(tag.equals("Cube")){

if(!first){
	dime = dime.substring(0,dime.length()-1);
	dime += "]},";
}
first = false;
	dime += "{ \"name\":\""+name+"\",";
	dime += "\"Dimensions\" : [";

  }

	

	if(tag.equals("Dimension")){
	String curr_dim="{\"name\":\""+name+"\",\"Hierarchies\":[";
	
	for(int p=1;p<vect.size()-y;p++)
	{
		MyElement elt = (MyElement) vect.get(y+p); 

		if("Dimension".equals(elt.getQname())){break;}
		else if("Hierarchy".equals(elt.getQname())){
		String allmn = elt.getAttributesI().getValue("allMemberName");
		String t_name = elt.getAttributesI().getValue("name");
		curr_dim+="{\"allmn\":\""+allmn+"\",\"name\":\"";
		curr_dim+=t_name;
		curr_dim+="\"},";
		}
		else if("Property".equals(elt.getQname())){
			String b = ""+st;
			if("Geometry".equals(elt.getAttributesI().getValue("type"))){t_gm+=(t_gm.equals(""))?b:"";}
				
		}
	}
	
	curr_dim = curr_dim.substring(0,curr_dim.length()-1);
	curr_dim+="]";
	dime+=curr_dim;
	dime+="},";
	st++;
	
	
}  
}
	dime = dime.substring(0,dime.length()-1);
	dime +="]}]";
	dime+=",\"geom\":\""+t_gm+"\"";
	dime+=",\"jpivot_url\":\""+gat_props.getProperty("jpivot_url")+"\"";
	dime+=",\"base_min_col\":\""+gat_props.getProperty("gat.color.min")+"\"";
	dime+=",\"base_max_col\":\""+gat_props.getProperty("gat.color.max")+"\"";
	dime+=",\"hover_color\":\""+gat_props.getProperty("gat.color.hover")+"\"";
	dime+=",\"wms_layer_url\":\""+gat_props.getProperty("gat.wms.url")+"\"";
	dime+=",\"wms_layer_format\":\""+gat_props.getProperty("gat.wms.format")+"\"";
	dime+=",\"wms_layer_layer\":\""+gat_props.getProperty("gat.wms.layer")+"\"";
	dime+=",\"map_center_lat\":\""+gat_props.getProperty("gat.map.initlat")+"\"";
	dime+=",\"map_center_lon\":\""+gat_props.getProperty("gat.map.initlon")+"\"";
	dime+=",\"map_center_zoom\":\""+gat_props.getProperty("gat.map.initzoom")+"\"";
	
	dime+=",\"map_fill_opacity\":\""+gat_props.getProperty("gat.map.fillopacity")+"\"";
	dime+=",\"map_font_color\":\""+gat_props.getProperty("gat.map.fontcolor")+"\"";
	dime+=",\"map_font_family\":\""+gat_props.getProperty("gat.map.fontfamily")+"\"";
	dime+=",\"map_font_size\":\""+gat_props.getProperty("gat.map.fontsize")+"\"";
	dime+=",\"map_stroke_color\":\""+gat_props.getProperty("gat.map.stroke.color")+"\"";
	dime+=",\"map_stroke_hovercolor\":\""+gat_props.getProperty("gat.map.stroke.hovercolor")+"\"";

	dime+=",\"base_intervals\":\""+gat_props.getProperty("gat.map.intervals")+"\"";
	
	//dime+="\"user\" : " + file_content;
	dime+="}";
%>

<%= dime %>