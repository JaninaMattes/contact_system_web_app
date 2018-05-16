package test;

import java.util.Vector;

import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class PropertyMapperTest {
	
	public static void main(String args[]) {
		
		Vector <Property> testProp = new Vector <Property>();
		
		User user = new User();	
		Contact contact = new Contact();
		Property property = new Property();
		PropertyValue propertyValue = new PropertyValue();
		ContactList contactList = new ContactList();
		
		//Vorbereitung der Instanzen
		
		user.setContact(contact);	
		user.setGoogleID(1011);
		user.setGMail("janinaMail@gmail.com");
		
		contact.setOwner(user);
		contact.setName(propertyValue);
		
		propertyValue.setProp(property);
		propertyValue.setValue("Janina");
		
		property.setPropertyValue(propertyValue);
		property.setDescription("Name");
		
		// normalerweise nur gesetzt, wenn Objekt geteilt wurde
		property.setShared_status(true);	
		
		contactList.setContact(contact);
		contactList.setName("Friendlist");
		
		int property_ID = 901;
		
		// PropertyMapper.propertyMapper().insert(property); // --> Funktioniert 
		
		// PropertyMapper.propertyMapper().deleteByID(property_ID); //--> Funktioniert
		
		// PropertyMapper.propertyMapper().findByID(property_ID); //--> Funktioniert 
		
		PropertyMapper.propertyMapper().findAll(); // --> Inner Join Problem
		
		//System.out.println( PropertyMapper.propertyMapper().findAll());
		
		
		//System.out.println(testProp);
		
		//System.out.println(PropertyMapper.propertyMapper().findAll());
		
		
		//System.out.println(PropertyMapper.propertyMapper().findByDescription("Name"));
		
	}

}
