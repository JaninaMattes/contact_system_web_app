package test;

import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.User;

public class ContactListMapperTestEinzeln {
	

	private final static BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();	
	private final static ContactListMapper clMapper = ContactListMapper.contactListMapper();
	private final static ContactMapper cMapper = ContactMapper.contactMapper();
	private final static ParticipationMapper partMapper = ParticipationMapper.participationMapper();
	private final static PropertyMapper pMapper = PropertyMapper.propertyMapper(); 
	private final static PropertyValueMapper pvMapper = PropertyValueMapper.propertyValueMapper();
	private final static UserMapper uMapper = UserMapper.userMapper();
	
	// Gültige IDs zu Testen
	private final static double vUID = 777; //666;
	private final static double vUID2 = 798019057881227.4;
	private final static int coID = 22;
	private final static int vCID = 19;
	private final static int vCLID = 3;
	private final static int vPID = 1; // = Sternzeichen
	private final static int vPVID = 20; // = Janina
	
	
	public static void main(String args[]) {	
		
	/*
	 * 	System.out.println("\n ############ Test ContactList findContactListByID ################ \n");
	
		ContactList cl = new ContactList();
		cl = clMapper.findContactListById(vCLID);
		System.out.println("contactList id: " + cl.getBo_Id());
		
		System.out.println("\n ############ Test ContactList findContactListByID ################ \n");
		Contact c = new Contact();
		c = cMapper.findContactById(coID);
		clMapper.addContactToContactlist(cl, c);
		
		
		
		System.out.println("\n ############ Test ContactList delteContactList ################ \n");
		ContactList cl = new ContactList();
		cl = clMapper.findContactListById(vCLID);
		System.out.println("contactList id: " + cl.getBo_Id());
		clMapper.deleteContactList(cl);
		
		clMapper.deleteContactListById(cl.getBo_Id());
		
		
		
		System.out.println("\n ############ Test ContactList findAllSharedByMe ################ \n");
		User user = new User();
		user = uMapper.findById(vUID);
		Vector <ContactList> cl = new Vector <ContactList>();
		cl = clMapper.findAllSharedByMe(user);
		for(ContactList c : cl) {
			System.out.println("contactList id " + c.getBo_Id());
		}	
		
		System.out.println("\n ############ Test ContactList deleteAllSharedByMe ################ \n");
		clMapper.deleteAllSharedByMe(user);
		
		
		System.out.println("\n ############ Test ContactList findAllSharedByOthers ################ \n");
		User u = new User();
		u = uMapper.findById(vUID);
		Vector <ContactList> c1 = new Vector <ContactList>();
		c1 = clMapper.findAllSharedByOthersToMe(u);
		for(ContactList c : c1) {
			System.out.println("contactList id " + c.getBo_Id());
		}
				
		System.out.println("\n ############ Test ContactList deleteAllSharedByOthers ################ \n");
		clMapper.deleteAllSharedByOthersToMe(u);
		*/
		
		System.out.println("\n ############ Test ContactList findAll() ################ \n");
		Vector <ContactList> c1 = new Vector <ContactList>();
		c1 = clMapper.findAllContactLists();
		for(ContactList c : c1) {
			System.out.println("contactList id " + c.getBo_Id());
		}
		
	}

}
