package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;

public class ContactList extends BusinessObject {

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	private String name = null;
	private User owner = null;
	
	private Vector <Contact> contacts = new Vector <Contact>();

	/**
	 * Konstruktoren
	 */

	public ContactList() {

	}

	/**
	 * Beim erstellen einer Liste wird ein contact hinzugef�gt.
	 * 
	 */

	public ContactList(String name, User owner, Contact contact) {
		this.name = name;
		this.owner = owner;
		this.contacts = new Vector<Contact>();
		contacts.add(contact);

		/**
		 * Getter und Setter
		 */

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Vector<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Vector<Contact> contacts) {
		this.contacts = contacts;
	}
	
	public void setContact(Contact contact) {
		this.contacts.addElement(contact);
	}

	
	
	@Override
	public String toString() {
		return "ContactList [name=" + name + ", owner=" + owner + ", contacts=" + contacts + "]";
	}

	 /**
	 * Der Hash-Code liefert zu jedem Objekt eine eindeutige Integerzahl, mit der das 
	 * Objekt identifiziert werden kann. Der Hash-Wert entspricht hier einfachheitshalber 
	 * der ID des Objekts.
	 * Dies überschreibt die Methode hashCode() der Klasse Object.
	 * 
	 */
 
	@Override
	public int hashCode(){
		return super.getBo_Id();
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactList other = (ContactList) obj;
		if (contacts == null) {
			if (other.contacts != null)
				return false;
		} else if (!contacts.equals(other.contacts))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	

}
