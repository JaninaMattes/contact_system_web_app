package test;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

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

		
//		PropertyValue pv = new PropertyValue("Janina");
//		Property prop = new Property();
//		prop.setId(2);
//		pv.setBo_Id(103);
//		pv.setProp(prop);
//		User testUser = new User();
//		testUser.setGoogleID(8.914336227056141e15);
//		pv.setOwner(testUser);
		
		//System.out.println(PropertyValueMapper.propertyValueMapper().findByKey(103));
	
		
		
		
//		User u = new User();
//		u.setGoogleID(3);
//		Property prop = new Property();
//		prop.setId(3);
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
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt3 = con.prepareStatement
			("INSERT INTO Property (ID, description) VALUES (?, ?)");
					stmt3.setInt(1, 7);
					stmt3.setString(2, "testProp");
					stmt3.execute();
			
			PreparedStatement stmt1 = con.prepareStatement
			("INSERT INTO PropertyValue (ID, property_ID, value) VALUES (?, ?, ?)");
				   stmt1.setInt(1, 2);
				   stmt1.setInt(2, 7);
				   stmt1.setString(3, "testPv");
				   stmt1.execute();
			
	        // Einfügeoperation in propertyvalue erfolgt
			PreparedStatement stmt2 = con.prepareStatement
	        ("INSERT INTO Contact_PropertyValue (Contact_ID, propertyValue_ID) VALUES (?, ?)");
			    stmt2.setInt(1, 32);
			    stmt2.setInt(2, 2);
			    stmt2.execute();

	  } catch(SQLException e) {
		  e.printStackTrace();
	  }
	  */
		
		/*
		Contact c = new Contact();
		PropertyValueMapper.propertyValueMapper().deleteBy(c);  // DONE
		
		*/
		
//		Property p = new Property("Sternzeichen");
//		p.setId(7);
//		User kimLy = new User();
//		PropertyValue pv = new PropertyValue();
//		pv.setOwner(kimLy);
//		pv.setProp(p);
//		pv.getOwner().setGoogleID(0);
//		pv.getOwner().setGMail("default@sys.de");
//		pv.setValue("Fische");
//		
//		
//		//PropertyMapper.propertyMapper().insert(p);
//		
//		PropertyValueMapper.propertyValueMapper().insert(pv);
		// PropertyValueMapper.propertyValueMapper().deleteAll();
		
		Double userId = 8.914336227056141e15;
//		
		PropertyValue pV = new PropertyValue();
				
		User user = new User();
		user.setGoogleID(userId);
		
		// PropertyValueMapper.propertyValueMapper().findAllSharedByMe(user); // Funktioniert
		PropertyValueMapper.propertyValueMapper().findAllSharedByOthersToMe(user);
		
	} 
}

	

	


