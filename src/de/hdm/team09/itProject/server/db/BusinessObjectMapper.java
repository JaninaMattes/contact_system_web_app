package de.hdm.team09.itProject.server.db;

public class BusinessObjectMapper {
	
	  /**
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
