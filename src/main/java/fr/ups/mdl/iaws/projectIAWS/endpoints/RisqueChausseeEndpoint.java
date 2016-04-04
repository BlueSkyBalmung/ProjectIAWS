package fr.ups.mdl.iaws.projectIAWS.endpoints;

import fr.ups.mdl.iaws.projectIAWS.ServiceRisqueChaussee;

import java.io.File;
import java.io.IOException;

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
import org.xml.sax.SAXException;

@Endpoint
public class RisqueChausseeEndpoint {
	private ServiceRisqueChaussee serviceRisqueChausee;

	private static final String NAMESPACE_URI = "http://risqueChaussee/ws";

	@Autowired
	public RisqueChausseeEndpoint(ServiceRisqueChaussee serviceRisqueChausee) {
		this.serviceRisqueChausee = serviceRisqueChausee;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "RisqueChausseeRequest") // nom de l'élément racine
	@Namespace(prefix = "rn", uri = NAMESPACE_URI) // espace de nom pour les expressions XPath ci-dessous
	@ResponsePayload
	public Element handleStationsNonVidesRequest(
			@XPathParam("/rn:RisqueChausseeRequest/rn:adresse/text()") String adresse)
			throws Exception, ParserConfigurationException, SAXException, IOException {

		float risque = serviceRisqueChausee.risqueChaussee(adresse);

		// Creation du DOM builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    final DocumentBuilder builder = factory.newDocumentBuilder();
	    final Document document= builder.parse(new File("RisqueChaussee.xml"));
	    
	    // Modification des noeuds dans le document XML
		final Element elementRisque = (Element)document.getElementsByTagName("risque").item(0);
		elementRisque.setTextContent(String.valueOf(risque));
		
		// Envoi du document XML en reponse
		final Element racine = document.getDocumentElement();
		return racine;
	}
}