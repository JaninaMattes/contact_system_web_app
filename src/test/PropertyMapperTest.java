package test;

import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class PropertyMapperTest {
	
	public static void main(String args[]) {
		
		//Hilfskonstruktion für abgerufene Objekte
		
		Vector <Property> propertyResultTest = new Vector <Property>();
		Vector <PropertyValue> propertyValResultTest = new Vector <PropertyValue>();
		Property propertyTest = new Property();
		
		User user = new User();	
		Contact contact = new Contact();
		Property property = new Property();
		PropertyValue propertyValue = new PropertyValue();
		ContactList contactList = new ContactList();
		
		//Vorbereitung der Instanzen
		
		user.setContact(contact);	
		user.setGoogleID(1);
		user.setGMail("janinaTest@gmail.com");
		
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
		
		// Einfügen über BO-Mapper generiert Bo_ID 
		
		BusinessObjectMapper.businessObjectMapper().insert(contact);
		BusinessObjectMapper.businessObjectMapper().insert(property);
		BusinessObjectMapper.businessObjectMapper().insert(propertyValue);
		BusinessObjectMapper.businessObjectMapper().insert(contact);
		
		// Testen des PropertyMapper
		
		PropertyMapper.propertyMapper().insert(property);
		
		// Übergeben der ausgelesenen Objekte
		
		propertyResultTest = PropertyMapper.propertyMapper().findPropertyByDescription("Name");
		propertyTest = PropertyMapper.propertyMapper().findPropertyByID(property.getBo_Id());
		propertyTest = PropertyMapper.propertyMapper().findPropertyByUser(user);
		propertyValResultTest = PropertyMapper.propertyMapper().findPropertyValuesByProperty(property);
		
		PropertyMapper.propertyMapper().findPropertyByStatus(true);
		PropertyMapper.propertyMapper().getAllOwnedProperties(user.getGoogleID());
		PropertyMapper.propertyMapper().getAllPropertiesByUser(user.getGoogleID());
		PropertyMapper.propertyMapper().getAllSharedPropertiesByUser(user.getGoogleID());
		
		PropertyMapper.propertyMapper().deleteAllPropertiesFromUser(user.getGoogleID());
		
		
		System.out.println(propertyResultTest);
		System.out.println(propertyTest);
		System.out.println(propertyTest);
		System.out.println(propertyValResultTest);
		
	}

}
