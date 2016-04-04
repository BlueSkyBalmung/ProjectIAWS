package fr.ups.mdl.iaws.projectIAWS;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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

public abstract class AccesOWM {

	private static String cleOWM = "a6466eebbd90007aec69064f3e4aea12";
	
	public static float calculRisque(List<Float> coordonnees) throws ParserConfigurationException, SAXException, IOException{
		
		final String requeteOWM = "api.openweathermap.org/data/2.5/weather?lat="+coordonnees.get(0).toString()+"&lon="+coordonnees.get(1).toString()+"&APPID="+cleOWM;
		Client clientOWM = ClientBuilder.newClient();
		WebTarget targetOWM = clientOWM.target(requeteOWM);
		InputStream refFic = targetOWM.request(MediaType.APPLICATION_XML).get(InputStream.class);
		
		//partie parse XML avec DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new DataInputStream(refFic));
		
		// récupération des precipitations
		Element precipitation = (Element)doc.getElementsByTagName("precipitation").item(0);
		String modePrecipitation = precipitation.getAttribute("mode");
		
		// calcul du risque
		float risque;
		if ("no".equals(modePrecipitation)) {
			risque = 0f;
		}
		else {
			int valeurPrecipitation = Integer.parseInt(precipitation.getAttribute("value"));
			if (valeurPrecipitation < 50) risque = 0.5f;
			else risque = 1f;
		}
		
		return risque;
	}
}
