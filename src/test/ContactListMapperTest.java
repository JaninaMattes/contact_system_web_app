package test;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.DBConnection;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class ContactListMapperTest {

	public static void main(String args[]) {

		User u = new User();
		u.setGoogleID(3);
		
<<<<<<< HEAD
		ContactListMapper.contactListMapper().deleteAllSharedByOthersToMe(u);
		
//		for (ContactList cl : hilfsVektor) {
//			
//			System.out.println(cl);
//			
//		}
		
		
		
=======
		Vector<ContactList> hilfsVektor = new Vector<ContactList>();
		// hilfsVektor = ContactListMapper.contactListMapper().findAllSharedByOthersToMe(u);
		
		// for (ContactList cl : hilfsVektor) {
			
		//	System.out.println(cl);
			
		// }
>>>>>>> 2218cffb89a33163ebc3acbe4deffbd6e597b982
		
		
		
//		BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();
//		ContactListMapper clMapper = ContactListMapper.contactListMapper();
//		ContactMapper cMapper = ContactMapper.contactMapper();
//		ParticipationMapper partMapper = ParticipationMapper.participationMapper();
//		PropertyMapper propMapper = PropertyMapper.propertyMapper();
//		PropertyValueMapper propValMapper = PropertyValueMapper.propertyValueMapper();
//		UserMapper uMapper = UserMapper.userMapper();
//
//		/**
//		 * Test fuer den ContactList Mapper.
//		 */
//		
//		ContactList cl = new ContactList();
//		cl.setName("Kontaktliste 1");
//		cl.setOwner(uMapper.findById(325));
//		
//		Vector<ContactList> cll = new Vector<ContactList>();
		
		/**
		 *Test alle Kontakte finden.
		 */
		
		//System.out.println(clMapper.findAllContactLists());
		
		/**
		 * Kontaktliste durch die ID finden.
		 */
		
		//System.out.println(clMapper.findContactListById(243));
		
		/**
		 * Einen Kontakt aus einer Liste finden.
		 */
		
		//clMapper.findContactFromList(cl);
		//System.out.println(cl);
		
		/**
		 * Kontaktliste durch die ID eines beinhaltenden User finden.
		 */
		
		//cll = clMapper.findContactListByUser(uMapper.findUserById(325));
		
		/**
		 * Kontaktliste nach Namen der Liste finden.
		 */
		
		//cll = clMapper.findContactListByName("KontaktListe_1");
		//System.out.println(cll);
	
		/**
		 * Den Name einer Kontaktliste �ndern / aktualisieren.
		 */
		//Nochmals pr�fen.
		
		//cl.setName("KontaktListe_2");
		//clMapper.updateContactList(cl);
		//cll = clMapper.findContactListByName("KontaktListe_2");
		//System.out.println(cll);
		


		
		
		//######## TESTS #######
		
		double id = 666;
		double id2 = 777;
		double id3 = 798019057881227.4;
		
		User user = new User();
		user.setGoogleID(id2);
		
		Vector <ContactList> cl = new Vector<ContactList>();
		//cl = ContactListMapper.contactListMapper().findAllSharedByMe(user);
		cl = ContactListMapper.contactListMapper().findAllSharedByOthersToMe(user);
		
		System.out.println(cl);
		
		//####### DELETE ########
		
		//ContactListMapper.contactListMapper().deleteAllSharedByMe(user);
		ContactListMapper.contactListMapper().deleteAllSharedByOthersToMe(user);
		
		
	}
	
	
	

}
