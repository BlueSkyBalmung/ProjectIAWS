package fr.ups.mdl.iaws.projectIAWS;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public abstract class AccesOSM {

	public static List<Float> calculCoordonnees(String adresse) throws ParserConfigurationException, SAXException, IOException{
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
		String lat = place.getAttribute("lat");
		String lng = place.getAttribute("lon");
		List<Float> coordonnees = new ArrayList<Float>();
		coordonnees.add(Float.valueOf("lat"));
		coordonnees.add(Float.valueOf("lon"));
		
		return coordonnees;
	}
}
