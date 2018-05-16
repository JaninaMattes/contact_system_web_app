package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.sql.ResultSet;

//import com.mysql.jdbc.Connection;

import de.hdm.kontaktsystem.shared.bo.Contact;
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
	 *  Mapper-Methoden um einen Kontakt zu l�schen
	 * @param contact
	 */
	public void deleteContact (Contact contact) {
		//nur contact l�schen.. rest in applogik
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELET FROM CONTACT WHERE id = " + contact.getBo_Id());
			//löscht PropertyValue des Kontaktes
			PropertyValueMapper.propertyValueMapper().deleteBy(contact); 
			BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(contact);



		}catch(SQLException e){
			e.printStackTrace();
		}

	}

	/**
	 * Mapper-Methode um einen Kontakt mit Hilfe der ID zu l�schen
	 * @param id
	 */
	public void deleteContactByID (int id){
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM CONTACT WHERE ID = " + id);
			 PropertyValueMapper.propertyValueMapper().deleteByContactId(id);
			 BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectById(id);
			 
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Mapper-Methode um alle Kontakte eines bestimmten Users mittels der User-ID zu l�schen
	 */

	public void deleteAllContactsByUser(int user_id) {
	         
	         Vector <Contact> result = new Vector <Contact>();
	         result = ContactMapper.contactMapper().findAllContactsByUser(user_id);
	       
	         for (Contact c : result){     
	        	 ContactMapper.contactMapper().deleteContactByID(c.getBo_Id());
	        	 PropertyValueMapper.propertyValueMapper().deleteBy(c);  
	        	 BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(c);
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
			         // contact.setName(rs.get //PropertyValue TODO: Abkl�ren
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
	    
/**
 * Mapper-Methode um alle Kontakte zu l�schen
 */
	public void deleteAllContacts() {
		//TODO:alle Kontakte durchgehen l�schen
        Vector <Contact> result = new Vector <Contact>();
        result = ContactMapper.contactMapper().findAllContacts();
      
        for (Contact c : result){     
       	 ContactMapper.contactMapper().deleteContactByID(c.getBo_Id());
       	 PropertyValueMapper.propertyValueMapper().deleteBy(c);  
       	BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(c);
        }
         }  
		

	//Kontakt erstellen
	public void insertContact(Contact contact) {
		
		//TODO:kontaktobjekt in id reinschreiben
		
		Connection con = DBConnection.connection();
		BusinessObjectMapper.businessObjectMapper().insert(contact);
		//setzt in insert BO die ID (Verbindung zu BO)--> braucht man bei insert, delete
		try {
         Statement stmt = con.createStatement();

         stmt.executeUpdate("INSERT INTO contact (id, status, name) "
             + "VALUES (" + contact.getBo_Id() + ",'" + contact.getShared_status() + contact.getName() + "')");
         
   } catch(SQLException e) {
       e.printStackTrace();
   }      
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
		         // contact.setName(rs.get //PropertyValue TODO:Abkl�ren
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
		          // contact.setName(rs.get //PropertyValue TODO: Abkl�ren
		          contact.setCreationDate(rs.getTimestamp("creationDate"));
		          contact.setModifyDate(rs.getTimestamp("modificationDate"));
		          
		  }
		  } catch (SQLException e) {
			  e.printStackTrace();
		  } 
		  
		  return contact;
	  }
	  /**
	   * Mapper-Methode um einen Kontakt durch den Namen zu finden
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
		          // contact.setName(rs.get //PropertyValue TODO: Abkl�ren
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
		  
/**
 * 
 * @param user_id
 * @param shared_status
 * @return
 */
	
	public Contact findContactByStatus(int user_id, boolean shared_status){
		  Contact contact = new Contact();
		  Connection con = DBConnection.connection();
		  
		  try {	
			  Statement stmt = con.createStatement();
		      ResultSet rs = stmt.executeQuery("SELECT id FROM contact WHERE shared_status = " + shared_status);
		      //SQL-Statement?
		      
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

	
}