package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * Formular für die Darstellung und Anzeige eines Kontaktes
 * 
 */

public class ContactForm extends VerticalPanel{
	
	ContactSystemAdministrationAsync contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
	Contact contactToDisplay = null;
	ContactListTreeViewModel ctvm = null;
	
	/**
	 * Instanziieren der Widgets
	 */
	TextBox textBoxName = new TextBox();
	TextBox textBoxNickName = new TextBox();
	TextBox textBoxFirma = new TextBox();
	TextBox textBoxTelefonnummer = new TextBox();
	TextBox textBoxMobilnummer = new TextBox();
	TextBox textBoxEmail = new TextBox();
	TextBox textBoxGeburtsdatum = new TextBox();
	TextBox textBoxAdresse = new TextBox();
	
	Label label = new Label("Kontakt:");
	Label labelName = new Label("Name:");
	Label labelNickName = new Label("Nick-Name:");
	Label labelFirma = new Label("Firma:");
	Label labelTeleNr = new Label("Telefonnummer:");
	Label labelMobilNr = new Label("Mobilnummer:");
	Label labelEmail = new Label("Email:");
	Label labelGeburtsdatum = new Label("Geburtsdatum:");
	Label labelAdresse = new Label("Adresse:");
	
	Label isShared = new Label("Geteilt: ");
	Label labelShare = new Label("Teilen mit: ");
	Label contactStatus = new Label("");
		
	Button deleteButton = new Button("Kontakt löschen");
	Button saveButton = new Button("Kontakt speichern");
	
	CheckBox checkBox1 = new CheckBox();
	CheckBox checkBox2 = new CheckBox();
	CheckBox checkBox3 = new CheckBox();
	CheckBox checkBox4 = new CheckBox();
	CheckBox checkBox5 = new CheckBox();
	CheckBox checkBox6 = new CheckBox();
	CheckBox checkBox7 = new CheckBox();
	CheckBox checkBox8 = new CheckBox();
	
	ListBox shareUser = new ListBox();
	
	/**
	 * Instanziieren der Panels
	 */
	
	/**
	 * HauptPanel
	 */
	private VerticalPanel vp = new VerticalPanel();
	
	/**
	 * Panel für Anordnung der Button
	 */
	private HorizontalPanel btnPanel = new HorizontalPanel();
	
	/**
	 * Startpunkt
	 */
	
