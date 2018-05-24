package de.hdm.kontaktsystem.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Participation;

/**
 * Klasse zur Darstellung von Participation-Objekten.
 * Orientiert an den Klassen CustomerCell und AccountCell aus dem BankProjekt.
 * 
 * @author Sandra
 *
 */

public class MyParticipationCell extends AbstractCell<Participation> {

	@Override
	public void render(Context context, Participation participation, SafeHtmlBuilder sb) {
	      // Value can be null, so do a null check.
	      if (participation == null) {
	        return;
	      }
		
	      sb.appendHtmlConstant("<div>"
	      		+ "<table>"
	      		+ "<tr>"
	      		+ "<td>");
	      
	      sb.append(participation.getParticipant().getContact().getpropertyValue().getName());
	      
	      sb.appendHtmlConstant("</td>"
		      	+ "</tr>"
	      		+ "</table>"
	      		+ "</div>");
	      
	}

}
