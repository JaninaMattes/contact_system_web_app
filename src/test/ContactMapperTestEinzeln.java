package test;

import java.util.Vector;

import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;

import de.hdm.kontaktsystem.shared.bo.*;

public class ContactMapperTestEinzeln {
	

	private final static PropertyMapper pMapper = PropertyMapper.propertyMapper(); 
	private final static UserMapper uMapper = UserMapper.userMapper();
	private final static PropertyValueMapper pvMapper = PropertyValueMapper.propertyValueMapper();
	private final static ContactMapper cMapper = ContactMapper.contactMapper();
	
	// Gültige IDs zu Testen
	private final static double vUID = 666;
	private final static double vUID2 = 798019057881227.4;
	private final static int coID = 22;
	private final static int vCID = 19;
	private final static int vCLID = 3;
	private final static int vPID = 1; // = Sternzeichen
	private final static int vPVID = 20; // = Janina
	
	
	public static void main(String args[]) {	
		
		/*
		 * 
		//#### find Name ####
		System.out.println("\n ############ Test Contact findAll ################ \n");
		Vector <Contact> c = new Vector <Contact>();
		c = cMapper.findAllContacts();
		System.out.println("\n ############ Test Contact findByID ################ \n");
		for(Contact co : c) {
			cMapper.findContactById(co.getBo_Id());
			System.out.println("contact id: " + co.getBo_Id()); // Achtung Kontakte in DB müssen mit Name angelegt werden für Tests
		}		
		
		System.out.println("\n ############ Test Contact findAllByUser ################ \n");
		Vector <Contact> c1 = new Vector <Contact>();
		c1 = cMapper.findAllContactsByUser(vUID);
		for(Contact co : c1) {
		System.out.println("contacts: " + co);
		}		
		
		System.out.println("\n ############ Test Contact addOwnContact ################ \n");
		Contact own = new Contact();
		own.setBo_Id(coID);
		User u = new User();
		u.setGoogleID(vUID);
		u.setUserContact(own);
		own = cMapper.findOwnContact(u);
		System.out.println("own contact id: " + own.getBo_Id());
		cMapper.addOwnContact(own.getBo_Id(), u);
		
		System.out.println("\n ############ Test Contact SharedByOthersToMe ################ \n");
		Vector <Contact> c1 = new Vector <Contact>();
		c1 = cMapper.findAllSharedByOthersToMe(u);
		for(Contact co : c1) {
		System.out.println("contact id: " + co.getBo_Id());
		}		
		
		System.out.println("\n ############ Test Contact findOwnContact ################ \n");
		Contact own = new Contact();
		own.setBo_Id(coID);
		User u = new User();
		u.setGoogleID(vUID);
		u.setUserContact(own);
		Contact c2 = new Contact();
		c2 = cMapper.findOwnContact(u);
		System.out.println(c2);
		
		System.out.println("\n ############ Test Contact byStatus ################ \n"); // Achtung Kontakte in DB müssen mit Name angelegt werden für Tests
		Vector <Contact> cr = new Vector <Contact>();
		cr = cMapper.findContactByStatus(u.getGoogleID(), true);
		for(Contact co : cr) {
			System.out.println("contact id: " + co.getBo_Id());
		}			
	
		
		System.out.println("\n ############ Test Contact update ################ \n"); // Achtung Kontakte in DB müssen mit Name angelegt werden für Tests
		Contact c = new Contact ();
		PropertyValue name = new PropertyValue();		
		c = cMapper.findContactById(37);		
		name = pvMapper.findName(c);
		c.setName(name);		
		cMapper.updateContact(c);
				
		
		System.out.println("\n ############ Test Contact findAllSharedByMe(User) ################ \n");
		Contact own = new Contact();
		own.setBo_Id(coID);
		User u = new User();
		u.setGoogleID(vUID2);
		u.setUserContact(own);
		Contact c2 = new Contact();
		c2 = cMapper.findOwnContact(u);
		System.out.println(c2);
		Vector <Contact> c = new Vector <Contact>();
		c = cMapper.findAllSharedByMe(u);
		for(Contact co : c) {
			System.out.println("contact id: " + co.getBo_Id());
		}		
		
		
		System.out.println("\n ############ Test Contact daleteSharedByMe(User) ################ \n");
		cMapper.deleteAllSharedByMe(u);
		
		
		System.out.println("\n ############ Test PropertyValue findByValue ################ \n");
		PropertyValue pV = new PropertyValue();
		pV = pvMapper.propertyValueMapper().findByKey(14);
		Contact c = new Contact();
		System.out.println("\n ############ Test Contact SharedByOthersToMe ################ \n"); // Achtung Kontakte in DB müssen mit Name angelegt werden für Tests
		c = cMapper.findBy(pV); 				
		
		
		System.out.println("\n ############ Test Contact daleteSharedByOthers(User) ################ \n");
		Contact own = new Contact();
		own.setBo_Id(coID);
		User u = new User();
		u.setGoogleID(vUID);
		u.setUserContact(own);		
		cMapper.deleteAllSharedByOthersToMe(u);
		
		*/
		
		System.out.println("\n ############ Test Contact deleteContactId(id) ################ \n");
		Contact c = new Contact();
		c = cMapper.findContactById(19);
		cMapper.deleteContactByID(47);
		Contact contact = cMapper.deleteContact(c);
		System.out.println("contact id " + contact.getBo_Id());
		
	}

}
