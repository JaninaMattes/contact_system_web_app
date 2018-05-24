package de.hdm.kontaktsystem.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.kontaktsystem.shared.bo.ContactList;

public class ContactListCell extends AbstractCell<ContactList> {

	@Override
	public void render(Context context, ContactList cL, SafeHtmlBuilder sb) {
		// ContactList can be null, so do a null check
		if (cL == null) {
			return;
		}

		sb.appendHtmlConstant("<div>" + "<table>" + "<tr>" + "<td>");
		// sb.append(cL.getName());  AUSBESSERN FEHLER
		sb.appendHtmlConstant("<td>" + "<tr>" + "<table>" + "<div>");
		
		/**
		 *Wenn eine Kontaktliste mit mir geteilt wurde, dann zeige das Sympbol "Teilhaberschaft". 
		 */
		
		//if();  Noch zu erg�nzen
			 
		/**
		 * Wenn ich eine Kontaktliste geteilt habe, dann zeige das Symbol "Geteilt".
		 */
		
		//if(); noch zu erg�nzen
			
	}

}
