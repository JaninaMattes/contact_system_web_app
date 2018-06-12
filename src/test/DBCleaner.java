package test;

import java.util.Vector;

import de.hdm.kontaktsystem.server.ContactSystemAdministrationImpl;
import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.shared.bo.*;

public class DBCleaner {

	private final static ContactSystemAdministrationImpl csa = new ContactSystemAdministrationImpl();
	private static ContactList cl;
	private static Contact c;
	private static User u;
	private static Property[] p = new Property[5]; 
//											0: Name				1: Nick					2:Firma					3:Tel				4:Email
	private static String[][] data ={{	"Oliver Gorges"	,		"Oli"	, 	"Stuttgarter Straßenbahnen AG" 	, "07117651745" 	, "Oli@gmail.com"		}, // 0
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
									{	"Sandra Prestel"	,		"Sandra"	, 			"HdM" 				, "07119832476" 	, "Sandra@gmail.com"	}, // 12
									{	"Test User2"	,		"Testi"	, 			"TestFirma" 			, "73571" 			, "Tes1@gmail.com"		}, // 13
									{	"Test User3"	,		"Test"	, 			"TestFirma" 			, "73572" 			, "Test2@gmail.com"		}, // 14
									
									};
	private static PropertyValue pv;
	private static Participation part;
	private static Vector<PropertyValue> pvv;
	
	
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
			System.out.println("Delete: " + user);
			csa.setCurrentUser(user.getGoogleID());
			csa.deleteUser(user);
		}
		
		BusinessObjectMapper.businessObjectMapper().resetBoId();
		
		u = new User();
		u.setGMail("Oli@gmail.com");
		u.setGoogleID(170d);
		c = new Contact(); // 0
		Vector<PropertyValue> pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[0][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		u = csa.createUser(u, c);
		
		cl = new ContactList();
		cl.setName(data[0][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		
		c = new Contact(); //1
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[1][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact( c);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 2
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[2][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact(c);
		csa.addContactToList(c, cl);
		cid++;
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[3][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		u = csa.createUser(u, c);
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 4
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[4][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact(c);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 5
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[5][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact(c);
		csa.addContactToList(c, cl);
		cid++;
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		c = new Contact(); // 6
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[6][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		u = csa.createUser(u, c);
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 7
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[7][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact(c);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 8
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[8][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact(c);
		csa.addContactToList(c, cl);
		cid++;
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		c = new Contact(); // 6
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[9][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		u = csa.createUser(u, c);
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 10
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[10][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact(c);
		csa.addContactToList(c, cl);
		cid++;
		
		id += 100;
		u = new User();
		u.setGMail(data[cid][4]);
		u.setGoogleID(id);
		c = new Contact(); // 11
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[11][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		u = csa.createUser(u, c);
		
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
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[12][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		u = csa.createUser(u, c);
		
		// KontaktListen erstellen
		cl = new ContactList();
		cl.setName(data[cid][1] + "s Liste");
		cl.setOwner(u);
		cl = csa.createContactList(cl);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 13
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[13][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact(c);
		csa.addContactToList(c, cl);
		cid++;
		
		c = new Contact(); // 14
		c.setOwner(u);
		pvv = new Vector<PropertyValue>();
		// Kontakt eigenschaften anlagen
		for(int i = 0; i < 5; i++){
			pv = new PropertyValue();
			pv.setProperty(p[i]);
			pv.setValue(data[14][i]);
			pvv.add(pv);
		}
		c.setPropertyValues(pvv);
		c = csa.createContact(c);
		csa.addContactToList(c, cl);
		cid++;
		
		// Erstellen von beziehungen
		part = new Participation();
		part.setParticipant(csa.getUserByID(170d));
		part.setReference(csa.getContactById(90));
		csa.createParticipation(part);
		
		part = new Participation();
		part.setParticipant(csa.getUserByID(170d));
		Contact maxi = csa.getContactById(63);
		Vector<PropertyValue> dm = maxi.getPropertyValues();
		dm.remove(csa.getPropertyValueById(68)); // Entfernt die Telefonnummer aus der Teilhaberschaft
		maxi.setPropertyValues(dm);
		part.setReference(maxi);
		csa.createParticipation(part);
		
		part = new Participation();
		part.setParticipant(csa.getUserByID(170d));
		Contact ingo = csa.getContactById(44);
		Vector<PropertyValue> di = ingo.getPropertyValues();
		di.remove(csa.getPropertyValueById(47)); // Entfernt die Email aus der Teilhaberschaft
		ingo.setPropertyValues(di);
		part.setReference(ingo);
		csa.createParticipation(part);
		
		// Listen Teilen mit User 170
		part = new Participation();
		part.setParticipant(csa.getUserByID(170d));
		part.setReference(csa.getContactListById(82));
		csa.createParticipation(part);
		
		part = new Participation();
		part.setParticipant(csa.getUserByID(170d));
		part.setReference(csa.getContactListById(31));
		csa.createParticipation(part);
		
		// BOs teilen mit anderen Usern
		part = new Participation();
		part.setParticipant(csa.getUserByID(110d));
		part.setReference(csa.getContactListById(7));
		csa.createParticipation(part);
		
		part = new Participation();
		part.setParticipant(csa.getUserByID(110d));
		part.setReference(csa.getContactById(1));
		csa.createParticipation(part);
		part = new Participation();
		part.setParticipant(csa.getUserByID(210d));
		part.setReference(csa.getContactById(1));
		csa.createParticipation(part);
		
		part = new Participation();
		part.setParticipant(csa.getUserByID(310d));
		Contact oli = csa.getContactById(1);
		Vector<PropertyValue> d = oli.getPropertyValues();
		d.remove(csa.getPropertyValueById(4)); // Entfernt die Frima aus der Teilhaberschaft
		oli.setPropertyValues(d);
		part.setReference(oli);
		csa.createParticipation(part);
		part = new Participation();
		
		part = new Participation();
		part.setParticipant(csa.getUserByID(410d));
		oli = csa.getContactById(1);
		d = oli.getPropertyValues();
		d.remove(csa.getPropertyValueById(5)); // Entfernt die Tel aus der Teilhaberschaft
		oli.setPropertyValues(d);
		part.setReference(oli);
		csa.createParticipation(part);
		part = new Participation();
		
		part = new Participation();
		part.setParticipant(csa.getUserByID(510d));
		part.setReference(csa.getContactById(1));
		csa.createParticipation(part);
		
		System.out.println("DB clean complete");
		
	}
	
	

}
