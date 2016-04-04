package fr.ups.mdl.iaws.projectIAWS;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ServiceRisqueChaussee {
	
	public float risqueChaussee(String adresse) throws ParserConfigurationException, SAXException, IOException{
		
		List<Float> coordonnees = AccesOSM.calculCoordonnees(adresse);
		return AccesOWM.calculRisque(coordonnees);
	}
}
