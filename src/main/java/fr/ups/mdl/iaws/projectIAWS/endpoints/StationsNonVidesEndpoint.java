package fr.ups.mdl.iaws.projectIAWS.endpoints;

import fr.ups.mdl.iaws.projectIAWS.ServiceVelib;
import fr.ups.mdl.iaws.projectIAWS.XmlHelper;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Endpoint
public class StationsNonVidesEndpoint {
	private ServiceVelib serviceVelib;

	private static final String NAMESPACE_URI = "http://stationsNonVides/ws";

	@Autowired
	public StationsNonVidesEndpoint(ServiceVelib serviceVelib) {
		this.serviceVelib = serviceVelib;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "StationsNonVidesRequest") // nom de l'élément racine
	@Namespace(prefix = "rn", uri = NAMESPACE_URI) // espace de nom pour les expressions XPath ci-dessous
	@ResponsePayload
	public Element handleStationsNonVidesRequest(
			@XPathParam("/rn:StationsNonVidesRequest/rn:adresse/text()") String adresse)
			throws Exception {

		// Invoque le service "releveNoteService" pour récupérer les objets recherchés :
		ArrayList<Station> stations = serviceVelib.stationsNonVides(adresse);

		// Creation du DOM builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    final DocumentBuilder builder = factory.newDocumentBuilder();       
	    final Document document= builder.parse(new File("StationsNonVides.xml"));
	    
	    // Modification des noeuds dans le document XML
	    Node noeudStation;
	    NodeList noeudsEnfantsStation;
	    Element elementNoeudEnfant;
	    Station station;
	    
	    for (int i = 0; i < stations.size(); i++) {
	    	station = stations.get(i);
	    	noeudStation = document.getElementsByTagName("station").item(i);
	    	noeudsEnfantsStation = noeudStation.getChildNodes();
	    	
	    	for (int j = 0; j < noeudsEnfantsStation.getLength(); j++) {
	    		elementNoeudEnfant = (Element)noeudsEnfantsStation.item(j);
	    		if ("adresse".equals(elementNoeudEnfant.getNodeName())) {
	    			elementNoeudEnfant.setTextContent(station.getAdresse());
	    		}
	    		else if ("nombreVelos".equals(elementNoeudEnfant.getNodeName())) {
	    			elementNoeudEnfant.setTextContent(station.getNombreVelos());
	    		}
	    	}
	    }
		
		// Envoi du document XML en reponse
		final Element racine = document.getDocumentElement();
		return racine;
	}
}
