package test;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


public class PropertyValueTest {

	
		
		private final static BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();	
		private final static ContactListMapper clMapper = ContactListMapper.contactListMapper();
		private final static ContactMapper cMapper = ContactMapper.contactMapper();
		private final static ParticipationMapper partMapper = ParticipationMapper.participationMapper();
		private final static PropertyMapper pMapper = PropertyMapper.propertyMapper(); 
		private final static PropertyValueMapper pvMapper = PropertyValueMapper.propertyValueMapper();
		private final static UserMapper uMapper = UserMapper.userMapper();
		
		// Gültige IDs zu Testen
		private final static double vUID = 666;
		private final static int vCID = 19;
		private final static int vCLID = 3;
		private final static int vPID = 1; // = Sternzeichen
		private final static int vPVID = 20; // = Janina
		
		
		
		public static void main(String args[]) {
		
		
		
			
			// Test insert method from BusinessObjectMapper 
			System.out.println("\n ############ Test BO ################ \n");
			System.out.println(BusinessObjectMapper.businessObjectMapper().findAllBusinessObjectIDs());
			System.out.println(BusinessObjectMapper.businessObjectMapper().findBusinessObjectIDsByUserID(vUID));
			

			 //Test insert method from UserMapper 
			System.out.println("\n ############ Test User ################ \n");
			
			
			//System.out.println("Find All: " +uMapper.findAll());
			//System.out.println("Find by EMail: " +uMapper.findByEmail("Oli@example.com"));
			PropertyValue pv = pvMapper.findByKey(vPVID);			
			System.out.println(pv);
			Contact contact = cMapper.findContactById(vCID);
			contact.setPropertyValue(pv);
			User u = uMapper.findById(vUID);
			u.setUserContact(cMapper.findContactById(22));
			
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
			cl.setOwner(u);
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
			
			
			
			
			System.out.println("Find by ID: " +pvMapper.findByKey(vPVID));
			
			System.out.println("Find by Property: " +pvMapper.findBy(pMapper.findBy(vPID)));
			System.out.println("Find by Contact: " +pvMapper.findBy(cMapper.findContactById(vCID)));
			System.out.println("Find by PropertyID: " +pvMapper.findByPropertyID(vPID));
			System.out.println("Find by Property: " +pvMapper.findAllCreated(uMapper.findById(vUID)));
			
			
			System.out.println("\n ########### Test Participation Property Value ################## \n");
			
			double sharingUserIdPv = 798019057881227.4;
			User sharingUserPv = uMapper.findById(sharingUserIdPv);
			System.out.println("Shared PropValues By User: " + pvMapper.findAllSharedByMe(sharingUserPv));
			
			
			double participatingUserIDPv = 666.0;
			User participatingUPv = uMapper.findById(participatingUserIDPv);
			System.out.println("Shared PropValues To User: " + pvMapper.findAllSharedByOthersToMe(participatingUPv));
			
			
			
			
			
			System.out.println("\n ############# Test Participation ContactList #################### \n");
			
			
			double sharingUserIDCl = 777.0;
			User sharingUserCl = uMapper.findById(sharingUserIDCl);
			System.out.println("Shared ContactLists By User: " + clMapper.findAllSharedByMe(sharingUserCl));
			
			
			double participatingUserID = 666.0;
			User participatingU = uMapper.findById(participatingUserID);
			System.out.println("Shared ContactLists To User: " + clMapper.findAllSharedByOthersToMe(participatingU));
			
			
		}
		
		
	} 


	

	


