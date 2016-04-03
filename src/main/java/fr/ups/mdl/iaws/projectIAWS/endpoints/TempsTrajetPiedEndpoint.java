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

		// Invoque le service "releveNoteService" pour récupérer les objets recherchés :
		int temps = serviceVelib.tempsTrajetPied(adresseDepart, adresseArrivee, vitesseDeplacement);

		// Transforme en élément XML ad-hoc pour le retour :
		// Ici, on prend le parti de renvoyer un fichier XML statique.
		// Il faudrait traiter la liste des évaluations avec une API XML pour
		// fournir l'élément réponse de manière dynamique
		Element elt = XmlHelper.getRootElementFromFileInClasspath("TempsTrajetPied.xml");
		return elt;
	}
}
