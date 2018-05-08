package de.hdm.kontaktsystem.server.db;

import java.io.Serializable;

public class BusinessObjectMapper implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	/*
	 * Singleton Pattern
	 */
	
	  private static BusinessObjectMapper businessObjectMapper = null;

	  protected BusinessObjectMapper() {
		  
	  }

	  public static BusinessObjectMapper businessObjectMapper() {
	    if (businessObjectMapper == null) {
	    	businessObjectMapper = new BusinessObjectMapper();
	    }

	    return businessObjectMapper;
	  }
	

}
