package de.hdm.kontaktsystem.shared.bo;

import java.util.Date;

/*Die Klasse Contact stellt die Kontakte dar, die von einem User erstellt werden bzw. auf welche ein User zugreifen kann, 
sowie den User selbst.*/


public class Contact extends BusinessObject {

	// Bezug zu dem User der den Kontakt besitzt
	private User owner = null;
	
	//
	private String status = null;
	
	//Name des Kontakts??
	private String name = null;
	
	//Datum wird von BusinessObject geerbt
	
	//Konstruktor
	
	public Contact(String name, String status, User owner) {
		this.name = name;
		this.status = status;
		this.owner = owner;
	}
	
	//Getter und Setter
	
	//Auslesen des Eigentümers
	
	public User getOwner() {
		return owner;
	}

	//Eigentümer setzen

	public void setOwner(User owner) {
		this.owner = owner;
	}
	//Status auslesen
	
	public String getStatus() {
		return status;
	}

	//Status setzen

	public void setStatus(String status) {
		this.status = status;
	}
	//Name auslesen
	
	public String getName() {
		return name;
	}

	//Name setzen

	public void setName(String name) {
		this.name = name;
	}
	
}
