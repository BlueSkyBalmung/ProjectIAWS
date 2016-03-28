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

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
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
	private String cleJCDecaux="039a8fcb1cfb47bcaa20e9ed00f0f07f64bff95e";
	private static final String API_URI =  "https://api.jcdecaux.com/vls/v1/stations?contract=Toulouse&apiKey=039a8fcb1cfb47bcaa20e9ed00f0f07f64bff95e";
	public void stationNonVide(String adresse){
		try {
		
			//JCDECAUX
			Client clientJCDecaux = ClientBuilder.newClient();
			WebTarget targetJCDecaux = clientJCDecaux.target(API_URI);
		
			List<Float> l=accessOSM(adresse);
			
		
			//ARCGIS
			//ex ArcGis request :
			//http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths?sr=4269&polylines=[{"paths":[[[-117,34],[-116,34],[-117,33]],[[-115,44],[-114,43],[-115,43]]]},{"paths":[[[32.49,17.83],[31.96,17.59],[30.87,17.01],[30.11,16.86]]]}]&lengthUnit=9036&calculationType=preserveShape
			Client clientaArcGIS = ClientBuilder.newClient();
			WebTarget arcGIStarget = clientaArcGIS.target("http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths");
			MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
			formData.add("sr", "4269");
	
			
			JsonArray resultJCDecaux = targetJCDecaux.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
			JsonArray closer;
			int lat, lng;
			
			for(int i=0;i<resultJCDecaux.size();i++){
				JsonObject ite=resultJCDecaux.getJsonObject(i);

				lat=ite.getInt("lat");
				lng=ite.getInt("lng");
				
				formData.add("", "");
				
			}
			
			
		    formData.add("lengthUnit", "9036");
		    formData.add("calculationType", "preserveShape");
		    Response response = arcGIStarget.request().post(Entity.form(formData));
		
			//String pageName = obj.getJsonObject();
		}catch (InternalServerErrorException |ParserConfigurationException |SAXException |IOException e) {
			// e.printStackTrace();
			System.err.println("Réponse HTTP " + e.toString());
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
				
				return l;
				
	}
	
	public void stationNonComplete(String adresse){
		
	}
	
	
}
