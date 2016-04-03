package fr.ups.mdl.iaws.projectIAWS;

public class Station {
	private String adresse;
	private int nombrePlaces;
	private int nombreVelos;
	public Station(String adresse, int nombrePlaces,int nombreVelos){
		this.adresse = adresse;
		this.nombrePlaces = nombrePlaces;
		this.nombreVelos = nombreVelos;
	}
	public String getAdresse() {
		return adresse;
	}

	public int getNombrePlaces() {
		return nombrePlaces;
	}

	public int getNombreVelos() {
		return nombreVelos;
	}
	
}
