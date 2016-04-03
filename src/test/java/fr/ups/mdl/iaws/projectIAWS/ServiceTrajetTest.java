package fr.ups.mdl.iaws.projectIAWS;


import java.awt.List;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import junit.framework.TestCase;

public class ServiceTrajetTest {
	
	@Test
	public void testStationVide(){
		ServiceVelib serviceVelib=new ServiceVelib();
		Station[] stationsNonVide=serviceVelib.stationNonVide("Université Paul Sabatier Toulouse");
		assert  stationsNonVide.length==3:"nombre de stations respectés";
		assert  stationsNonVide[0].getNom().isEmpty():"pas d'adresse vide dans 0";
		assert  stationsNonVide[1].getNom().isEmpty():"pas d'adresse vide dans 1";
		assert  stationsNonVide[2].getNom().isEmpty():"pas d'adresse vide dans 2";
		assert stationsNonVide[0].getNombreVelos()>0:"station 0 non complète";
		assert stationsNonVide[1].getNombreVelos()>0:"station 1 non complète";
		assert stationsNonVide[2].getNombreVelos()>0:"station 2 non complète";
	}
	@Test
	public void testStationnonComplete(){
		ServiceVelib serviceVelib=new ServiceVelib();
		Station[] stationsNonVide=serviceVelib.stationNonComplete("Université Paul Sabatier Toulouse");
		assert  stationsNonVide.length==3:"nombre de stations respectés";
		assert  stationsNonVide[0].getNom().isEmpty():"pas d'adresse vide dans 0";
		assert  stationsNonVide[1].getNom().isEmpty():"pas d'adresse vide dans 1";
		assert  stationsNonVide[2].getNom().isEmpty():"pas d'adresse vide dans 2";
		assert stationsNonVide[0].getNombrePlaces()>0:"station 0 non complète";
		assert stationsNonVide[1].getNombrePlaces()>0:"station 1 non complète";
		assert stationsNonVide[2].getNombrePlaces()>0:"station 2 non complète";
	}
	@Test
	public void testRisqueChausseMouille(){
		
	}
	@Test
	public void testTempsTrajetPied(){
		
	}
	@Test
	public void testTempsTrajetVelo(){
		
	}
}
