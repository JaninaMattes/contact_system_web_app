package test;

import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class PropertyMapperTest {
	
	public static void main(String args[]) {
		
		User user = new User();	
		Contact contact = new Contact();
		Property property = new Property();
		PropertyValue propertyValue = new PropertyValue();
		ContactList contactList = new ContactList();
		
		//Vorbereitung der Instanzen
		
		user.setContact(contact);	
		user.setGoogleID(1);
		user.setGMail("janinaMail@gmail.com");
		
		contact.setOwner(user);
		contact.setName(propertyValue);
		
		property.setPropertyValue(propertyValue);
		property.setDescription("Name");
		// normalerweise nur gesetzt, wenn Objekt geteilt wurde
		property.setShared_status(true);
		
		propertyValue.setContact(contact);
		propertyValue.setProp(property);
		propertyValue.setValue("Janina");
		
		contactList.setContact(contact);
		contactList.setName("Friendlist");
		
		
		PropertyMapper.propertyMapper().insert(property);
		
	}

}
