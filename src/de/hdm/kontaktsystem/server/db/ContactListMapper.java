package de.hdm.kontaktsystem.server.db;

import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.User;

public class ContactListMapper {
	
	  /**
	   * Singleton Pattern
	   */
	
	  private static ContactListMapper contactListMapper = null;

	  protected ContactListMapper() {
		  
	  }

	  public static ContactListMapper contactListMapper() {
	    if (contactListMapper == null) {
	      contactListMapper = new ContactListMapper();
	    }

	    return contactListMapper;
	  }
	  
	  public Vector <ContactList> findAllContactLists(){
		  //getAll Mapper
		  return null;
	  }
	  
	  public ContactList findContactListById (int id) {
		 //getId Mapper
		  return null;
	  }
	  
	  public Vector <ContactList> findContactListByUser(User user){
		  //getUser Mapper
		  return null;
	  }
	  
	  public Vector <ContactList> findContactListByName(String name){
		  //getName Mapper
		  return null;
	  }
	  
	  public void updateContactList(ContactList cl) {
		  //updateCl Mapper
	  }
	  
	  public void insertContactList(ContactList cl) {
		  //insertCl Mapper
	  }
	  
	  public void deleteContactListById(int id) {
		  //deleteId Mapper
	  }
	  
	  public void deleteContactListByUser(User user){
		  //deleteUser Mapper
	  }
	  
	  public void deleteAllContactList() {
		  //deleteAll Mapper
	  }
	  
	  public void initContactListTable() {
		  //initTable Mapper
	  }
	  
	  public void deleteContactListTable() {
		  //deleteTable Mapper
	  }
	  
	  public ContactListMapper findInstance() {
		  //findInstance Mapper
		  return null;
	  }
	  
	  
	  
	  

}
