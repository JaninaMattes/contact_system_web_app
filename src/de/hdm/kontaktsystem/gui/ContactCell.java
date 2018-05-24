package de.hdm.kontaktsystem.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.kontaktsystem.shared.bo.Contact;

public class ContactCell extends AbstractCell<Contact> {

	@Override
	public void render(Context context, Contact value, SafeHtmlBuilder sb) {
		// TODO Auto-generated method stub
		//einzelne Listenelemente mit Symbol
		
	      // Value kann null sein
			if (value == null) {
	          return;
	        }
		      sb.appendHtmlConstant("<div>" 
		    		  + "<table>" 
		    		  + "<tr>" 
		    		  + "<td>" );
		      
		      //Name des Kontakts anzeigen
		      sb.appendEscaped(value.getpropertyValue().getName());
		      
		      sb.appendHtmlConstant(",");
		      sb.appendHtmlConstant("</div>" 
		    		  + "<table>" 
		    		  + "<tr>" 
		    		  + "<td>");
	}

}
