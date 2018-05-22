package de.hdm.kontaktsystem.server.db;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class BusinessObjectMapper implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	
	/*
	 * Singleton Pattern
	 */
	
	  private static BusinessObjectMapper businessObjectMapper = null;

	  protected BusinessObjectMapper() {
		  
	  }

	  public static BusinessObjectMapper businessObjectMapper() {
	    if (businessObjectMapper == null) {
	    	businessObjectMapper = new BusinessObjectMapper();
	    }

	    return businessObjectMapper;
	  }
	  
	  /**
	   * Updatet das ModifizierungsDatum und den Status des BusinessBojects
	   * @param BusinessObject
	   */
	  public void update(BusinessObject bo){
		  Connection con = DBConnection.connection();
		  try{
			  PreparedStatement stmt = con.prepareStatement("UPDATE BusinessObject SET status = ? WHERE bo_ID = ?");
			  stmt.setBoolean(1, bo.isShared_status());
			  stmt.setInt(2, bo.getBo_Id());
			  stmt.executeUpdate();
			  
		  }catch(SQLException e){
			  
		  }
	  }
	  
	  /**
	   * Ändert den Shared Status des BusinessObjects auf True (Geteilt)
	   * @param BusinessObject ID
	   */
	  public void setStatusTrue(int boID){
		  Connection con = DBConnection.connection();
		  try{
			  PreparedStatement stmt = con.prepareStatement("UPDATE BusinessObject SET status = ? WHERE bo_ID = ?");
			  stmt.setBoolean(1, true);
			  stmt.setInt(2, boID);
			  stmt.executeUpdate();
			  
		  }catch(SQLException e){
			  
		  }
	  }
	  
	  /**
	   * Ändert den Shared Status des BusinessObjects auf False (nicht Geteilt)
	   * @param BusinessObject ID
	   */
	  public void setStatusFalse(int boID){
		  Connection con = DBConnection.connection();
		  try{
			  PreparedStatement stmt = con.prepareStatement("UPDATE BusinessObject SET status = ? WHERE bo_ID = ?");
			  stmt.setBoolean(1, false);
			  stmt.setInt(2, boID);
			  stmt.executeUpdate();
			  
		  }catch(SQLException e){
			  
		  }
	  }
	  
	  
	  /**
	   * Erzeugt eine neue Zeile in der BusinessObject Tabelle und speichert den Autogenerated Key in dem Object 
	   * @param BusinessObject
	   */
	  public void insert(BusinessObject bo){
		  Connection con = DBConnection.connection();

			try {
				//System.out.println("Set Owner " + bo.getOwner());
				PreparedStatement statement = con.prepareStatement(
						"INSERT INTO BusinessObject (user_ID) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
				
				statement.setDouble(1, bo.getOwner().getGoogleID());
				
				
				// Führt das PreparedStatement aus
				statement.executeUpdate();
				
				// save the Autogenerated BusinessObject ID into an result set 
				ResultSet rs = statement.getGeneratedKeys();
				if (rs.next()) {
					
					/** 
					 * Speichert die von der Datenbank generierte ID in dem BusinessObject,
					 * dadurch erhalten die Klassen, welche von BusinessObject erben die selbe ID.
					 * Somit erhält jedes BusinessObjecten eine einzigartige ID.
					 */
					
					bo.setBo_Id(rs.getInt(1));
					//System.out.println("ID: "+ bo.getBo_Id());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	  }
	  
	  // BO Tabelle anhand der ID abfragen ob BO enthalten ist
	  
	  public BusinessObject findBy(int bo_id) {
		
		  BusinessObject bo = new BusinessObject();
          // Verbindung zur DB herstellen
          Connection con = DBConnection.connection();
                   
          try {              
             // Statement ausfüllen und als Query an die DB schicken
             PreparedStatement stmt = con.prepareStatement(
            		 			"SELECT * "
            		 		  + "FROM BusinessObject "  
                              + "WHERE BusinessObject.bo_ID = ? " 
                   			  );
             
             stmt.setInt(1, bo_id);
             // Statement ausfüllen und als Query an die DB schicken
             ResultSet rs = stmt.executeQuery();
                          
              if (rs.next()) {
            	 User user = new User();
            	 user.setGoogleID(rs.getDouble("user_Id"));
            	 
            	 bo.setBo_Id(rs.getInt("bo_ID"));
            	 bo.setCreationDate(rs.getTimestamp("creationDate"));
            	 bo.setModifyDate(rs.getTimestamp("modificationDate"));
            	 bo.setShared_status(rs.getBoolean("status"));
            	 bo.setOwner(user);                           
                }              
              
              return bo;
             
          } catch (SQLException e) {
              e.printStackTrace();
          }       
		 return null;
	  }

	  	  
	  /**
	   * Exception wenn das gesuchte BusinessBoject nicht gefunden wurde
	   * Aktuell nicht genutzt
	   * @author oli
	   *
	   */
	  public class BONotFoundException extends Exception{

		private static final long serialVersionUID = 1L;

		@Override
		public void printStackTrace() {
			// TODO Auto-generated method stub
			System.err.println("Das gesuchte BusinessObject existiert in der Datenbank nicht");
		}
				
		  
	  }
	  
	
	   
	  
	  /**
	   * Gibt alle BusinessObject IDs aus der Tabelle zurück
	   * @return Vector<Integer>
	   */
	  public Vector<Integer> findAllBusinessObjectIDs(){
		  Vector<Integer> boList = new Vector<Integer>();
		  
		  Connection con = DBConnection.connection();
		  try{
			  Statement stmt = con.createStatement();
			  ResultSet rs = stmt.executeQuery("Select * FROM BusinessObject ");
			  while (rs.next()){
				  boList.add(rs.getInt("bo_ID"));
			  }
		  }catch(SQLException e){
			  
		  }
		  
		  return boList;
	  }
	  
	  /**
	   * Gibt alle BusinessObject IDs von einem User aus der Tabelle zurück
	   * @return Vector<Integer>
	   */
	  public Vector<Integer> findBusinessObjectIDsByUserID(double userID){
		  Vector<Integer> boList = new Vector<Integer>();
		  
		  Connection con = DBConnection.connection();
		  try{
			  PreparedStatement stmt = con.prepareStatement("Select * FROM BusinessObject WHERE user_ID = ?");
			  stmt.setDouble(1, userID);
			  ResultSet rs = stmt.executeQuery();
			  while (rs.next()){
				  boList.add(rs.getInt("bo_ID"));
			  }
		  }catch(SQLException e){
			  
		  }
		  
		  return boList;
	  }
	  

	/**
	 * Löscht den Eintrag der das übergebene BusinessObject enthält  
	 * @param BusinessObject
	 */
	public void deleteBusinessObject(BusinessObject bo) {
		deleteBusinessObjectByID(bo.getBo_Id());
	}

	
	/**
	 * Löscht das BusinessObject mit der übergebenen ID
	 * @param BusinessObjectID
	 */
	public void deleteBusinessObjectByID(int bo_id) {
		// TODO Auto-generated method stub
		Connection con = DBConnection.connection();
		  
		  try {
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement

		      ("DELETE FROM BusinessObject WHERE bo_ID = ? ");
		      stmt.setInt(1, bo_id);

		      stmt.execute();
		      
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		
	}
	
	/**
	 * Löscht das BusinessObject mit der übergebenen UserID
	 * @param BusinessObjectID
	 */
	public void deleteBusinessObjectByUserId(User user) {
		// TODO Auto-generated method stub
		Connection con = DBConnection.connection();
		  
		  try {
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement
		      ("DELETE FROM BusinessObject WHERE user_ID= ?");
		      stmt.setDouble(1, user.getGoogleID());
		      stmt.execute();
		      
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		
	}

}
