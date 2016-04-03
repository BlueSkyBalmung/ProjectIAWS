package fr.ups.mdl.iaws.projectIAWS.endpoints;

import fr.ups.mdl.iaws.projectIAWS.ServiceVelib;
import fr.ups.mdl.iaws.projectIAWS.XmlHelper;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

@Endpoint
public class StationsNonCompletesEndpoint {
	private ServiceVelib serviceVelib;

	private static final String NAMESPACE_URI = "http://stationsNonCompletes/ws";

	@Autowired
	public StationsNonCompletesEndpoint(ServiceVelib serviceVelib) {
		this.serviceVelib = serviceVelib;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "StationsNonCompletesRequest") // nom de l'élément racine
	@Namespace(prefix = "rn", uri = NAMESPACE_URI) // espace de nom pour les expressions XPath ci-dessous
	@ResponsePayload
	public Element handleStationsNonVidesRequest(
			@XPathParam("/rn:StationsNonCompletesRequest/rn:adresse/text()") String adresse)
			throws Exception {

		// Invoque le service "releveNoteService" pour récupérer les objets recherchés :
		ArrayList<Station> stations = serviceVelib.stationsNonCompletes(adresse);

		// Creation du DOM builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    final DocumentBuilder builder = factory.newDocumentBuilder();       
	    final Document document= builder.parse(new File("StationsNonCompletes.xml"));
	    
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
	    		else if ("nombrePlaces".equals(elementNoeudEnfant.getNodeName())) {
	    			elementNoeudEnfant.setTextContent(station.getNombrePlaces());
	    		}
	    	}
	    }
		
		// Envoi du document XML en reponse
		final Element racine = document.getDocumentElement();
		return racine;
	}
}
