package de.hdm.kontaktsystem.server.db;

import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

public class ContactMapper {

	  /**
	   * Singleton Pattern
	   */
	
	  private static ContactMapper contactMapper = null;

	  protected ContactMapper() {
		  
	  }

	  public static ContactMapper contactMapper() {
	    if (contactMapper == null) {
	      contactMapper = new ContactMapper();
	    }

	    return contactMapper;
	  }
	  
	  

	public Vector<PropertyValue> findByOwner(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}
	  
}
