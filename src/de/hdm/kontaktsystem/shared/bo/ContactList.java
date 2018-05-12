package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;

public class ContactList extends BusinessObject {

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	private String name = null;
	private String status = null;
	private User owner = null;
	private Vector<Contact> contacts = null;

	/**
	 * Konstruktoren
	 */

	public ContactList() {

	}

	/**
	 * Beim erstellen einer Liste wird ein contact hinzugef�gt.
	 * 
	 */

	public ContactList(String name, String status, User owner, Contact contact) {
		this.name = name;
		this.status = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	

}
