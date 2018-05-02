package de.hdm.team09.itProject.server.db;

import java.util.Vector;

import de.hdm.team09.itProject.shared.bo.Contact;
import de.hdm.team09.itProject.shared.bo.Property;

public class PropertyMapper {
	
	  /**
	   * Singleton Pattern
	   */
	
	  private static PropertyMapper propertyMapper = null;

	  protected PropertyMapper() {
		  
	  }

	  public static PropertyMapper propertyMapper() {
	    if (propertyMapper == null) {
	      propertyMapper = new PropertyMapper();
	    }

	    return propertyMapper;
	  }

	  // TODO: Methoden ausformulieren & Absprache mit PropertyValue
	  
	  public Vector<Property> getAllProperties(){
		  
		  Vector <Property> propVector = new Vector<Property>();
		  
		  return propVector;
	  }
	  
	  
	  public Property getPropertyByID(int id) {
		  Property property = new Property();
		  
		  return property;
	  }
	  
	  
	  public Property getPropertyByDescription(String description) {
	  Property property = new Property();
		  
		  return property;
		  
	  }
	  
	  
	  public void updateProperty(Property property) {
		  
	  }
	  
	  public void insertProperty(Property property) {
		  
	  }
	  
	  public void deletePropertyByID(int id) {
		  
	  }
	  
	  public void deletePropertyOfContact(Contact contact) {
		  
	  }
	  
	  public void deleteAllProperties() {
		  
	  }
	  
	  public void initPropertyTable() {
		  
	  }
	  
	  public void deletePropertyTable() {
		  
	  }
	  
	 
	 /*public PropertyMapper getInstance() {
		  
	  }
	  */
	  
}
