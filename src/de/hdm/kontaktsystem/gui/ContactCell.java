package de.hdm.kontaktsystem.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.kontaktsystem.shared.bo.Contact;

public class ContactCell extends AbstractCell<Contact> {

	@Override
	public void render(Context context, Contact value, SafeHtmlBuilder sb) {
		// TODO Auto-generated method stub
		//einzelne Listenelemente mit Symbol erzeugen
		
	      // Value kann null sein
			if (value == null) {
	          return;
	        }
			
			//HTML Tabelle erzeugen
		      sb.appendHtmlConstant("<div>" 
		    		  + "<table>" 
		    		  + "<tr>" 
		    		  + "<td>" );
		      
		      //Name des Kontakts anzeigen
		      sb.appendEscaped(value.getpropertyValue().getName());
		      sb.appendHtmlConstant("</td>"
		    		  + "<td>");
		      
		      //TODO: if 
		      
		      sb.appendHtmlConstant("<td>" 
		    		  + "<tr>"
		    		  + "<table>" 
		    		  + "</div>");
	}

}
