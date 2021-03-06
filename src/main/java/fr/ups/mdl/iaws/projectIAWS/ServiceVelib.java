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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.ByteArrayInputStream;
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
import javax.json.JsonReader;
import javax.json.JsonString;
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
	private static String cleJCDecaux = "039a8fcb1cfb47bcaa20e9ed00f0f07f64bff95e";
	private static final String API_URI = "https://api.jcdecaux.com/vls/v1/stations?contract=Toulouse&apiKey=";
	
	public ArrayList<Station> stationsNonVides(String adresse){
		try {
		
			//JCDECAUX 
			Client clientJCDecaux = ClientBuilder.newClient();
			WebTarget targetJCDecaux = clientJCDecaux.target(API_URI+cleJCDecaux+"&format=XML");
		
			List<Float> coordonnees = accessOSM(adresse);
			
			//ARCGIS
			//ex ArcGis request :
			//http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths?sr=4269&polylines=[{"paths":[[[-117,34],[-116,34],[-117,33]],[[-115,44],[-114,43],[-115,43]]]},{"paths":[[[32.49,17.83],[31.96,17.59],[30.87,17.01],[30.11,16.86]]]}]&lengthUnit=9036&calculationType=preserveShape
			Client clientaArcGIS = ClientBuilder.newClient();
			WebTarget arcGIStarget = clientaArcGIS.register(JsonContentTypeResponseFilter.class)
					.target("http://sampleserver6.arcgisonline.com/ArcGIS/rest/services/Utilities/Geometry/GeometryServer/lengths");
			MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
			formData.add("sr", "4269");
	
			String resultJCDecauxS = targetJCDecaux.request(MediaType.APPLICATION_JSON).get(String.class);
			JsonReader result=Json.createReader(new ByteArrayInputStream(resultJCDecauxS.getBytes()));

			/*JsonObjectBuilder resultJCDecauxBuild =Json.createObjectBuilder();
			JsonObject resultJCDecauxObject=resultJCDecauxBuild.add("tab", resultJCDecauxS).build();*/
			//resultJCDecauxBuild.;
			JsonArray resultJCDecaux=result.readArray();

			JsonArray closer;

			float lat, lng;
			JsonArrayBuilder polylines = Json.createArrayBuilder();
			for(int i=0;i<resultJCDecaux.size();i++){
				JsonObject ite=resultJCDecaux.getJsonObject(i);
				//available_bikes
				if (ite.getInt("available_bikes")>0){
					
					lat=ite.getJsonObject("position").getJsonNumber("lat").bigDecimalValue().floatValue();
					lng=ite.getJsonObject("position").getJsonNumber("lng").bigDecimalValue().floatValue();
					JsonObjectBuilder oneAssert=Json.createObjectBuilder();

					polylines.add(Json.createObjectBuilder().
						add("path", "[[["+coordonnees.get(0).toString()+","+coordonnees.get(1).toString()+"],["+String.valueOf(lat)+","+String.valueOf(lat)+"]]]").build());

				}
			}
			
			formData.add("polylines", polylines.build().toString());
		    formData.add("lengthUnit", "9036");
		    formData.add("calculationType", "preserveShape");
		    String responseArcGISString = arcGIStarget.request(MediaType.APPLICATION_JSON)
		    		.post(Entity.form(formData),String.class);
		    JsonReader readerArcGIS=Json.createReader(new ByteArrayInputStream(responseArcGISString.getBytes()));
		    JsonObject responseArcGIS = readerArcGIS.readObject();
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
		    
		    ArrayList<Station> stations = new ArrayList<Station>();
			stations.add(new Station(resultJCDecaux.getJsonObject((int)answer[0][0]).getString("address")
					, resultJCDecaux.getJsonObject((int)answer[0][0]).getInt("available_bike_stands")
					, resultJCDecaux.getJsonObject((int)answer[0][0]).getInt("available_bikes")));
			stations.add(new Station(resultJCDecaux.getJsonObject((int)answer[1][0]).getString("address")
					, resultJCDecaux.getJsonObject((int)answer[1][0]).getInt("available_bike_stands")
					, resultJCDecaux.getJsonObject((int)answer[1][0]).getInt("available_bikes")));
			stations.add(new Station(resultJCDecaux.getJsonObject((int)answer[2][0]).getString("address")
					, resultJCDecaux.getJsonObject((int)answer[2][0]).getInt("available_bike_stands")
					, resultJCDecaux.getJsonObject((int)answer[2][0]).getInt("available_bikes")));
			return stations;
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
				String requeteOSM = "http://nominatim.openstreetmap.org/search?q="+adresse.replace(" ","+")
				+",+Toulouse&format=xml&polygon=1&addressdetails=1";
				Client clientOSM = ClientBuilder.newClient();
				WebTarget targetOSM= clientOSM.target(requeteOSM);
				InputStream refFic=targetOSM.request(MediaType.APPLICATION_XML).get(InputStream.class);
				
				//partie parse XML avec DOM
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new DataInputStream(refFic));
				Element root = doc.getDocumentElement();
				NodeList nList = root.getElementsByTagName("place");
				Element place = (Element)nList.item(0);
				Float lat= Float.parseFloat(place.getAttribute("lat"));
				Float lng = Float.parseFloat(place.getAttribute("lon"));
				List<Float> coord=new ArrayList<Float>();
				coord.add(lat);
				coord.add(lng);
				return coord;
				
	}
	
	public float risqueChausse(String adresse) {
		return 0;
	}
	
	
	public ArrayList<Station> stationsNonCompletes(String adresse) {
		return null;
	}
}
