package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.sql.ResultSet;

//import com.mysql.jdbc.Connection;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
/**
 * 
 * @author Katalin
 *
 */
public class ContactMapper {
	  /**
	   * Singleton Pattern
	   */
	
	  private static ContactMapper contactMapper = null;

	  protected ContactMapper() {
		  
	  }

	  public static ContactMapper contactMapper() {
	    if (contactMapper == null) {
	      contactMapper = new ContactMapper();
	    }

	    return contactMapper;
	  }
	  
	  	  
	/**
	 *  Mapper-Methoden um Kontakte zu lï¿½schen
	 * @param contact
	 */
	public void deleteContact (Contact contact) {
		//nur contact lï¿½schen.. rest in applogik
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELET FROM CONTACT WHERE id = " + contact.getBo_Id());
			PropertyValueMapper.propertyValueMapper().deleteBy(contact); //TODO: abklären ob der richtige?


		}catch(SQLException e){
			e.printStackTrace();
		}

	}

	/**
	 * Mapper-Methode um Kontakte mit Hilfe der ID zu lï¿½schen
	 * @param id
	 */
	public void deleteContactByID (int id){
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM CONTACT WHERE ID = " + id);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Mapper-Methode um alle Kontakte eines Users zu lï¿½schen
	 */

	public void deleteAllContactsByUser(int user_id) {
		//TODO:get all contacts und dann durchgehen und löschen
	         
	         Vector <Contact> result = new Vector <Contact>();
	         result = ContactMapper.contactMapper().findAllContactsByUser(user_id);
	       
	         for (Contact c : result){         
	        	 ContactMapper.contactMapper().deleteContactByID(c.getBo_Id());
	              PropertyValueMapper.propertyValueMapper().deletePropertyValue(c.getBo_Id());
	         }
	          }      
	/**
	 * Mapper-Methode um alle Kontakte eines bestimmten Users zu finden
	 * @param user_id
	 * @return
	 */
	         public Vector<Contact> findAllContactsByUser(int user_id) {
	        	 Vector <Contact> result = new Vector<Contact>();
	             
	             Connection con = DBConnection.connection();
	            
	             try {  
	                 Statement stmt = con.createStatement();
	                 ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
	                       + "BusinessObject.ModificationDate, BusinessObject.Status, "
	                       + "Contact.ID, Contact.name" +
	                       "FROM BusinessObject" +
	                       "INNER JOIN Contact ON BusinessObject.bo_ID = Contact.ID" +
	                       "WHERE BusinessObject.user_ID =" + user_id);
	                
	                 while (rs.next()) {
	                     Contact contact = new Contact();
			          contact.setBo_Id(rs.getInt("id"));
			          contact.setShared_status(rs.getBoolean("status"));
			         // contact.setName(rs.get //PropertyValue
			          contact.setCreationDate(rs.getTimestamp("creationDate"));
			          contact.setModifyDate(rs.getTimestamp("modificationDate"));
	                    
	                     System.out.println(contact.toString());
	    
	                     result.addElement(contact);
	                    
	                   }
	             } catch (SQLException e) {
	                 e.printStackTrace();
	             }
	            
	             return result;
	         }
	    

	public void deleteAllContacts() {
		//TODO:get all contacts und dann durchgehen und lï¿½schen
	}
	
	//Kontakt erstellen
	public void insertContact(Contact contact) {
		//TODO:kontaktobjekt in id reinschreiben
		//BusinessObjectMapper.BusinessObjectMapper().insertBO(e); //setzt in insert BO die ID (Verbindung zu BO)--> braucht man bei insert, delete
	}
	// Anzeigen (findby)
	public void findPropertyValueOfContact() {

	  
	}
	
	
	/**
	 * Mapper-Methode um alle vorhandenen Kontakte zu suchen
	 * @return
	 */
	public Vector<Contact> findAllContacts(){
		  Vector <Contact> result = new Vector<Contact>();
		  
		  Connection con = DBConnection.connection();
		  
		  try {	
			 Statement stmt = con.createStatement();
		      ResultSet rs = stmt.executeQuery("SELECT* FROM CONTACT ");
		      
		      while (rs.next()) {
		          Contact contact = new Contact();
		          contact.setBo_Id(rs.getInt("id"));
		          contact.setShared_status(rs.getBoolean("status"));
		         // contact.setName(rs.get //PropertyValue
		          contact.setCreationDate(rs.getTimestamp("creationDate"));
		          contact.setModifyDate(rs.getTimestamp("modificationDate"));
		          		          
		          result.addElement(contact);
		          
		      	}
		  } catch (SQLException e) {
			  e.printStackTrace();
		  } 
		  
		  return result;
	}
	/**
	 * Mapper-Methode um einen Kontakt per id zu suchen.
	 * @param id
	 * @return
	 */
	  public Contact findContactById(int id) { 
		  Contact contact = new Contact();	  
		  Connection con = DBConnection.connection();
		  
		  try {	
			  Statement stmt = con.createStatement();
		      ResultSet rs = stmt.executeQuery("SELECT id FROM contact WHERE id = " + id);
		      
		      if (rs.next()) {
		          contact.setBo_Id(rs.getInt("id"));
		          contact.setShared_status(rs.getBoolean("status"));
		          // contact.setName(rs.get //PropertyValue
		          contact.setCreationDate(rs.getTimestamp("creationDate"));
		          contact.setModifyDate(rs.getTimestamp("modificationDate"));
		          
		  }
		  } catch (SQLException e) {
			  e.printStackTrace();
		  } 
		  
		  return contact;
	  }
	  /**
	   * Mapper-Methode um einen Kontakt anhand des Namens zu finden
	   * @param name
	   * @return
	   */
	  public Contact findByName(PropertyValue name) {
		  Contact contact = new Contact();	  
		  Connection con = DBConnection.connection();
		  
		  try {	
			  Statement stmt = con.createStatement();
		      ResultSet rs = stmt.executeQuery("SELECT name FROM contact WHERE name = " + name);
		      
		      if (rs.next()) {

		    	  contact.setBo_Id(rs.getInt("id"));
		    	  contact.setShared_status(rs.getBoolean("status"));
		          contact.setBo_Id(rs.getInt("id"));
		          // contact.setName(rs.get //PropertyValue
		          contact.setCreationDate(rs.getTimestamp("creationDate"));
		          contact.setModifyDate(rs.getTimestamp("modificationDate"));
		          
		  }
		  } catch (SQLException e) {
			  e.printStackTrace();
		  } 
		  
		  return contact;
	  }
	
	  /**
	   * Mapper-Methode um einen Kontakt zu bearbeiten
	   * @param contact
	   * @return
	   */
	  public Contact updateContact(Contact contact) {
		  Connection con = DBConnection.connection();
		  
		  try {
		      Statement stmt = con.createStatement();

		      stmt.executeUpdate("UPDATE contact " + "SET status=\"" + contact.getShared_status()
		          + "\" " + "SET modificationDate=\"" + contact.getModifyDate()
		          + "\" "+ "WHERE id=" + contact.getBo_Id());

		    } 
		  	  catch (SQLException e) {
		      e.printStackTrace();
		    } 
		    return contact;
		  }
		  

	public PropertyValue getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector <PropertyValue> findAllPropertyValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PropertyValue findPropertyValueByValue(String value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Vector <Contact> findByStatus(int user_id, boolean shared_status){
		// TODO Auto-generated method stub
				return null;
	}

	
}