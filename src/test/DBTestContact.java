package test;

import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class DBTestContact {
	
	public static void main(String args[]) {
		
		
//		User u = new User();
//		u.setGoogleID(3);
//		
//		Contact c = new Contact();
//		c.setOwner(u);
//		ContactMapper.contactMapper().insertContact(c);
		
		
		Contact c = new Contact(); 
		PropertyValue pv = new PropertyValue();
		
		System.out.println(
		
		ContactMapper.contactMapper().findBy(pv)
				);
		

		/**
		 * Test: Einen Kontakt hizufügen
		 */
		//ContactMapper.contactMapper().insertContact(contact);
		//ContactMapper.contactMapper().insertContact(contact2);
		//ContactMapper.contactMapper().insertContact(contact3);

		
//		ContactMapper mapper = ContactMapper.contactMapper();
//		
//		User user = new User();
//		User user2 = new User();
//		Contact contact = new Contact();
//		Contact contact2 = new Contact();
//		Contact contact3 = new Contact();
//		PropertyValue pv = new PropertyValue();
//		
//		user.setContact(contact);
//		user.setContact(contact2);
//		user2.setContact(contact3);
//		contact.setOwner(user);
//		contact2.setOwner(user);
//		contact3.setOwner(user2);
//		contact.setpV(pv);
//		contact.setShared_status(false);
//		
//		/**
//		 * Test: Einen Kontakt durch die ID finden
//		 */
//		//System.out.println(mapper.findContactById(2));
//		
//		/**
//		 * Test: Einen Kontakt hizufügen
//		 */


		/**
		 * Test: Alle Kontakte eines Users finden
		 */
		ContactMapper.contactMapper().findAllContactsByUser(0);
		/**
		 * Test: Alle vorhandenen Kontakte suchen
		 */
		//ContactMapper.contactMapper().findAllContacts();
		/**
		 * Test: Kontakt über den Namen finden
		 */
		//ContactMapper.contactMapper().findByName(pv);
		/**
		 * Test:Kontakt über den Status finden
		 */
		//ContactMapper.contactMapper().findContactByStatus(0, false);
		/**
		 * Test: Kontakt verändern
		 */
		//ContactMapper.contactMapper().updateContact(contact);
		
		
		
		
		
		/**
		 * Test: Einen Kontakt löschen
		 */
		//ContactMapper.contactMapper().deleteContact(contact);
		/**
		 * Test: Einen Kontakt mittels der ID löschen
		 */
		//ContactMapper.contactMapper().deleteContactByID(1);
		/**
		 * Test: Alle Kontakte eines Users löschen
		 */
		//ContactMapper.contactMapper().deleteAllContactsByUser(0);
		/**
		 * Test: Alle Kontakte löschen
		 */
		//ContactMapper.contactMapper().deleteAllContacts();

		
		
	}
}
