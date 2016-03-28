/**
 * 
 */
package fr.ups.mdl.iaws.projectIAWS;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

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
		//JCDECAUX
		Client clientJCDecaux = ClientBuilder.newClient();
		WebTarget targetJCDecaux = clientJCDecaux.target(API_URI);
		
		//OpenStreetMap partis
		//ex request :
		//http://nominatim.openstreetmap.org/search?q=Université+Paul+Sabatier,+Toulouse&format=xml
		String requeteOSM = "http://nominatim.openstreetmap.org/search?q="+adresse.replace(" ", "+")+",+Toulouse&format=xml";
		Client clientOSM = ClientBuilder.newClient();
		WebTarget targetOSM= clientOSM.target(requeteOSM);
		
		//ARCGIS
		//ex ArcGis request :
		//http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths?sr=4269&polylines=[{"paths":[[[-117,34],[-116,34],[-117,33]],[[-115,44],[-114,43],[-115,43]]]},{"paths":[[[32.49,17.83],[31.96,17.59],[30.87,17.01],[30.11,16.86]]]}]&lengthUnit=9036&calculationType=preserveShape
		Client clientaArcGIS = ClientBuilder.newClient();
		WebTarget arcGIStarget = clientaArcGIS.target("http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths");
	    MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
	    formData.add("sr", "4269");
		try {
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
		}catch (InternalServerErrorException e) {
			// e.printStackTrace();
			System.err.println("Réponse HTTP " + e.getResponse().getStatus());
		}
		

	}
	public void stationNonComplete(String adresse){
		
	}
	
	
}
