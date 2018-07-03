package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;


/**
 * <p>
 * Reports sind als <code>Serializable</code> deklariert, damit sie von dem 
 * Server an den Client gesendet werden koennen. Der Zugriff auf Reports erfolgt 
 * also nach deren Bereitstellung lokal auf dem Client.
 * </p>
 * <p>
 * Reports enthalten neben den Standard-Daten eine Menge von <code>SingleContact</code>
 * -Elementen.
 * </p>
 * 
 * @see SingleContact
 * @see de.hdm.thies.bankProjekt.shared.report.Report
 * @see de.hdm.thies.bankProjekt.shared.report.CompositeReport
 * @author Sandra Prestel
 */
public class Report implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Absatz, der Daten ueber den User enthaelt, fuer den der Report erstellt wird
	 */
	private Paragraph userData = null;
	
	
	/**
	 * Titel des Berichts
	 */
	private String title = "Report";
	
	/**
	 * Erstellungsdatum des Berichts als String
	 */
	private String created = "";

	/**
	 * Menge der enthaltenen <code>SingleContact</code>-Elemente
	 */
	private Vector<SingleContact> elements = new Vector<SingleContact>();
	
	
	/**
	 * Zurueckgeben der User-Daten
	 * @return User-Daten
	 */
	public Paragraph getUserData() {
		return this.userData;
	}

	/**
	 * Setzen der User-Daten
	 * @param userData Zu setzende User-Daten
	 */
	public void setUserData(Paragraph userData) {
		this.userData = userData;
	}

	/**
	 * Zurueckgeben des Titels
	 * @return Titel des Berichts
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setzen des Titels
	 * @param title Zu setzender Titel des Berichts
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Zurueckgeben des Erstellungsdatums
	 * @return Erstellungsdatum des Berichts
	 */
	public String getCreated() {
		return this.created;
	}

	/**
	 * Setzen des Erstellungsdatums
	 * @param created Zu setzendes Erstellungsdatum
	 */
	public void setCreated(String created) {
		this.created = created;
	}
	
	/**
	 * Hinzufuegen eines <code>SingleContact</code>-Elements
	 * @param element Das hinzuzufuegende SingleContact-Element
	 */
	public void addContactElement(SingleContact element) {
		this.elements.addElement(element);
	}
	
	/**
	 * Entfernen eines <code>SingleContact</code>-Elements
	 * @param element Das zu entfernende SingleContact-Element
	 */
	public void removeContactElement(SingleContact element) {
		this.elements.remove(element);
	}
	
	/**
	 * Auslesen der Anzahl an <code>SingleContact</code>-Elementen
	 * @return int Anzahl der SingleContact-Elemente
	 */
	public int getNumContactElements() {
		return this.elements.size();
	}
	
	/**
	 * Zurueckgeben eines einzelnen <code>SingleContact</code>-Elements
	 * @param i Position des SingleContact-Elements. Bei n Elementen laeuft der 
	 * Index i von 0 bis n-1.
	 * @return das gesuchte SingleContact-Element
	 */
	public SingleContact getSingleContactAt(int i) {
		return this.elements.elementAt(i);
	}
	
	/**
	 * Zurueckgeben aller <code>SingleContact</code>-Elemente
	 * @return Vector mit allen <code>SingleContact</code>-Elementen
	 */
	public Vector<SingleContact> getAllSingleContacts() {
		return this.elements;
	}
	
}
