/**
 * 
 */
package fr.ups.mdl.iaws.projectIAWS;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;

/**
 * @author 21002269
 *
 */
public class ServiceVelib {
	private static String cleJCDecaux="039a8fcb1cfb47bcaa20e9ed00f0f07f64bff95e";
	private static final String API_URI =  "https://api.jcdecaux.com/vls/v1/stations?contract=Toulouse&apiKey="+cleJCDecaux;
	public ArrayList<Station> stationsNonVides(String adresse){
		try {
		
			//JCDECAUX 
			Client clientJCDecaux = ClientBuilder.newClient();
			WebTarget targetJCDecaux = clientJCDecaux.target(API_URI);
		
			List<Float> l=accessOSM(adresse);
			
		
			//ARCGIS
			//ex ArcGis request :
			//http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths?sr=4269&polylines=[{"paths":[[[-117,34],[-116,34],[-117,33]],[[-115,44],[-114,43],[-115,43]]]},{"paths":[[[32.49,17.83],[31.96,17.59],[30.87,17.01],[30.11,16.86]]]}]&lengthUnit=9036&calculationType=preserveShape
			Client clientaArcGIS = ClientBuilder.newClient();
			WebTarget arcGIStarget = clientaArcGIS.register(JsonContentTypeResponseFilter.class).target("http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths");
			MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
			formData.add("sr", "4269");
	
			
			JsonArray resultJCDecaux = targetJCDecaux.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
			JsonArray closer;
			float lat, lng;
			JsonArrayBuilder polylines = Json.createArrayBuilder();
			for(int i=0;i<resultJCDecaux.size();i++){
				JsonObject ite=resultJCDecaux.getJsonObject(i);
				//available_bikes
				if (ite.getInt("available_bikes")>0){
					
					lat=Float.valueOf(ite.getJsonObject("position").getString("lat"));
					lng=Float.valueOf(ite.getJsonObject("position").getString("lng"));
					JsonObjectBuilder oneAssert=Json.createObjectBuilder();
				
					polylines.add(Json.createObjectBuilder().
						add("path", "[[["+l.get(0).toString()+","+l.get(1).toString()+"],["+ite.getString("lat")+","+ite.getString("lng")+"]]]").build());
				}
			}
			
			formData.add("polylines", polylines.toString());
		    formData.add("lengthUnit", "9036");
		    formData.add("calculationType", "preserveShape");
		    JsonObject responseArcGIS = arcGIStarget.request(MediaType.APPLICATION_JSON).post(Entity.form(formData),JsonObject.class);
		   
		    //A Debattre
		    //Explication : je recois un string de la forme [valFloat1,valFloat2,...] que je dois transformer en tableau de float pour le parcourir
		    float[][] answer=new float[3][2];
		    String[] parts = responseArcGIS.getString("lengths").replace("[", "").replace("]","").split(",");
		    float[] resp = new float[parts.length];
		    for (int i = 0; i < parts.length; ++i) {
		        float number = Float.parseFloat(parts[i]);
		        float rounded = (int) Math.round(number * 10000) / 10000f;
		        resp[i] = rounded;
		    }
		    answer[0][1]=resp[0]; answer[0][0]=(float) 0;
		    answer[1][1]=resp[1]; answer[1][0]=(float) 1;
		    answer[2][1]=resp[2]; answer[2][0]=(float) 2;
		    for(int i=3;i<resp.length;i++){
		    	float dist1=answer[0][1]-resp[i];
		    	float dist2=answer[1][1]-resp[i];
		    	float dist3=answer[2][1]-resp[i];
		    	
		    	if(dist1>dist2 && dist1>dist3 && dist1>0){
		    		answer[0][1]=resp[i]; answer[0][0]=(float) i;
		    	}else if(dist2>dist1 && dist2>dist3 && dist2>0){
		    		answer[1][1]=resp[i]; answer[1][0]=(float) i;
		    	}else if(dist3>dist1 && dist3>dist2 && dist3>0){
		    		answer[2][1]=resp[i]; answer[2][0]=(float) i;
		    	}
		    }
		    //Fin du "A Debattre"
		    
		    HashMap<String,Integer> hm=new HashMap<String,Integer>();
			hm.put(resultJCDecaux.getJsonObject((int)answer[0][0]).getString("name"),resultJCDecaux.getJsonObject(1).getInt("available_bikes"));
			hm.put(resultJCDecaux.getJsonObject((int)answer[1][0]).getString("name"),resultJCDecaux.getJsonObject(1).getInt("available_bikes"));
			hm.put(resultJCDecaux.getJsonObject((int)answer[2][0]).getString("name"),resultJCDecaux.getJsonObject(1).getInt("available_bikes"));
			return hm;
		}catch (InternalServerErrorException |ParserConfigurationException |SAXException |IOException e) {
			// e.printStackTrace();
			System.err.println("Réponse HTTP " + e.toString());
			return null;
		}
		
	}
	
	private List<Float> accessOSM(String adresse) throws ParserConfigurationException, SAXException, IOException{
		//OpenStreetMap partis
				//ex request :
				//http://nominatim.openstreetmap.org/search?q=Université+Paul+Sabatier,+Toulouse&format=xml
				//partie acces
				String requeteOSM = "http://nominatim.openstreetmap.org/search?q="+adresse.replace(" ","+")+",+Toulouse&format=xml&polygon=1&addressdetails=1";
				Client clientOSM = ClientBuilder.newClient();
				WebTarget targetOSM= clientOSM.target(requeteOSM);
				InputStream refFic=targetOSM.request(MediaType.APPLICATION_XML).get(InputStream.class);
				
				//partie parse XML avec DOM
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new DataInputStream(refFic));
				Element root = doc.getDocumentElement();
				Element place = (Element)root.getFirstChild();
				String lat= place.getAttribute("lat");
				String lng = place.getAttribute("lon");
				List<Float> l=new ArrayList<Float>();
				l.add(Float.valueOf("lat"));
				l.add(Float.valueOf("lon"));
				return l;
				
	}
	
	public void stationsNonCompletes(String adresse){
		
	}

	public ArrayList<Station> stationNonComplete(String adresse){
		return null;
	}

	public int tempsTrajetPied(String adresseDepart, String adresseArrivee, String vitesseDeplacement) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int tempsTrajetVelo(String adresseDepart, String adresseArrivee, String vitesseDeplacement) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
