/*package iaws.relevenotes.ws.contractfirst;

import iaws.relevenotes.domain.Evaluation;
import iaws.relevenotes.domain.nomenclature.AnneeScolaire;
import iaws.relevenotes.domain.nomenclature.Niveau;
import iaws.relevenotes.domain.nomenclature.Semestre;
import iaws.relevenotes.services.ReleveNoteService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.Element;

*//**
 * @author Franck Silvestre
 *//*

@Endpoint
public class ReleveNotesEndpoint {

    private ReleveNoteService releveNotesService;

    private static final String NAMESPACE_URI = "http://iaws/ws/contractfirst/exemple";

    @Autowired
    public ReleveNotesEndpoint(ReleveNoteService releveNotesService) {
        this.releveNotesService = releveNotesService;
    }

    @PayloadRoot(localPart = "ReleveNotesRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    @Namespace(prefix = "s", uri = NAMESPACE_URI)
    public Element handleReleveNotesRequest(@XPathParam("/s:ReleveNotesRequest/@annee_scolaire") String anneeScol,
    		@XPathParam("/s:ReleveNotesRequest/@niveau") String niveauCode, @XPathParam("/s:ReleveNotesRequest/@semestre") Integer semestreId) throws Exception {

	// Parse le XML de ReleveNotesRequest pour extraire les informations de
	// l'année scolaire, du niveau et du semestre et créer les objets ad-hoc :
        AnneeScolaire anneeScolaire = new AnneeScolaire(anneeScol);
        Niveau niveau = new Niveau(niveauCode);
        Semestre semestre = new Semestre(semestreId);


	// Invoque le service "releveNoteService" pour récupérer les objets recherchés :
        List<Evaluation> evals = releveNotesService.findAllEvaluationsForAnneeScolaireNiveauAndSemestre(anneeScolaire, niveau, semestre);

	// Transforme en élément XML ad-hoc pour le retour :
	// Ici, on prend le parti de renvoyer un fichier XML statique.
	// Il faudrait traiter la liste des évaluations avec une API XML pour
	// fournir l'élément réponse de manière dynamique
        Element elt = XmlHelper.getRootElementFromFileInClasspath("ReleveNotes.xml") ;
        return  elt;
    }
}*/
