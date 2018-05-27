package de.hdm.kontaktsystem.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

public class ReceivedParticipationCell extends AbstractCell<Participation> {

	@Override
	public void render(Context context, Participation participation, SafeHtmlBuilder sb) {
	      // Prüfung, ob der Wert von participation null ist
	      if (participation == null) {
	        return;
	      }
		
	      sb.appendHtmlConstant("<div>"
		      		+ "<table> <tr><th>Geteiltes Objekt</th>\r\n" + 
		      		"    <th>erhalten von: </th></tr>"
		      		+ "<tr>"
		      		+ "<td>");
		      
	      if(participation.getReferencedObject() instanceof Contact) {
	    	  Contact c = (Contact) participation.getReferencedObject();
	    	  sb.appendEscaped(c.getName().getValue());
	      }else if(participation.getReferencedObject() instanceof ContactList) {
	    	  ContactList cl = (ContactList) participation.getReferencedObject();
	    	  sb.appendEscaped(cl.getName());
	      }else if(participation.getReferencedObject() instanceof PropertyValue) {
	    	  PropertyValue pv = (PropertyValue) participation.getReferencedObject();
	    	  sb.appendEscaped(pv.getValue());
	      }
	      
		  sb.appendHtmlConstant("</td>"
		      		+ "<td>");
		  sb.appendEscaped(participation.getReferencedObject().getOwner().getUserContact().getName().getValue());
		      
		  sb.appendHtmlConstant("</td>"
				  	+ "</tr>"
		      		+ "</table>"
		      		+ "</div>");
	}

}
