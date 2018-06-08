package test;

import de.hdm.kontaktsystem.server.ContactSystemAdministrationImpl;
import de.hdm.kontaktsystem.shared.bo.*;

public class DBCleaner {

	private final static ContactSystemAdministrationImpl csa = new ContactSystemAdministrationImpl();
	private static ContactList cl;
	private static Contact c;
	private static User u;
	private static Property[] p = new Property[5]; 
//											0: Name				1: Nick					2:Firma					3:Tel				4:Email
	private static String[][] data ={{	"Oliver Gorges"	,		"Oli"	, 	"Stuttgarter Straßenbahen AG" 	, "07117651745" 	, "Oli@gmail.com"		}, // 0
									{	"Michi Gorges"	,		"Michi"	, 			"Schreinerei Weiss"		, "07117651745" 	, "Michi@gmail.com"		}, // 1
									{	"Michi Wörner"	,		"Michi"	, 			"Thyssen Krupp" 		, "07118123894" 	, "MichiW@gmail.com"	}, // 2
									{	"Marco Pracher"	,		"Marco"	, 				"HdM" 				, "07117261234" 	, "Marco@gmail.com"		}, // 3
									{	"Martin Forster",		"Martin"	, 			"HdM" 				, "10231239991" 	, "Martin@gmail.com"	}, // 4
									{	"Egor Krämer"	,		"Egor"	, 		"McKesson Europe AG" 		, "07118734981" 	, "Egor@gmail.com"		}, // 5
									{	"Janina Mattes"	,		"Janina"	, 		"Daimler TSS" 			, "07118980342" 	, "Janina@gmail.com"	}, // 6
									{	"Ingo Trautwein",		"Ingo"	, 				"HdM" 				, "07111908234" 	, "Ingo@gmail.com"		}, // 7
									{	"Test User"	,			"Testo"	, 			"TestFirma" 			, "73570" 			, "Test@gmail.com"		}, // 8
									{	"Kim Lee"	,			"Kim"	, 			"Daimler TSS"			, "07117657452" 	, "Kim@gmail.com"		}, // 9
									{	"Maximilian Muster"	,	"Maxi"	, 			"MusterFirma"			, "07111235756" 	, "Maxi@gmail.com"		}, // 10
									{	"Katalin Wagner",		"Katalin"	, 			"HdM" 				, "07110985314" 	, "Katalin@gmail.com"	}, // 11
									{	"Sandra Pestel"	,		"Sandra"	, 			"HdM" 				, "07119832476" 	, "Sandra@gmail.com"	}, // 12
									{	"Test User2"	,		"Testi"	, 			"TestFirma" 			, "73571" 			, "Tes1@gmail.com"		}, // 13
									{	"Test User3"	,		"Test"	, 			"TestFirma" 			, "73572" 			, "Test2@gmail.com"		}, // 14
									
									};
	private static PropertyValue pv;
	private static Participation part;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		csa.init();
		p[0] = csa.getPropertyByID(1);
		p[1] = csa.getPropertyByID(2);
		p[2] = csa.getPropertyByID(3); 
		p[3] = csa.getPropertyByID(4);
		p[4] = csa.getPropertyByID(6);
		double id = 10d;
		int cid = 1;
		for(User user : csa.getAllUsers()){
		
			csa.deleteUser(user);
		}
		
		u = new User();
		u.setGMail("Oli@gmail.com");
		u.setGoogleID(170d);
		c = new Contact(); // 0
		u = csa.createUser(u, c);
		c = u.getUserContact();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[0][i]);
			csa.createPropertyValue(pv);
		}
		
		cl = new ContactList();
		cl.setName(data[0][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		
		c = new Contact(); //1
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 2
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		c = new Contact(); // 3
		csa.createUser(u, c);
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 4
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 5
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		c = new Contact(); // 6
		csa.createUser(u, c);
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 7
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 8
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		c = new Contact(); // 9
		csa.createUser(u, c);
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 10
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		c = new Contact(); // 11
		csa.createUser(u, c);
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		c = new Contact(); // 12
		csa.createUser(u, c);
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 13
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 14
		c.setOwner(u);
		c = csa.createContact(c);
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setContact(c);
			pv.setValue(data[cid][i]);
			csa.createPropertyValue(pv);
		}
		csa.addContactToList(c, cl);
		cid++;
		
		System.out.println("DB clean complete");
		
	}
	
	

}
