package fr.ups.mdl.iaws.projectIAWS;

public class Station {
	private String nom;
	private int nombrePlaces;
	private int nombreVelos;
	public Station(String nom,int nombrePlaces,int nombreVelos){
		this.nom=nom;
		this.nombrePlaces=nombrePlaces;
		this.nombreVelos=nombreVelos;
	}
	public String getNom() {
		return nom;
	}

	public int getNombrePlaces() {
		return nombrePlaces;
	}

	public int getNombreVelos() {
		return nombreVelos;
	}
	
}
