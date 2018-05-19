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
			
		
		
		/*
		 * findBy(Prop) Test
		 */
		
		
		Property prop = new Property("Test789");
		
		Vector<PropertyValue> hilfsVector = new Vector<PropertyValue>();
		hilfsVector = PropertyValueMapper.propertyValueMapper().findBy(prop);
		
		
		for (PropertyValue pV : hilfsVector) {
			
		
		System.out.println(
				"Gefundene Ausprägungen zu Eigenschaft : " + pV);
		
		}		
		
		
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
		
		Property p = new Property();
		p.setBo_Id(5);
		User kimLy = new User();
		PropertyValue pv = new PropertyValue();
		pv.setOwner(kimLy);
		pv.setProp(p);
		pv.getOwner().setGoogleID(3);
		pv.setValue("Lalala");
		
		
		PropertyValueMapper.propertyValueMapper().insert(pv);
		 
			
		
	} 
}

	

	


