package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.User;

public class ContactForm2 extends VerticalPanel {
	
		//Referenzattribute
		User myUser = null; 
		
		Contact contactToDisplay = null;
		TreeViewModelTest tvm = null;
		
		//Proxy-Objekt erzeugen
		ContactSystemAdministrationAsync contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();	
		
		
		/**
		 * Widgets, deren Inhalte variable sind, werden als Attribute angelegt.
		 */	
		Vector <TextBox> textboxes = new Vector <TextBox>();
		FlexTable ft = new FlexTable();
		
		Label label = new Label("Kontakt:");
		Label labelName = new Label("Name:");
		Label labelNickName = new Label("Nick-Name:");
		Label labelFirma = new Label("Firma:");
		Label labelTeleNr = new Label("Telefonnummer:");
		Label labelMobilNr = new Label("Mobilnummer:");
		Label labelEmail = new Label("Email:");
		Label labelGeburtsdatum = new Label("Geburtsdatum:");
		Label labelAdresse = new Label("Adresse:");
		
		Label isShared = new Label("Auswählen: ");
		Label labelShare = new Label("Teilen mit: ");
		Label labelSharedWith = new Label("Geteilt mit: ");
		Label labelReceivedFrom = new Label("Geteilt von: ");
		Label contactStatus = new Label("");
				
		Button deleteButton = new Button("Löschen");
		Button saveButton = new Button("Speichern");
		Button shareButton = new Button("Teilen");
		Button emailButton = new Button("Email Prüfen");
		
		CheckBox checkBox1 = new CheckBox();
		CheckBox checkBox2 = new CheckBox();
		CheckBox checkBox3 = new CheckBox();
		CheckBox checkBox4 = new CheckBox();
		CheckBox checkBox5 = new CheckBox();
		CheckBox checkBox6 = new CheckBox();
		CheckBox checkBox7 = new CheckBox();
		CheckBox checkBox8 = new CheckBox();
		
		ListBox shareUser = new ListBox();
		ListBox sharedWithUser = new ListBox();
		ListBox receivedFrom = new ListBox();
		
		/**
		 * Startpunkt ist die onLoad() Methode
		 */
		public void  onLoad() {
			super.onLoad();
			VerticalPanel vp  = new VerticalPanel();
			this.add(vp);

			
		    /*
			 * Panel für Anordnung der Button
			 */
			HorizontalPanel btnPanel = new HorizontalPanel(); 
		}
		
		/*
		 * TreeViewModel setter
		 */
		
		void setTree(TreeViewModelTest tvm) {
			this.tvm = tvm;
		}

		void setMyUser(User user) {
			this.myUser = user;
		}
		
		native void log(String s)/*-{
		console.log(s);
		}-*/;
}
