package test;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.DBConnection;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class PropertyValueTest {

	public static void main(String args[]) {
		
		User u = new User();
		u.setGoogleID(0);
		
		PropertyValueMapper.propertyValueMapper().deleteAllSharedByMe(u);
//		
//		Contact c = new Contact();
//		c.setBo_Id(126);
//		
//		
//
//		PropertyValueMapper.propertyValueMapper().deleteBy(c);
		
		
//		
//		Property prop = new Property();
//		prop.setId(1);
//		
//		PropertyValueMapper.propertyValueMapper().deleteBy(prop);

//		User u = new User();
//		u.setGoogleID(8.207046809097136e16);
//		
//		Vector<PropertyValue> pVVector = new Vector<PropertyValue>();
//		pVVector = PropertyValueMapper.propertyValueMapper().findAllSharedByOthersToMe(u);
		
//		for (PropertyValue pV : pVVector) {
//			
//			System.out.println(pV);
//			
//		}
		
		
		
//		Vector<PropertyValue> pVVector = new Vector<PropertyValue>();
//		pVVector = PropertyValueMapper.propertyValueMapper().findAll();
//		
//		for (PropertyValue pV : pVVector) {
//			
//			System.out.println(pV);
//			
//		}
		
		
	
		
//		System.out.println(
//				PropertyValueMapper.propertyValueMapper().findByKey(123)
//						);
		
//		User u = new User();
//		u.setGoogleID(666);
//
//		
//		Vector<PropertyValue> hilfsVector = new Vector<PropertyValue>();
//		hilfsVector = PropertyValueMapper.propertyValueMapper().findAllSharedByOthersToMe(u);
//		
//		
//		
//		for (PropertyValue pV : hilfsVector) {
//			System.out.println(pV);
//		}
		
		
		
		
		//PropertyValueMapper.propertyValueMapper().deleteAll();
		
//		PropertyValue pv = new PropertyValue("Janina");
//		pv.setBo_Id(111);
//		PropertyValueMapper.propertyValueMapper().update(pv);
		
//		PropertyValue pv = new PropertyValue("Janina");
//		Property prop = new Property();
//		prop.setId(2);
//		pv.setBo_Id(103);
//		pv.setProp(prop);
//		User testUser = new User();
//		testUser.setGoogleID(8.914336227056141e15);
//		pv.setOwner(testUser);

		
		
		
//		User u = new User();
//		u.setGoogleID(0);
//		Property prop = new Property();
//		prop.setId(1);
//		PropertyValue pV = new PropertyValue();
//		pV.setProp(prop);
//
//		
//		Vector<PropertyValue> ResultVector = new Vector<PropertyValue>();
//		ResultVector = PropertyValueMapper.propertyValueMapper().findBy(prop);
//	
//		for (PropertyValue pv : ResultVector) {
//			
//			System.out.println(pv);
//			
//		}
		
		
		/*
		 * findBy(Prop) Test
		 */
		
//		
//		Property prop = new Property("Hihihi");
//		prop.setId(5);
//		
//		Vector<PropertyValue> hilfsVector = new Vector<PropertyValue>();
//		hilfsVector = PropertyValueMapper.propertyValueMapper().findBy(prop);
//	
//		
//		for(int i = 0; i < hilfsVector.size(); i++) {
//			System.out.println(hilfsVector.elementAt(i));
//		}
		
		
	  
		
		/*
		Contact c = new Contact();
		PropertyValueMapper.propertyValueMapper().deleteBy(c);  // DONE
		
		*/
		

//		User kimLy = new User();
//		kimLy.setGoogleID(0);
//		kimLy.setGMail("kl055@hdm-stuttgart.de");
//		PropertyValue pv = new PropertyValue();
//		pv.setValue("Kim-Ly");
		//Property p = new Property();
		//System.out.println(p.getId());
		//Contact c = new Contact();
		//c.setBo_Id(106);
//		c.setOwner(kimLy);
//		pv.setContact(c);
//		pv.setOwner(kimLy);
//		pv.setProp(PropertyMapper.propertyMapper().findBy(1));
//		pv.getOwner().setGoogleID(0);
//		pv.getOwner().setGMail("default@sys.de");
//		pv.setValue("Fische");
		
		
		
		//UserMapper.userMapper().insert(kimLy);
		//ContactMapper.contactMapper().insertContact(c);
		//PropertyValueMapper.propertyValueMapper().insert(pv);
//		
//		PropertyValueMapper.propertyValueMapper().insert(pv);
		// PropertyValueMapper.propertyValueMapper().deleteAll();
		
//		Double userId = 8.914336227056141e15;
////		
//		PropertyValue pV = new PropertyValue();
//				
//		User user = new User();
//		user.setGoogleID(userId);
//		
//		//****************TESTS*********************
//		//******************************************
//		
//		// PropertyValueMapper.propertyValueMapper().findAllSharedByMe(user); // Funktioniert
//		// PropertyValueMapper.propertyValueMapper().findAllSharedByOthersToMe(user); --> Contact Mapper Problem
//		
//		PropertyValueMapper.propertyValueMapper().findAllOwnedByMe(user);
		
	} 
}

	

	


