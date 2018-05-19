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
		
		PropertyValue propertyValue = new PropertyValue();
		PropertyValue propertyValue1 = new PropertyValue();
		
		BusinessObject bo = new BusinessObject();
		
		
		ContactList contactList = new ContactList();
		
		//Vorbereitung der Instanzen
		
		user.setContact(contact);	
		user.setGoogleID(2);
		user.setGMail("janinaMail@gmail.com");
		
		contact.setOwner(user);
		contact.setName(propertyValue);
		
		propertyValue.setProp(property);
		propertyValue.setValue("Janina");
		
		propertyValue1.setProp(property1);
		propertyValue1.setValue("+4916666666");
		
		property.setPropertyValue(propertyValue);
		property.setDescription("Name");
		property.setBo_Id(104);
		
		property1.setPropertyValue(propertyValue1);
		property1.setDescription("Telefonnummer");
		
		// normalerweise nur gesetzt, wenn Objekt geteilt wurde
		property.setShared_status(true);	
		
		contactList.setContact(contact);
		contactList.setName("Friendlist");
		
		bo.setBo_Id(104);
		bo.setOwner(user);				
		
		Participation participation = new Participation();
		participation.setParticipant(user);
		participation.setReference(property);
		
		Participation participation1 = new Participation();
		participation1.setParticipant(user);
		participation1.setReference(bo);
			
		
		// int property_id = 54;
		// String description = "Telefonnummer";
		// int user_id = 0;
		int owner_id = 2;
		boolean shared_status = false;
		
		
		// PropertyMapper.propertyMapper().insert(property1); // --> Funktioniert 

		// PropertyMapper.propertyMapper().findByID(property_id); //--> Funktioniert 
		
		// PropertyMapper.propertyMapper().findAll(); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().findByDescription(description); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().findByStatus(user_id, shared_status); // --> Funktioniert
		
				
		// ParticipationMapper.participationMapper().insertParticipation(participation);
		
		 PropertyMapper.propertyMapper().findSharedByMe(owner_id);
				
		// PropertyMapper.propertyMapper().findByUser(user); // --> Funktioniert
		
		// PropertyMapper.propertyMapper().findByUserID(user_id); // --> Funktioniert
					
		// PropertyMapper.propertyMapper().deleteProperty(test); // --> Funktionert
		
		// PropertyMapper.propertyMapper().deleteByUserID(user_id);
		
		// BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(property_id);
		
		//PropertyMapper.propertyMapper().updateProperty(property1); // --> Fehler im Insert Statement von PropertyValue

		
	}

}
