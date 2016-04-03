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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Endpoint
public class TempsTrajetPiedEndpoint {
	private ServiceVelib serviceVelib;

	private static final String NAMESPACE_URI = "http://tempsTrajetPied/ws";

	@Autowired
	public TempsTrajetPiedEndpoint(ServiceVelib serviceVelib) {
		this.serviceVelib = serviceVelib;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "TempsTrajetPiedRequest") // nom de l'élément racine
	@Namespace(prefix = "rn", uri = NAMESPACE_URI) // espace de nom pour les expressions XPath ci-dessous
	@ResponsePayload
	public Element handleStationsNonVidesRequest(
			@XPathParam("/TempsTrajetPiedRequest/rn:adresseDepart/text()") String adresseDepart,
			@XPathParam("/TempsTrajetPiedRequest/rn:adresseArrivee/text()") String adresseArrivee,
			@XPathParam("/TempsTrajetPiedRequest/rn:vitesseDeplacement/text()") String vitesseDeplacement)
			throws Exception {

		int temps = serviceVelib.tempsTrajetPied(adresseDepart, adresseArrivee, vitesseDeplacement);

		// Creation du DOM builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    final DocumentBuilder builder = factory.newDocumentBuilder();       
	    final Document document= builder.parse(new File("TempsTrajetPied.xml"));
	    
	    // Modification des noeuds dans le document XML
		final Element elementTempsTrajetPied = (Element)document.getElementsByTagName("tempsTrajetPied").item(0);
		elementTempsTrajetPied.setTextContent(String.valueOf(temps));
		
		// Envoi du document XML en reponse
		final Element racine = document.getDocumentElement();
		return racine;
	}
}
