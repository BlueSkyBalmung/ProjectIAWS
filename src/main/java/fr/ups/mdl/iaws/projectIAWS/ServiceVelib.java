/**
 * 
 */
package fr.ups.mdl.iaws.projectIAWS;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * @author 21002269
 *
 */
public class ServiceVelib {
	private String cleJCDecaux="039a8fcb1cfb47bcaa20e9ed00f0f07f64bff95e";
	private static final String API_URI =  "https://api.jcdecaux.com/vls/v1/stations?contract=Toulouse&apiKey=039a8fcb1cfb47bcaa20e9ed00f0f07f64bff95e";
	public void stationNonVide(String adresse){
		String requeteOSM="http://nominatim.openstreetmap.org/search.php?q="+adresse+"&countrycodes=fr&limit=1&format=json";
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(API_URI);
		try {
			JsonArray result = target.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
			JsonArray closer;
			int lat, lng;
			for(int i=0;i<result.size();i++){
				JsonObject ite=result.getJsonObject(i);

				lat=ite.getInt("lat");
				lng=ite.getInt("lng");
				
			}
		
			//String pageName = obj.getJsonObject();
		}catch (InternalServerErrorException e) {
			// e.printStackTrace();
			System.err.println("Réponse HTTP " + e.getResponse().getStatus());
		}
		

	}
	public void stationNonComplete(String adresse){
		
	}
	
	
}
