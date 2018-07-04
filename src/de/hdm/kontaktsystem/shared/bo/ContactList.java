package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;

/**
 * Die Klasse <code>ContactList</code> stellt eine Liste dar, welcher <code>Contact</code> hinzugefuegt werden koennen.
 * <p></p>
 * Einer <code>ContactList</code> muss mindestens ein <code>Contact</code> zugeteilt sein. Eine Kontaktliste
 * ist immer im Besitz eines User und kann mit einem anderen User geteilt werden.
 * @author Marco Pracher
 */
public class ContactList extends BusinessObject {

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Benamung einer Kontaktliste
	 */

	private String name = null;
	
	/**
	 * Alle Kontakte, welche zu einer Kontaktliste zugeordnet werden
	 */
	
	private Vector <Contact> contacts = new Vector <Contact>();

	/**
	 * Konstruktoren
	 */

	public ContactList() {

	}
	
	/**
	 * Beim erstellen einer Kontaktliste muss mindestens
	 * der Listenname festgelegt werden. Eine Kontaktliste
	 * darf damit auch leer, ohne Kontakt erstellt werden,
	 * um diese bei Bedarf zu befüllen. 
	 */
	
	public ContactList(String name) {
		this.name = name;		
	}

	/**
	 * Beim erstellen einer Liste wird ein contact hinzugefuegt.
	 */

	public ContactList(String name, Contact contact) {
		this.name = name;
		this.contacts = new Vector<Contact>();
		contacts.add(contact);
	}

	/**
	 * Aufruf des Listen Namens
	 */
	
	public String getName() {
		return name;
	}
	
	/**
	 * Setzen des Listen Namens
	 */

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Aufruf aller Kontakt-Objekte einer Liste
	 */

	public Vector<Contact> getContacts() {
		return contacts;
	}
	
	/**
	 * Setzen aller Kontakt-Objekte einer Liste
	 */

	public void setContacts(Vector<Contact> contacts) {
		this.contacts = contacts;
	}
	
	/**
	 * Hinzufuegen eines einzelnen Kontaktes zu einer Liste
	 */
	
	public void addContact(Contact contact) {
		this.contacts.addElement(contact);
	}

	/**
	 * Die toString Methode
	 * @see de.hdm.kontaktsystem.shared.bo.BusinessObject#toString()
	 */
	
	@Override
	public String toString() {
		return "ContactList [name=" + name + ", owner=" + getOwner() + ", contacts=" + contacts + " ID: " + this.getBoId() + "]";
	}

	 /**
	 * Der Hash-Code liefert zu jedem Objekt eine eindeutige Integerzahl, mit der das 
	 * Objekt identifiziert werden kann. Der Hash-Wert entspricht hier einfachheitshalber 
	 * der ID des Objekts.
	 * Dies Ueberschreibt die Methode hashCode() der Klasse Object.
	 */
	public int hashCode(){
		return super.getBoId();
	}
	
	/**
	 * Prueft, ob ein Objekt einem User Objekt gleicht.
	 * Gleichheit bedeutet hier, dass alle Attribute der Objekte uebereinstimmen.
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if(!(obj instanceof ContactList))
			return false;
		if(this.getBoId() != ((ContactList) obj).getBoId())
			return false;
		
		return true;
		
	}

	

}
