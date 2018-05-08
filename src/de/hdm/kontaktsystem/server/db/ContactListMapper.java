package de.hdm.kontaktsystem.server.db;

public class ContactListMapper {
	
	  /**
	   * Singleton Pattern
	   */
	
	  private static ContactListMapper contactListMapper = null;

	  protected ContactListMapper() {
		  
	  }

	  public static ContactListMapper contactListMapper() {
	    if (contactListMapper == null) {
	      contactListMapper = new ContactListMapper();
	    }

	    return contactListMapper;
	  }
	  
	  

}
