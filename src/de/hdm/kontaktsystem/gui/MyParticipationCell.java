package de.hdm.kontaktsystem.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * Klasse zur Darstellung von Participation-Objekten.
 * Orientiert an den Klassen CustomerCell und AccountCell aus dem BankProjekt.
 * 
 * @author Sandra
 *
 */

//Hinzufügen eines Symbols zum Unterscheiden der Arten? Oder Sortierung nach Arten?

public class MyParticipationCell extends AbstractCell<Participation> {

	@Override
	public void render(Context context, Participation participation, SafeHtmlBuilder sb) {
	      // Value can be null, so do a null check.
	      if (participation == null) {
	        return;
	      }
		
	      sb.appendHtmlConstant("<div>"
	      		+ "<table> <tr><th>Geteiltes Objekt</th>\r\n" + 
	      		"    <th>geteilt mit: </th></tr>"
	      		+ "<tr>"
	      		+ "<td>");
	      
	      //Anzeige des geteilten Objekts auf der linken Seite
	      //Falls das geteilte Objekt ein Kontakt ist, Anzeige des Namens
	      if(participation.getReferencedObject() instanceof Contact) {
	    	  Contact c = (Contact) participation.getReferencedObject();
//	    	  sb.appendEscaped(c.getpropertyValue().getName());
	    	  //Testdaten
	    	  sb.appendEscaped("MusterKontakt");
	    	  
	    //Falls das geteilte Objekt eine Kontaktliste ist, Anzeige des Bezeichners
	      }else if(participation.getReferencedObject() instanceof ContactList) {
	    	  ContactList cl = (ContactList) participation.getReferencedObject();
//	    	  sb.appendEscaped(cl.getName());
	    	//Testdaten
	    	  sb.appendEscaped("MusterKontaktliste");
	    	  
	    //Falls das geteilte Objekt eine Eigenschaftsausprägung ist, Anzeige des Werts
	      }else if(participation.getReferencedObject() instanceof PropertyValue) {
	    	  PropertyValue pv = (PropertyValue) participation.getReferencedObject();
//	    	  sb.appendEscaped(pv.getValue());
	    	//Testdaten
	    	  sb.appendEscaped("MusterEigenschaft");
	      }
	      
	      
	      sb.appendHtmlConstant("</td>"
	      		+ "<td>");
//	      sb.appendEscaped(participation.getParticipant().getContact().getpropertyValue().getName());
	      //Testdaten
	      sb.appendEscaped("MusterTeilhaber");
	      
	      sb.appendHtmlConstant("</td>"
		      	+ "</tr>"
	      		+ "</table>"
	      		+ "</div>");
	      
	}

}
