package de.hdm.team09.itProject.shared.bo;

import java.util.Vector;

public class Property extends BusinessObject{
	
	private String description = null;
	private Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
	
	public Property() {
	
	}
	
	public Property(String description) {
		this.description = description;
	}
	
	
	private class PropertyValue <T> extends BusinessObject{
		
		private T value = null;
		private Property property = null;
		private Contact sourceContact = null; //Hier nur einem Kontakt zuweisen oder mehreren? Logik Klassendiagramm
		
		public PropertyValue(T value, Property property, Contact con) {
			
			this.value = value;
			this.property = property;
			this.sourceContact = con;
			
		}
		
		public T getValue() {
			return value;
		}
		
		public void setValue(T value) {
			this.value = value;
		}
		
		public Property getProperty() {
			return property;
		}
		
		public void setProperty(Property property) {
			this.property = property;
		}

		public Contact getSourceContact() {
			return sourceContact;
		}

		public void setSourceContact(Contact sourceContact) {
			this.sourceContact = sourceContact;
		}
		
		
		
		
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	public <T> PropertyValue createPropertyValue(T value, Contact con) {
		PropertyValue propertyValue = new PropertyValue(value, this, con);
		
		this.propertyValues.add(propertyValue);
		
		return propertyValue;	
		
	}
	
	
	

}