	public void  onLoad() {
		super.onLoad();
		
		/**
		 * Anordnung der einzelnen Inhalte für ContactForm.
		 * 
		 */
		Grid contactGrid = new Grid(9, 3);
		
		contactGrid.setWidget(0, 0, label);
		contactGrid.setWidget(0, 1, contactStatus);
		contactGrid.setWidget(0, 2, isShared);
		
		contactGrid.setWidget(1, 0, labelName);
		contactGrid.setWidget(1, 1, textBoxName);
		contactGrid.setWidget(1, 2, checkBox1);
				
		contactGrid.setWidget(2, 0, labelNickName);
		contactGrid.setWidget(2, 1, textBoxNickName);
		contactGrid.setWidget(2, 2, checkBox2);
				
		contactGrid.setWidget(3, 0, labelFirma);
		contactGrid.setWidget(3, 1, textBoxFirma);
		contactGrid.setWidget(3, 2, checkBox3);
				
		contactGrid.setWidget(4, 0, labelTeleNr);
		contactGrid.setWidget(4, 1, textBoxTelefonnummer);
		contactGrid.setWidget(4, 2, checkBox4);
		
		contactGrid.setWidget(5, 0, labelMobilNr);
		contactGrid.setWidget(5, 1, textBoxMobilnummer);
		contactGrid.setWidget(5, 2, checkBox5);
		
		contactGrid.setWidget(6, 0, labelEmail);
		contactGrid.setWidget(6, 1, textBoxEmail);
		contactGrid.setWidget(6, 2, checkBox6);
		
		contactGrid.setWidget(7, 0, labelGeburtsdatum);
		contactGrid.setWidget(7, 1, textBoxGeburtsdatum);
		contactGrid.setWidget(7, 2, checkBox7);
		
		contactGrid.setWidget(8, 0, labelAdresse);
		contactGrid.setWidget(8, 1, textBoxAdresse);
		contactGrid.setWidget(8, 2, checkBox8);
		
		
		Grid shareGrid = new Grid (2, 2);
		shareGrid.setWidget(0, 0, labelShare);
		shareGrid.setWidget(0, 1, shareUser);
		
		deleteButton.setPixelSize(110, 30);
		saveButton.setPixelSize(110, 30);
		 
		btnPanel.add(deleteButton);
		btnPanel.add(saveButton);
		
		/*
		 * CheckBoxen für das Teilen einzelner Elemente einer ContactForm 
		 * per Default auf "false" setzen.
		 */
		checkBox1.setEnabled(false);
	    checkBox2.setEnabled(false);
	    checkBox3.setEnabled(false);
	    checkBox4.setEnabled(false);
	    checkBox5.setEnabled(false);
	    checkBox6.setEnabled(false);
	    checkBox7.setEnabled(false);
	    checkBox8.setEnabled(false);
	    
	    /*
	     * ClickHandler Verbindung zu CheckBoxen
	     */
	    
	    checkBox1.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;	      
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactName = textBoxName.getText();
	    	  if(contactName.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  else {
	    		  pv = contactToDisplay.getName();
	    	  }
	    	  
	      }
	    });
	    
	    
	    checkBox2.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;
	    	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactNickName = textBoxName.getText();
	    	  if(contactNickName.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  //else pv = getValue()
	      }
	    });
	    	  
	    checkBox3.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactFirma = textBoxFirma.getText();
	    	  if(contactFirma.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });

	    checkBox4.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactPhone = textBoxTelefonnummer.getText();
	    	  if(contactPhone.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox5.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactMobile = textBoxMobilnummer.getText();
	    	  if(contactMobile.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox6.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactEmail = textBoxEmail.getText();
	    	  if(contactEmail.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	 
	    checkBox7.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactGebuDatum = textBoxGeburtsdatum.getText();
	    	  if(contactGebuDatum.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox8.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactAdresse = textBoxAdresse.getText();
	    	  if(contactAdresse.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	
	    /** Buttons in CSS
	     * 
	     * Der Name, mit welchem der Delete-Button in CSS formatiert werden kann, wird festgelegt. 
	     */ 

	    deleteButton.addStyleName("DeleteContact"); 
	    
	    /** 
	     * Der Name, mit welchem der save-Button in CSS formatiert werden kann, wird festgelegt. 
	     */ 

	    saveButton.addStyleName("saveContact"); 
	    
	     
		/**
		 * Die TextBox um den Kontakt einzugeben, das Label und das Panel für die Buttons werden 
		 * über das Grid Widget untereinander auf dem VerticalPanel angeordnet.
		 * 
		 */		
	    
		vp.add(label);
		vp.add(contactGrid);
		vp.add(shareGrid);
		vp.add(btnPanel);
				
	}		
		
	  
	/**
	   * Methode um die Informationen aus den Checkboxen als PropertyValue Objekt
	   * einzusammeln und als Vector zur weiteren Verarbeitung durch den 
	   * entsprechenden Button Aufruf in der Menü Leiste zurück zugeben. 
	   * 
	   * @return Vector <PropertyValue>
	   */
/*	

	public Vector <PropertyValue> getAllCheckedValues() {
		   Vector <PropertyValue> result = null;
		   Vector <PropertyValue> pvList = null;
		   PropertyValue pv = null;
		  		   
		   if(contactToDisplay!=null && contactToDisplay.isShared_status() == true) {	
			   pvList = contactToDisplay.getPropertyValues();			
			   //Checkbox 1 prüfen und Objekt abrufen
			   if(checkBox1.isChecked()) {		
			   for(PropertyValue p : pvList) {
			   if(p.getValue().equals(textBoxName.getText())) {
				   pv = new PropertyValue();
				   result.add(pv);
			   		}
			   	  }
			   } //Checkbox 2 prüfen und Objekt abrufen				   
				if(checkBox2.isChecked()) {		
				for(PropertyValue p : pvList) {
				if(p.getValue().equals(textBoxNickName.getText())) {
					pv = new PropertyValue();
					result.add(pv);	
					}
			   	  }
			   } //Checkbox 3 prüfen und Objekt abrufen					
				if(checkBox3.isChecked()) {		
					for(PropertyValue p : pvList) {
					if(p.getValue().equals(textBoxFirma.getText())) {
						pv = new PropertyValue();
						result.add(pv);	
						}
				   	  }
				   } 	
				//Checkbox 4 prüfen und Objekt abrufen
				if(checkBox3.isChecked()) {		
					for(PropertyValue p : pvList) {
					if(p.getValue().equals(textBoxFirma.getText())) {
						pv = new PropertyValue();
						result.add(pv);
						}
				   	  }
				   } 	
				//Checkbox 5 prüfen und Objekt abrufen
				if(checkBox4.isChecked()) {		
					for(PropertyValue p : pvList) {
					if(p.getValue().equals(textBoxTelefonnummer.getText())) {
						pv = new PropertyValue();
						result.add(pv);
						}
				   	  }
				   } 	
				//Checkbox 6 prüfen und Objekt abrufen
				if(checkBox5.isChecked()) {		
					for(PropertyValue p : pvList) {
					if(p.getValue().equals(textBoxMobilnummer.getText())) {
						pv = new PropertyValue();
						result.add(pv);
						}
				   	  }
				   }
				//Checkbox 7 prüfen und Objekt abrufen
				if(checkBox6.isChecked()) {		
					for(PropertyValue p : pvList) {
					if(p.getValue().equals(textBoxEmail.getText())) {
						pv = new PropertyValue();
						result.add(pv);
						}
				   	  }
				   } 
				//Checkbox 8 prüfen und Objekt abrufen
				if(checkBox7.isChecked()) {		
					for(PropertyValue p : pvList) {
					if(p.getValue().equals(textBoxGeburtsdatum.getText())) {
						pv = new PropertyValue();
						result.add(pv);
						}
				   	  }
				   } 
				//Checkbox 9 prüfen und Objekt abrufen
				if(checkBox8.isChecked()) {		
					for(PropertyValue p : pvList) {
					if(p.getValue().equals(textBoxAdresse.getText())) {
						pv = new PropertyValue();
						result.add(pv);
						}
				   	  }
				   } 
		   }
		   return result;
	}
*/   
	   
		/**
		 * Wenn der anzuzeigende Kontakt gesetzt bzw. gelöscht wird, werden die
		 * zugehörenden Textfelder mit den Informationen aus dem Kontaktobjekt
		 * gefüllt bzw. gelöscht.
		*/
		
		public void setSelected(Contact c) {
			
			if (c != null) {
				contactToDisplay = c;		
				if(c.isShared_status()) contactStatus.setText("Status: Geteilt");
				label.setText("Kontakt: " + c.getBoId());
				
				//Enable Checkboxen
				checkBox1.setEnabled(true);
			    checkBox2.setEnabled(true);
			    checkBox3.setEnabled(true);
			    checkBox4.setEnabled(true);
			    checkBox5.setEnabled(true);
			    checkBox6.setEnabled(true);
			    checkBox7.setEnabled(true);
			    checkBox8.setEnabled(true);
				
				Vector <PropertyValue> pv = new Vector <PropertyValue>();
				pv = contactToDisplay.getPropertyValues();
				
				for(PropertyValue p : pv) {	
					
					//TODO: Überprüfen ob geteilt wurde -> dann checkboxen anhacken
				if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
				if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
				if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
				if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
				if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
				if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
				if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
				if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());				
			    
			}
				
			} else {
				contactStatus.setText("Status: Nicht geteilt");
				label.setText("Kontakt: ");
				textBoxName.setText("");
				textBoxNickName.setText("");
				textBoxFirma.setText("");
				textBoxTelefonnummer.setText("");
				textBoxMobilnummer.setText("");
				textBoxEmail.setText("");
				textBoxGeburtsdatum.setText("");
				textBoxAdresse.setText("");
				
			}
		}
		
		
			/*
			 *  catvm setter
			 */
			
			void setCatvm(ContactListTreeViewModel ctvm) {
				this.ctvm = ctvm;
			}
			

}