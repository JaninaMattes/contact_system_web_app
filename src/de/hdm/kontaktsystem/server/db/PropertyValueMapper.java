package de.hdm.kontaktsystem.server.db;

public class PropertyValueMapper {
	
	  /**
	   * Singleton Pattern
	   */
	
	  private static PropertyValueMapper propertyValueMapper = null;

	  protected PropertyValueMapper() {
		  
	  }

	  public static PropertyValueMapper propertyValueMapper() {
	    if (propertyValueMapper == null) {
	      propertyValueMapper = new PropertyValueMapper();
	    }

	    return propertyValueMapper;
	  }

}
