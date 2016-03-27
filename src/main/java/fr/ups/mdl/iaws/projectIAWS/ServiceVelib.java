/**
 * 
 */
package fr.ups.mdl.iaws.projectIAWS;
import javax.ws.rs.core.MultivaluedMap;
import com.sun.jersey.api.client.WebResource;
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
		
		String requestArcGIS = "http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths?sr=4269&polylines=[{"paths":[[[-117,34],[-116,34],[-117,33]],[[-115,44],[-114,43],[-115,43]]]},{"paths":[[[32.49,17.83],[31.96,17.59],[30.87,17.01],[30.11,16.86]]]}]&lengthUnit=9036&calculationType=preserveShape";
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(API_URI);
		try {
			JsonArray result = target.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
			JsonArray closer;
			int lat, lng;
			MultivaluedMap formData = new MultivaluedMapImpl();
			for(int i=0;i<result.size();i++){
				JsonObject ite=result.getJsonObject(i);

				lat=ite.getInt("lat");
				lng=ite.getInt("lng");
				
				formData.add("", "");
				
			}
			
			
			formData.add("name1", "val1");
			formData.add("name2", "val2");
			ClientResponse response = 
			        webResource.type(MediaType.
			                         APPLICATION_FORM_URLENCODED_TYPE)
			                         .post(ClientResponse.class, formData);
		
			//String pageName = obj.getJsonObject();
		}catch (InternalServerErrorException e) {
			// e.printStackTrace();
			System.err.println("Réponse HTTP " + e.getResponse().getStatus());
		}
		

	}
	public void stationNonComplete(String adresse){
		
	}
	
	
}
