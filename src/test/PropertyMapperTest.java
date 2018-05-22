package test;

import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class PropertyMapperTest {
	
	public static void main(String args[]) {
		
		Double id = 8.914336227056141e15;
		
		User user = new User();	
		Contact contact = new Contact();
		
		Property property = new Property();
		Property property0 = new Property();
		Property property1 = new Property();
		Property property2 = new Property();
		Property property3 = new Property();
		Property property4 = new Property();
		
		PropertyValue propertyValue = new PropertyValue();
		PropertyValue propertyValue0 = new PropertyValue();
		PropertyValue propertyValue1 = new PropertyValue();
		PropertyValue propertyValue2 = new PropertyValue();
		PropertyValue propertyValue3 = new PropertyValue();
		PropertyValue propertyValue4 = new PropertyValue();
		PropertyValue propertyValue5 = new PropertyValue();
		
		// BusinessObject bo = new BusinessObject();
		
		
		ContactList contactList = new ContactList();
		
		user = UserMapper.userMapper().findUserById(id);
	
		contact.setName(propertyValue);
		
		propertyValue.setProp(property);
		propertyValue.setValue("Janina");
		
		propertyValue0.setProp(property0);
		propertyValue0.setValue("Wonder");
		
		propertyValue1.setProp(property1);
		propertyValue1.setValue("+4916666666");
		propertyValue1.setOwner(user);
		
		propertyValue2.setProp(property2);
		propertyValue2.setValue("Liststraße");
		propertyValue2.setOwner(user);
		
		propertyValue3.setProp(property3);
		propertyValue3.setValue("Daimler");
		propertyValue3.setOwner(user);
		
		propertyValue4.setProp(property3);
		propertyValue4.setValue("Porsche");
		propertyValue4.setOwner(user);
		
		propertyValue5.setProp(property4);
		propertyValue5.setValue("Einhorn");
		propertyValue5.setOwner(user);
		
		// Property - Objekte befüllen
		property.setPropertyValue(propertyValue);
		property.setDescription("Vorname");
		property.setId(1);
		
		property0.setPropertyValue(propertyValue0);
		property0.setDescription("Nachname");
		property0.setId(1);
		
		property1.setPropertyValue(propertyValue1);
		property1.setDescription("Telefonnummer");
		property1.setId(3);
		
		property2.setPropertyValue(propertyValue2);
		property2.setDescription("Straße");
		property2.setId(4);
		
		property3.setPropertyValue(propertyValue3);
		property3.setPropertyValue(propertyValue4);
		property3.setDescription("Arbeitgeber");
		property3.setId(6);
		
		property4.setPropertyValue(propertyValue5);
		property4.setDescription("Sternzeichen");
		property4.setId(7);
		
		contactList.setContact(contact);
		contactList.setName("Friendlist");
		
	
		//************************************************************
		// Property Mapper Test
		//************************************************************
				
		 //PropertyMappe//r.propertyMapper().insert(property4); // -> Erfolgreich  
		 PropertyMapper.propertyMapper().findAll(); // -> Erfolgreich 
//		 PropertyMapper.propertyMapper().findBy(4); // -> Erfolgreich 
		 //PropertyMapper.propertyMapper().findBy(propertyValue2); // -> Erfolgreich x
//		 PropertyMapper.propertyMapper().findBy("Sternzeichen"); // -> Erfolgreich 
		
	}

}
