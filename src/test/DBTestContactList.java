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

public class DBTestContactList {

	public static void main(String args[]) {

		BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();
		ContactListMapper clMapper = ContactListMapper.contactListMapper();
		ContactMapper cMapper = ContactMapper.contactMapper();
		ParticipationMapper partMapper = ParticipationMapper.participationMapper();
		PropertyMapper propMapper = PropertyMapper.propertyMapper();
		PropertyValueMapper propValMapper = PropertyValueMapper.propertyValueMapper();
		UserMapper uMapper = UserMapper.userMapper();

		/**
		 * Test fuer den ContactList Mapper.
		 */
		
		ContactList cl = new ContactList();
		cl.setName("Kontaktliste 1");
		cl.setOwner(uMapper.getUserById(615));
		
		Vector<ContactList> cll = new Vector<ContactList>();
		
		/**
		 *Test alle Kontakte finden.
		 */
		
		//System.out.println(clMapper.findAllContactLists());
		
		/**
		 * Kontaktliste durch die ID finden.
		 */
		
		System.out.println(clMapper.findContactListById(01));
		
	}

}
