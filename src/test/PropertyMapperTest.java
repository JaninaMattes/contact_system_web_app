package test;

import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class PropertyMapperTest {
	
	public static void main(String args[]) {
		
		Vector <Property> testProp = new Vector <Property>();
		
		User user = new User();	
		Contact contact = new Contact();
		
		Property property = new Property();
		Property property1 = new Property();
		Property property2 = new Property();
		Property property3 = new Property();
		
		PropertyValue propertyValue = new PropertyValue();
		PropertyValue propertyValue1 = new PropertyValue();
		PropertyValue propertyValue2 = new PropertyValue();
		PropertyValue propertyValue3 = new PropertyValue();
		PropertyValue propertyValue4 = new PropertyValue();
		
		// BusinessObject bo = new BusinessObject();
		
		
		ContactList contactList = new ContactList();
		
		
		// int property_id = 54;
		// String description = "Telefonnummer";
		// int user_id = 0;
		// int owner_id = 2;
		// boolean shared_status = false;
		
		//Vorbereitung der Instanzen
		
		//user.setContact(contact);	
		//user.setGoogleID(2);
		//user.setGMail("janinaMail@gmail.com");
		//user.setContact(contact);
	
		user = UserMapper.userMapper().findUserById(2);
		
		//contact.setOwner(user);
		contact.setName(propertyValue);
		
		propertyValue.setProp(property);
		propertyValue.setValue("Janina");
		
		propertyValue1.setProp(property1);
		propertyValue1.setValue("+4916666666");
		propertyValue1.setOwner(user);
		
		propertyValue2.setProp(property2);
		propertyValue2.setValue("Liststraße");
		
		propertyValue3.setProp(property3);
		propertyValue3.setValue("Daimler");
		
		propertyValue4.setProp(property3);
		propertyValue4.setValue("Porsche");
		
		property.setPropertyValue(propertyValue);
		property.setDescription("Name");
		//property.setBo_Id(400);
		property.setOwner(user);
		
		// normalerweise nur gesetzt, wenn Objekt geteilt wurde
		property.setShared_status(true);
		
		property1.setPropertyValue(propertyValue1);
		property1.setDescription("Telefonnummer");
		
		property2.setPropertyValue(propertyValue2);
		property2.setDescription("Straße");
		
		property3.setPropertyValue(propertyValue3);
		property3.setPropertyValue(propertyValue4);
		property3.setDescription("Arbeitgeber");
		
		contactList.setContact(contact);
		contactList.setName("Friendlist");
		
		//bo.setBo_Id(900);
		//bo.setOwner(user);				
		
		Participation participation = new Participation();
		participation.setParticipant(user);
		participation.setReference(property);
		
		Participation participation1 = new Participation();
		participation1.setParticipant(user);
		participation1.setReference(property1);
			
	
		//************************************************************
		// Property Mapper - TESTS
		//************************************************************
		
		 // PropertyMapper.propertyMapper().insert(property1); // --> Funktioniert 

		// PropertyMapper.propertyMapper().findByID(property_id); //--> Funktioniert 
		
		// PropertyMapper.propertyMapper().findAll(); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().findByDescription(description); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().findByStatus(user_id, shared_status); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().updateProperty(property1); // --> Funktioniert (Achtung Key Value Constraint)
				
		// PropertyMapper.propertyMapper().findByUser(user); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().findByUserID(user_id); // --> Funktioniert
					
		// PropertyMapper.propertyMapper().deleteProperty(test); // --> Funktionert
		
		// PropertyMapper.propertyMapper().deleteByUserID(143); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().deleteByUser(user); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().delete(property1); // --> Funktioniert
		
		
		
		// PropertyMapper.propertyMapper().findByOwnership(user); --> ??
		 
	    // PropertyMapper.propertyMapper().findByParticipation(user); --> ?? 
	
	}

}
