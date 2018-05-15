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
	  
	  
	  //???
	//public Vector<PropertyValue> findByOwner(Contact contact) {
		// TODO Auto-generated method stub
		//return null;
	//}
	/**
	 *  Mapper-Methoden um Kontakte zu löschen
	 * @param contact
	 */
	public void deleteContact (Contact contact) {
		//nur contact löschen.. rest in applogik
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELET FROM CONTACT WHERE id = " + contact.getId());
			PropertyMapper.propertyMapper().deletePropertyOfContact(contact);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
<<<<<<< HEAD
	/**
	 * Mapper-Methode um Kontakte mit Hilfe der ID zu löschen
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
	 * Mapper-Methode um alle Kontakte eines Users zu löschen
	 */
	public void deleteAllContacts() {
		//TODO:get all contacts und dann durchgehen und löschen
	}
	//Kontakt erstellen
	public void insertContact(Contact contact) {
		//TODO:kontaktobjekt in id reinschreiben
		//BusinessObjectMapper.BusinessObjectMapper().insertBO(e); //setzt in insert BO die ID (Verbindung zu BO)--> braucht man bei insert, delete
	}
	// Anzeigen (findby)
	public void findPropertyOfContact() {

	  
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
		          contact.setId(rs.getInt("id"));
		          contact.setStatus(rs.getString("status"));
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
		          contact.setId(rs.getInt("id"));
		          contact.setStatus(rs.getString("status"));
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
		          contact.setId(rs.getInt("id"));
		          contact.setStatus(rs.getString("status"));
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

		      stmt.executeUpdate("UPDATE contact " + "SET status=\"" + contact.getStatus()
		          + "\" " + "SET modificationDate=\"" + contact.getModifyDate()
		          + "\" "+ "WHERE id=" + contact.getId());

		    } 
		  	  catch (SQLException e) {
		      e.printStackTrace();
		    } 
		    return contact;
		  }
		  
	  }
=======
>>>>>>> branch 'master' of https://github.com/SandraPrestel/ItProjektSS2018-Team09.git

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
	  
}