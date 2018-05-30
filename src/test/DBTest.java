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
	private final static int vCID = 22;
	private final static int vCLID = 21;
	private final static int vPID = 5; // = Sternzeichen
	private final static int vPVID = 61; // = Janina
	
	static double uID;
	
	public static void main(String args[]){
		
		
		//createUserwithContacts();
		//createAll();
		//updateAll();
		//findAll();
		//deleteAll();
		findAllShared();
		//addParticipations();
		//deleteParticipations();
		
	}
	
	public static void createUserwithContacts(){
		System.out.println("\n ############ Test User ################ \n");
		
		String gmail = "user@gmail.com";
		String nickName = "Oli2";

		User u = new User();
		u.setGMail(gmail);
		u.setGoogleID(170);
		uID = u.getGoogleID();
		
		Contact c = new Contact();
		
		
		uMapper.insert(u);
		
		// Setzt die propertyValues für den Kontakt
		PropertyValue mail = new PropertyValue();
		PropertyValue name = new PropertyValue();
		PropertyValue str = new PropertyValue();
		PropertyValue hausNr = new PropertyValue();
		PropertyValue ort = new PropertyValue();
		PropertyValue arbeit = new PropertyValue();
		PropertyValue tel = new PropertyValue();
		PropertyValue vorname = new PropertyValue();
		PropertyValue nachname = new PropertyValue();
		
		mail.setContact(c);
		name.setContact(c);
		str.setContact(c);
		hausNr.setContact(c);
		ort.setContact(c);
		arbeit.setContact(c);
		tel.setContact(c);
		vorname.setContact(c);
		nachname.setContact(c);
		
		mail.setOwner(c.getOwner());
		name.setOwner(c.getOwner());
		str.setOwner(c.getOwner());
		hausNr.setOwner(c.getOwner());
		ort.setOwner(c.getOwner());
		arbeit.setOwner(c.getOwner());
		tel.setOwner(c.getOwner());
		vorname.setOwner(c.getOwner());
		nachname.setOwner(c.getOwner());
		

		mail.setProperty(pMapper.findBy(6));
		name.setProperty(pMapper.findBy(1));
		str.setProperty(pMapper.findBy(2));
		hausNr.setProperty(pMapper.findBy(3));
		ort.setProperty(pMapper.findBy(8));
		arbeit.setProperty(pMapper.findBy(7));
		tel.setProperty(pMapper.findBy(12));
		vorname.setProperty(pMapper.findBy(10));
		nachname.setProperty(pMapper.findBy(11));
		
		mail.setValue(gmail);
		name.setValue(nickName);
		str.setValue("Musterstr.");
		hausNr.setValue("23");
		ort.setValue("Stuttgart");
		arbeit.setValue("HdMVerlag");
		tel.setValue("07117123496");
		vorname.setValue("Maxi");
		nachname.setValue("Muster");
		
		
		pvMapper.insert(mail);
		pvMapper.insert(name);
		pvMapper.insert(str);
		pvMapper.insert(hausNr);
		pvMapper.insert(ort);
		pvMapper.insert(arbeit);
		pvMapper.insert(tel);
		pvMapper.insert(vorname);
		pvMapper.insert(nachname);
		
		c = new Contact();
		c.setOwner(uMapper.findById(170));
		cMapper.insertContact(c);
		
		
		mail = new PropertyValue();
		name = new PropertyValue();
		str = new PropertyValue();
		hausNr = new PropertyValue();
		ort = new PropertyValue();
		arbeit = new PropertyValue();
		tel = new PropertyValue();
		vorname = new PropertyValue();
		nachname = new PropertyValue();
		
		mail.setContact(c);
		name.setContact(c);
		str.setContact(c);
		hausNr.setContact(c);
		ort.setContact(c);
		arbeit.setContact(c);
		tel.setContact(c);
		vorname.setContact(c);
		nachname.setContact(c);
		
		mail.setOwner(c.getOwner());
		name.setOwner(c.getOwner());
		str.setOwner(c.getOwner());
		hausNr.setOwner(c.getOwner());
		ort.setOwner(c.getOwner());
		arbeit.setOwner(c.getOwner());
		tel.setOwner(c.getOwner());
		vorname.setOwner(c.getOwner());
		nachname.setOwner(c.getOwner());
		

		mail.setProperty(pMapper.findBy(6));
		name.setProperty(pMapper.findBy(1));
		str.setProperty(pMapper.findBy(2));
		hausNr.setProperty(pMapper.findBy(3));
		ort.setProperty(pMapper.findBy(8));
		arbeit.setProperty(pMapper.findBy(7));
		tel.setProperty(pMapper.findBy(12));
		vorname.setProperty(pMapper.findBy(10));
		nachname.setProperty(pMapper.findBy(11));
		
		mail.setValue("meins@test.de");
		name.setValue("Oli");
		str.setValue("Musterstr.");
		hausNr.setValue("25");
		ort.setValue("Stuttgart");
		arbeit.setValue("Druck und Sonstiges");
		tel.setValue("07117123346");
		vorname.setValue("Oli");
		nachname.setValue("Test");
		
		
		pvMapper.insert(mail);
		pvMapper.insert(name);
		pvMapper.insert(str);
		pvMapper.insert(hausNr);
		pvMapper.insert(ort);
		pvMapper.insert(arbeit);
		pvMapper.insert(tel);
		pvMapper.insert(vorname);
		pvMapper.insert(nachname);
		
		c = new Contact();
		c.setOwner(uMapper.findById(170));
		cMapper.insertContact(c);
		
		
		mail = new PropertyValue();
		name = new PropertyValue();
		str = new PropertyValue();
		hausNr = new PropertyValue();
		ort = new PropertyValue();
		arbeit = new PropertyValue();
		tel = new PropertyValue();
		vorname = new PropertyValue();
		nachname = new PropertyValue();
		
		mail.setContact(c);
		name.setContact(c);
		str.setContact(c);
		hausNr.setContact(c);
		ort.setContact(c);
		arbeit.setContact(c);
		tel.setContact(c);
		vorname.setContact(c);
		nachname.setContact(c);
		
		mail.setOwner(c.getOwner());
		name.setOwner(c.getOwner());
		str.setOwner(c.getOwner());
		hausNr.setOwner(c.getOwner());
		ort.setOwner(c.getOwner());
		arbeit.setOwner(c.getOwner());
		tel.setOwner(c.getOwner());
		vorname.setOwner(c.getOwner());
		nachname.setOwner(c.getOwner());
		

		mail.setProperty(pMapper.findBy(6));
		name.setProperty(pMapper.findBy(1));
		str.setProperty(pMapper.findBy(2));
		hausNr.setProperty(pMapper.findBy(3));
		ort.setProperty(pMapper.findBy(8));
		arbeit.setProperty(pMapper.findBy(7));
		tel.setProperty(pMapper.findBy(12));
		vorname.setProperty(pMapper.findBy(10));
		nachname.setProperty(pMapper.findBy(11));
		
		mail.setValue("meins2@test.de");
		name.setValue("Michi");
		str.setValue("Musterstr.");
		hausNr.setValue("25a");
		ort.setValue("Stuttgart");
		arbeit.setValue("Bäckerei Müller");
		tel.setValue("07117123107");
		vorname.setValue("Michi");
		nachname.setValue("Musterman");
		
		
		pvMapper.insert(mail);
		pvMapper.insert(name);
		pvMapper.insert(str);
		pvMapper.insert(hausNr);
		pvMapper.insert(ort);
		pvMapper.insert(arbeit);
		pvMapper.insert(tel);
		pvMapper.insert(vorname);
		pvMapper.insert(nachname);
		
		
		
		
	}
	
	public static void createAll(){
		
		int pTestID = 1;
		/*
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
		//uMapper.insert(u);
		u = uMapper.findById(666);
		
		
		System.out.println("\n ############ Test Contact ################ \n");
		
		Contact c = new Contact();
		c.setOwner(u);
		cMapper.insertContact(c);
		
		
		System.out.println("\n ############ Test PopertyValue ################ \n");
		
		PropertyValue pv = new PropertyValue();
		pv.setContact(c);
		pv.setOwner(c.getOwner());
		pv.setProperty(pMapper.findBy(pTestID));
		pv.setValue("TestUser");
		pvMapper.insert(pv);
		
		System.out.println("Add Contact to User");
		u.setUserContact(c);
		uMapper.update(u);
		*/
		
		System.out.println("\n ############ Test ContactList ################ \n");
	
		ContactList cl = new ContactList();
		cl.setName("Olis Liste");
		cl.setOwner(uMapper.findById(170));
		clMapper.insertContactList(cl);
		
		System.out.println("Add Contact to ContactList");
		
		clMapper.addContactToContactlist(cl, cMapper.findContactById(75));
		clMapper.addContactToContactlist(cl, cMapper.findContactById(85));
		clMapper.addContactToContactlist(cl, cMapper.findContactById(95));
		
		
		System.out.println("\n ############ Test Participation ################ \n");
		
		Participation p1 = new Participation();
		Participation p2 = new Participation();
		Participation p3 = new Participation();
		
		p1.setParticipant(uMapper.findById(vUID));
		p2.setParticipant(uMapper.findById(vUID));
		p3.setParticipant(uMapper.findById(vUID));
		
		p1.setReference(cMapper.findContactById(75));
		p2.setReference(cl);
		p3.setReference(cMapper.findContactById(95));
		
		partMapper.insertParticipation(p1);
		partMapper.insertParticipation(p2);
		partMapper.insertParticipation(p3);
		
		
	}
	
	public static void updateAll(){
		
		
		//System.out.println("\n ############ Test Poperty ################ \n");
		
		
		
		
		System.out.println("\n ############ Test User ################ \n");
		
		User u = uMapper.findById(vUID);
		u.setUserContact(cMapper.findContactById(vCID));
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
		System.out.println("Find by EMail: " +uMapper.findByEmail("user@gmail.com"));
		User u = uMapper.findById(vUID);
		u.setUserContact(cMapper.findContactById(70));
		
		uMapper.update(u);
		System.out.println(u = uMapper.findById(u.getGoogleID()));

		
		
		
		
		/**
		 * Test für den ContactList Mapper
		 */
		System.out.println("\n ############ Test ContactList ################ \n");
		
		
		
		System.out.println("Find All: "+clMapper.findAllContactLists());
		
		
		Vector<ContactList> cll = new Vector<ContactList>();
		
		cll = clMapper.findContactListByName("Die Liste");
		System.out.println("Find By Name: " +cll);
		
		cll = clMapper.findContactListByUser(u);
		System.out.println("Find By User: " +cll);
		

		ContactList cl = clMapper.findContactListById(vCLID);
		
		cl.setName("Eine Liste");
		System.out.println("Update ContactList: "+cl);
		clMapper.updateContactList(cl);
		
		// Testet Contact - ContactList BezTabelle
		
		Contact con = cMapper.findContactById(vCID);
		System.out.println("Add Contact " + con + " to Contact List" + cl);
		clMapper.addContactToContactlist(cl, con);
		
		System.out.println("Find CL by ID: " + clMapper.findContactListById(vCLID));
		
		System.out.println("Remove Contact from Contact List");
		clMapper.removeContactFromContactList(cl, con);
		
		
		
		
		/**
		 * Test Contact Mapper
		 */
		System.out.println("\n ############ Test Contact ################ \n");
		
		Contact c;
		
		//System.out.println(cMapper.findBy(pvMapper.findByKey(vPVID)));
		System.out.println("Find by User: " +cMapper.findAllContactsByUser(vUID));
		System.out.println("Find by Status True: " +cMapper.findContactByStatus(vUID, true));

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
		
		System.out.println("Find by Property: " +pvMapper.findBy(pMapper.findBy(6))); 
		System.out.println("Find by Contact: " +pvMapper.findBy(cMapper.findContactById(75)));
		System.out.println("Find by PropertyID: " +pvMapper.findByPropertyID(6)); 
		System.out.println("Find by Owner: " +pvMapper.findAllCreated(uMapper.findById(170))); // null
	}
	
	public static void deleteAll(){
		
		/*
		User user = new User();
		user.setGoogleID(uID);
		

		System.out.println("\n ############ Test PopertyValue ################ \n");
		
		pvMapper.deleteByProp(1);
		pvMapper.deleteByContact(113);
		pvMapper.deleteByPropValue(136);

		System.out.println("\n ############ Test ContactList ################ \n");

		clMapper.deleteContactListByUserId(uID);
		clMapper.deleteContactListById(1);
		
		System.out.println("\n ############ Test Contact ################ \n");
		
		cMapper.deleteAllContactsByUser(uID);
		//cMapper.deleteContact(cMapper.findContactById(119));
		cMapper.deleteContactByID(113);
		
		*/
		System.out.println("\n ############ Test User ################ \n");
		
		//uMapper.delete(user);
		//uMapper.deleteByID(1.9452793556627856e16);
		uMapper.deleteAll();
		
		
		
		
		
		
		
	
		
	}

	public static void findAllShared(){
		
		User user = uMapper.findById(vUID);
		
		
		System.out.println("\n ########### Contact ############## \n");
		System.out.println("FASPV: " +cMapper.findAllSharedByMe(user));
		System.out.println("FASBOTMPV: " +cMapper.findAllSharedByOthersToMe(user));
		
		
		System.out.println("\n ######### ProptertyValue ########### \n");
		
		System.out.println("FASPV: " + pvMapper.findAllSharedByMe(user));
		
		System.out.println("\n \n");
		
		System.out.println("FASBOTMPV: " +pvMapper.findAllSharedByOthersToMe(user));
		
		
		
		System.out.println("\n ########### ContactList ############## \n");
		
		System.out.println("FASC: " +clMapper.findAllSharedByMe(user));
		System.out.println("FASBOTMPV: " +clMapper.findAllSharedByOthersToMe(user));
	}

	public static void addParticipations(){
		
		Vector<Participation> parts = new Vector<Participation>();
		Participation p = new Participation();
		int id = 126;
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(22));
		parts.add(p);
		p = new Participation();
		System.out.println("Parts: " + parts.size());
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(23));
		parts.add(p);
		p = new Participation();
		System.out.println("Parts: " + parts.size());
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(24));
		parts.add(p);
		p = new Participation();
		System.out.println("Parts: " + parts.size());
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(56));
		parts.add(p);
		p = new Participation();
		System.out.println("Parts: " + parts.size());
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(57));
		parts.add(p);
		System.out.println("Parts: " + parts.size());
		
		id = 666;
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(40));
		parts.add(p);
		p = new Participation();
		System.out.println("Parts: " + parts.size());
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(43));
		parts.add(p);
		p = new Participation();
		System.out.println("Parts: " + parts.size());
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(44));
		parts.add(p);
		p = new Participation();
		System.out.println("Parts: " + parts.size());
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(56));
		parts.add(p);
		p = new Participation();
		System.out.println("Parts: " + parts.size());
		p.setParticipant(uMapper.findById(id));
		p.setReference(boMapper.findBusinessObjectByID(57));
		parts.add(p);
		System.out.println("Parts: " + parts.size());
		for(Participation p1 : parts){
		
			partMapper.insertParticipation(p1);
		}
	}
	
	public static void deleteParticipations(){
		//partMapper.deleteParticipationForBusinessObject(cMapper.findContactById(63));
		
		//partMapper.deleteParticipationForOwner(uMapper.findById(126));
		partMapper.deleteParticipationForParticipant(uMapper.findById(777)); // Geht nicht !!!
		
		/*
		Participation p = new Participation();
		p.setParticipant(uMapper.findById(126));
		p.setReference(cMapper.findContactById(17));
		partMapper.deleteParticipation(p);
		*/
		
	}

}
