package de.hdm.kontaktsystem.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.kontaktsystem.shared.bo.Contact;
/**
 * 
 * @author Katalin
 * Die Klasse dient zur Erzeugung einzelner Listenelemente (um Kontakte in der GUI darzustellen)
 */
public class ContactCell extends AbstractCell<Contact> {

	@Override
	public void render(Context context, Contact value, SafeHtmlBuilder sb) {
		// TODO Auto-generated method stub
		
		
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
		      
		      /**
		       * Mit einer If-Abfrage herausfinden ob der Kontakt ein mit dem User geteilter Kontakt 
		       * oder ein vom User selbst erzeugter Kontakt ist.
		       */
		      //TODO: IF-Abfrage machen wenn Appl.logik fertig
		      //	fehlt um darzustellen, ob ein Kontakt mit mir geteilt wurde
		      
		      
		      sb.appendHtmlConstant("<td>" 
		    		  + "<tr>"
		    		  + "<table>" 
		    		  + "</div>");
	}

}
