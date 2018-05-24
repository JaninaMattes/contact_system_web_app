package test;

import java.util.Random;
import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class DBTest {
	
	private final static BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();	
	private final static ContactListMapper clMapper = ContactListMapper.contactListMapper();
	private final static ContactMapper cMapper = ContactMapper.contactMapper();
	private final static ParticipationMapper partMapper = ParticipationMapper.participationMapper();
	private final static PropertyMapper pMapper = PropertyMapper.propertyMapper(); 
	private final static PropertyValueMapper pvMapper = PropertyValueMapper.propertyValueMapper();
	private final static UserMapper uMapper = UserMapper.userMapper();
	
	// Gültige IDs zu Testen
	private final static double vUID = 666;
	private final static int vCID = 165;
	private final static int vCLID = 167;
	private final static int vPID = 5; // = Sternzeichen
	private final static int vPVID = 166; // = Janina
	
	static double uID;
	
	public static void main(String args[]){
		
		//createAll();
		//updateAll();
		//findAll();
		//deleteAll();
		
		findAllShared();
		
	}
	
	public static void createAll(){
		
		int pTestID = 8;
		
		System.out.println("\n ############ Test Poperty ################ \n");
		
		Property p = new Property();
		p.setId(pTestID);
		p.setDescription("Test"+pTestID);
		//pMapper.insert(p);
		
		
		System.out.println("\n ############ Test User ################ \n");
		
		User u = new User();
		Random rng = new Random();
		u.setGMail("mail@gmail.com");
		// Generate test User with random ID
		u.setGoogleID(666);//rng.nextDouble()*100000000000000000d);
		uID = u.getGoogleID();
		uMapper.insert(u);
		
		
		
		System.out.println("\n ############ Test Contact ################ \n");
		
		Contact c = new Contact();
		c.setOwner(u);
		cMapper.insertContact(c);
		
		
		System.out.println("\n ############ Test PopertyValue ################ \n");
		
		PropertyValue pv = new PropertyValue();
		pv.setContact(c);
		pv.setOwner(c.getOwner());
		pv.setProp(pMapper.findBy(pTestID));
		pv.setValue("TestUser");
		pvMapper.insert(pv);
		
		System.out.println("Add Contact to User");
		u.setContact(c);
		uMapper.update(u);
		
		
		System.out.println("\n ############ Test ContactList ################ \n");
	
		ContactList cl = new ContactList();
		cl.setName("Meine Liste");
		cl.setOwner(u);
		clMapper.insertContactList(cl);
		
		System.out.println("Add Contact to ContactList");
		clMapper.addContactToContactlist(cl, c);
		
		
		System.out.println("\n ############ Test Participation ################ \n");
		
		Participation p1 = new Participation();
		Participation p2 = new Participation();
		
		p1.setParticipant(uMapper.findById(vUID));
		p2.setParticipant(uMapper.findById(vUID));
		
		p1.setReference(c);
		p2.setReference(cl);
		
		partMapper.insertParticipation(p1);
		partMapper.insertParticipation(p2);
		
		
	}
	
	public static void updateAll(){
		
		
		//System.out.println("\n ############ Test Poperty ################ \n");
		
		
		
		
		System.out.println("\n ############ Test User ################ \n");
		
		User u = uMapper.findById(vUID);
		u.setContact(cMapper.findContactById(vCID));
		uMapper.update(u);
		
		
		//System.out.println("\n ############ Test Contact ################ \n");

		
		System.out.println("\n ############ Test PopertyValue ################ \n");
		
		PropertyValue pv = pvMapper.findByKey(vPVID);
		pv.setShared_status(true);
		pv.setValue("OlisTest");
		pvMapper.update(pv);
		
		
		System.out.println("\n ############ Test ContactList ################ \n");
	
		ContactList cl = clMapper.findContactListById(vCLID);
		cl.setShared_status(true);
		cl.setName("Olis Liste");
		clMapper.updateContactList(cl);
		
		
		System.out.println("\n ############ Test Participation ################ \n");
		
	
		
		
	}
	
	public static void findAll(){
		
		
		// Test insert method from BusinessObjectMapper 
		System.out.println("\n ############ Test BO ################ \n");
		System.out.println(BusinessObjectMapper.businessObjectMapper().findAllBusinessObjectIDs());
		System.out.println(BusinessObjectMapper.businessObjectMapper().findBusinessObjectIDsByUserID(vUID));
		

		

		
		// Test insert method from UserMapper 
		System.out.println("\n ############ Test User ################ \n");
		
		
		System.out.println("Find All: " +uMapper.findAll());
		System.out.println("Find by EMail: " +uMapper.findByEmail("Oli@example.com"));
		User u = uMapper.findById(vUID);
		u.setContact(cMapper.findContactById(117));
		
		uMapper.update(u);
		System.out.println(u = uMapper.findById(u.getGoogleID()));

		
		
		
		
		/**
		 * Test für den ContactList Mapper
		 */
		System.out.println("\n ############ Test ContactList ################ \n");
		
		
		
		System.out.println("Find All: "+clMapper.findAllContactLists());
		
		
		Vector<ContactList> cll = new Vector<ContactList>();
		
		cll = clMapper.findContactListByName("Meine Liste");
		System.out.println("Find By Name: " +cll);
		
		cll = clMapper.findContactListByUser(u);
		System.out.println("Find By User: " +cll);
		

		ContactList cl = clMapper.findContactListById(vCLID);
		
		cl.setName("Die Liste");
		System.out.println("Update ContactList: "+cl);
		clMapper.updateContactList(cl);
		
		// Testet Contact - ContactList BezTabelle
		System.out.println("Add Contact to Contact List");
		clMapper.addContactToContactlist(cl, cMapper.findContactById(vCID));
		
		System.out.println("Find CL by ID: " + clMapper.findContactListById(vCLID));
		
		System.out.println("Remove Contact from Contact List");
		clMapper.removeContactFromContactList(cl, cMapper.findContactById(vCID));
		
		
		
		
		/**
		 * Test Contact Mapper
		 */
		System.out.println("\n ############ Test Contact ################ \n");
		
		Contact c;
		
		//System.out.println(cMapper.findBy(pvMapper.findByKey(vPVID)));
		System.out.println("Find by User: " +cMapper.findAllContactsByUser(vUID));
		System.out.println("Find by Status False: " +cMapper.findContactByStatus(vUID, false));

		System.out.println("Find All: " +cMapper.findAllContacts());
		
		System.out.println(c = cMapper.findContactById(vCID));
		
		
		
		
		
		System.out.println("\n ############ Test Property ################ \n");
		
		System.out.println("Find All: " +pMapper.findAll());
		System.out.println("Find by ID: " +pMapper.findBy(vPVID));
		System.out.println("Find by Desc: " +pMapper.findBy("Name"));

		//System.out.println("Find PV: " +pMapper.findBy(pvMapper.findByKey(vPVID)));

		
		System.out.println("\n ############ Test PropertyValue ################ \n");
		
		PropertyValue pv;
		System.out.println("Find by ID: " +pvMapper.findByKey(vPVID));
		
		System.out.println("Find by Property: " +pvMapper.findBy(pMapper.findBy(vPID)));
		System.out.println("Find by Contact: " +pvMapper.findBy(cMapper.findContactById(vCID)));
		System.out.println("Find by PropertyID: " +pvMapper.findByPropertyID(vPID));
		System.out.println("Find by Property: " +pvMapper.findAllCreated(uMapper.findById(vUID)));
	}
	
	public static void deleteAll(){

		
	

	

		
		
		User user = new User();
		user.setGoogleID(uID);
		
		System.out.println("\n ############ Test Contact ################ \n");
		
		cMapper.deleteAllContactsByUser(uID);
		
		System.out.println("\n ############ Test Poperty ################ \n");
		

		System.out.println("\n ############ Test ContactList ################ \n");

		clMapper.deleteContactListByUserId(uID);
		
		
		System.out.println("\n ############ Test User ################ \n");
		
		uMapper.delete(user);
		
		System.out.println("\n ############ Test PopertyValue ################ \n");
		
		
		
		
		
	
		
	}

	public static void findAllShared(){
		
		User user = uMapper.findById(vUID);
		
		//System.out.println(cMapper.findAllSharedByMe(user));
		
		System.out.println("\n \n \n");
		
		System.out.println(pvMapper.findAllSharedByMe(user));
		
		System.out.println("\n \n \n");
		
		
		System.out.println(pvMapper.findAllSharedByOthersToMe(user));
		
		System.out.println("\n \n \n");
		
		System.out.println(clMapper.findAllSharedByMe(user));
	}
}
