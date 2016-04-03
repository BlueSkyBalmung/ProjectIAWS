package fr.ups.mdl.iaws.projectIAWS.endpoints;

import fr.ups.mdl.iaws.projectIAWS.ServiceVelib;
import fr.ups.mdl.iaws.projectIAWS.XmlHelper;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.Element;

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
		HashMap<String,Integer> stations = serviceVelib.stationNonCompletes(adresse);

		// Transforme en élément XML ad-hoc pour le retour :
		// Ici, on prend le parti de renvoyer un fichier XML statique.
		// Il faudrait traiter la liste des évaluations avec une API XML pour
		// fournir l'élément réponse de manière dynamique
		Element elt = XmlHelper.getRootElementFromFileInClasspath("StationsNonCompletes.xml");
		return elt;
	}
}
