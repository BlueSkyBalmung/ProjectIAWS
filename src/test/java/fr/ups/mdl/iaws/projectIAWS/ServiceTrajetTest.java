package fr.ups.mdl.iaws.projectIAWS;


import java.awt.List;
import java.util.ArrayList;
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
		ArrayList<Station> stationsNonVide=serviceVelib.stationsNonVides("Université Paul Sabatier");
		assert  stationsNonVide.size()==3:"nombre de stations respectés";
		assert  stationsNonVide.get(0).getAdresse().isEmpty():"pas d'adresse vide dans 0";
		assert  stationsNonVide.get(1).getAdresse().isEmpty():"pas d'adresse vide dans 1";
		assert  stationsNonVide.get(2).getAdresse().isEmpty():"pas d'adresse vide dans 2";
		assert stationsNonVide.get(0).getNombreVelos()>0:"station 0 a des velos";
		assert stationsNonVide.get(1).getNombreVelos()>0:"station 1 a des velos";
		assert stationsNonVide.get(2).getNombreVelos()>0:"station 2 a des velos";
	}
	@Test
	public void testStationnonComplete(){
		ServiceVelib serviceVelib=new ServiceVelib();
		ArrayList<Station> stationsNonVide=serviceVelib.stationsNonCompletes("Université Paul Sabatier Toulouse");
		assert  stationsNonVide.size()==3:"nombre de stations respectés";
		assert  stationsNonVide.get(0).getAdresse().isEmpty():"pas d'adresse vide dans 0";
		assert  stationsNonVide.get(1).getAdresse().isEmpty():"pas d'adresse vide dans 1";
		assert  stationsNonVide.get(2).getAdresse().isEmpty():"pas d'adresse vide dans 2";
		assert stationsNonVide.get(0).getNombrePlaces()>0:"station 0 non complète";
		assert stationsNonVide.get(1).getNombrePlaces()>0:"station 1 non complète";
		assert stationsNonVide.get(2).getNombrePlaces()>0:"station 2 non complète";
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
