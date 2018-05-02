package de.hdm.team09.itProject.shared.bo;

import java.io.Serializable;
import java.sql.Date;

public abstract class BusinessObject implements Serializable {

  private static final long serialVersionUID = 1L;

  private int id = 0;
  
  private Date creationDate = null;
  private Date modificationDate = null;
  

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getCreationDate() {
	return creationDate;
  }

  public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
  }

  public Date getModificationDate() {
	return modificationDate;
  }

  public void setModificationDate(Date modificationDate) {
	this.modificationDate = modificationDate;
  }

  public String toString() { 
    return this.getClass().getName() + " #" + this.id;
  }

  public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
	return result;
  } 

  /*
   * Bestimmung der Gleichheit zweier Objekte mittels derer id
   */
  
  public boolean equals(Object o) {
   
    if (o != null && o instanceof BusinessObject) {
      BusinessObject bo = (BusinessObject) o;
      try {
        if (bo.getId() == this.id)
          return true;
      }
      catch (IllegalArgumentException e) {
       
        return false;
      }
    }
    return false;
  }  
  

}
