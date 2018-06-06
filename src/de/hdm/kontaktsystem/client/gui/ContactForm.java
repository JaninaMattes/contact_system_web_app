package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

/**
 * Formular für die Darstellung und Anzeige eines Kontaktes
 * @author janina
 */

public class ContactForm extends VerticalPanel{
	
	private User owner = new User();
	private User sharedFor = new User();
	private User sharedFrom = new User();
	private int count = 0;
	
	
	ContactSystemAdministrationAsync contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
	Contact contactToDisplay = null;
	ContactTreeViewModel ctvm = null;
	ContactListTreeViewModel cltvm = null; /*Kann von beiden TreeViewModeln aufgerufen werden*/
	MyParticipationsTreeViewModel mptvm = null;
	ReceivedParticipationTreeViewModel rptvm = null;
	
	/**
	 * Widgets, deren Inhalte variable sind, werden als Attribute angelegt.
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
	Label labelSharedWith = new Label("Geteilt mit: ");
	Label labelReceiedFrom = new Label("Geteilt von: ");
	Label contactStatus = new Label("");
		
	Button deleteButton = new Button("Löschen");
	Button saveButton = new Button("Speichern");
	Button shareButton = new Button("Teilen");
	
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
	 * Startpunkt
	 */
	
	public void  onLoad() {
		super.onLoad();
		
		/**
		 * Panel für Anordnung der Button
		 */
		HorizontalPanel btnPanel = new HorizontalPanel(); 
		
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
		
		//CSS IDs setzen
		shareUser.getElement().setId("ListBox");
		sharedWithUser.getElement().setId("ListBox");
		receivedFrom.getElement().setId("ListBox");
		
		/*TextBox kann nur Informationen darstellen, verhindert aber deren Bearbeitung*/
		receivedFrom.setEnabled(false);
		
		/**
		 * id für CSS
		 */
		
		//Buttons in CSS
		//gleicher Stylename wie die anderen share-, delete- und save-Buttons
		deleteButton.setStyleName("deleteButton");
		deleteButton.addClickHandler(new DeleteClickHandler());
		
		saveButton.setStyleName("saveButton");
		saveButton.addClickHandler(new SaveClickHandler());
		
		shareButton.setStyleName("shareButton");
		shareButton.addClickHandler(new ShareClickHandler());
		
		//Labels in CSS
		label.setStyleName("ueberschriftlabel");
		labelName.setStyleName("namelabel");
		labelNickName.setStyleName("namelabel");
		
		//Hinzufügbare Eigenschaften gleicher StyleName
		labelFirma.setStyleName("eigenschaftlabel");
		labelTeleNr.setStyleName("eigenschaftlabel");
		labelEmail.setStyleName("eigenschaftlabel");
		labelGeburtsdatum.setStyleName("eigenschaftlabel");
		labelAdresse.setStyleName("eigenschaftlabel");

		isShared.setStyleName("teilenlabel");
		labelShare.setStyleName("teilenlabel");
		labelSharedWith.setStyleName("teilenlabel");
		labelReceiedFrom.setStyleName("teilenlabel");
		contactStatus.setStyleName("contactstatus");
		
		//Textboxen in CSS
		//bekommen alle den gleichen StyleName (wie in ContactLIstForm.java)
		textBoxName.setStyleName("Textbox");
		textBoxNickName.setStyleName("Textbox");
		textBoxFirma.setStyleName("Textbox");
		textBoxTelefonnummer.setStyleName("Textbox");
		textBoxMobilnummer.setStyleName("Textbox");
		textBoxEmail.setStyleName("Textbox");
		textBoxGeburtsdatum.setStyleName("Textbox");
		textBoxAdresse.setStyleName("Textbox");

		
		/**
		 * Anordnung der einzelnen Inhalte für ContactForm.
		 * Abhandlung der Teilhaberschaft.
		 */
		Grid shareGrid = new Grid (4, 3);
		shareGrid.setWidget(0, 0, labelShare);		
		shareGrid.setWidget(0, 1, shareUser);
		shareGrid.setWidget(0, 2, shareButton);
	
		shareGrid.setWidget(1, 0, labelSharedWith);
		shareGrid.setWidget(1, 1, sharedWithUser);
			
		shareGrid.setWidget(2, 0, labelReceiedFrom);
		shareGrid.setWidget(2, 1, receivedFrom);		
		
		/**
		 * Anordnung der Buttons auf einem Button Panel
		 */
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
	
	    /**
	     * Clickhandler zum Überprüfen ob TextBox angeklickt wurde.
	     * Unterstützt darin herauszufinden, ob eine Veränderung in 
	     * TextBox vorgenommen wurde und updated deren Inhalt.
	     * 
	     * @author janina
	     */	
		
		textBoxName.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!saveButton.isEnabled()) saveButton.setEnabled(true);
			if(!checkBox1.isEnabled()) checkBox1.setEnabled(true);
			updateText(textBoxName, textBoxName.getText());
			}
	    });
	    
		textBoxNickName.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			checkBox1.setEnabled(true);
			if(!saveButton.isEnabled()) saveButton.setEnabled(true);
			if(!checkBox2.isEnabled()) checkBox2.setEnabled(true);
			updateText(textBoxNickName, textBoxNickName.getText());
			}
	    });
		
		textBoxFirma.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!saveButton.isEnabled()) saveButton.setEnabled(true);
			if(!checkBox3.isEnabled()) checkBox3.setEnabled(true);
			updateText(textBoxFirma, textBoxFirma.getText());
			}
	    });
		
		textBoxTelefonnummer.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!saveButton.isEnabled()) saveButton.setEnabled(true);
			if(!checkBox4.isEnabled()) checkBox4.setEnabled(true);
			updateText(textBoxTelefonnummer, textBoxTelefonnummer.getText());
			}
	    });
		
		textBoxMobilnummer.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!saveButton.isEnabled()) saveButton.setEnabled(true);
			if(!checkBox5.isEnabled()) checkBox5.setEnabled(true);
			updateText(textBoxMobilnummer, textBoxMobilnummer.getText());
			}
	    });
		
		textBoxEmail.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!saveButton.isEnabled()) saveButton.setEnabled(true);
			if(!checkBox6.isEnabled()) checkBox6.setEnabled(true);
			updateText(textBoxEmail, textBoxEmail.getText());
			}
	    });
		
		textBoxGeburtsdatum.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!saveButton.isEnabled()) saveButton.setEnabled(true);
			if(!checkBox7.isEnabled()) checkBox7.setEnabled(true);
			updateText(textBoxGeburtsdatum, textBoxGeburtsdatum.getText());
			}
	    });
		
		textBoxAdresse.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!saveButton.isEnabled()) saveButton.setEnabled(true);
			if(!checkBox8.isEnabled()) checkBox8.setEnabled(true);
			updateText(textBoxAdresse, textBoxAdresse.getText());
			}
	    });
		
	    /**
	     * ClickHandler Verbindung zu CheckBoxen.
	     * @author janina
	     */
	    
		//TODO: Prüfen ob Abruf hier sinnvoll ist oder in einer einzigen Methode
	    checkBox1.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;	      
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactName = textBoxName.getText();
	    	  if(contactName.isEmpty()) {
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
	    	  if(contactNickName.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  else {
	    		  pv = findCorrectPv(textBoxNickName.getText());
	    	  }
	      }
	    });
	    	  
	    checkBox3.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactFirma = textBoxFirma.getText();
	    	  if(contactFirma.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  else {
	    		  pv = findCorrectPv(textBoxNickName.getText());
	    	  }
	      }
	    });

	    checkBox4.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactPhone = textBoxTelefonnummer.getText();
	    	  if(contactPhone.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  else {
	    		  pv = findCorrectPv(textBoxNickName.getText());
	    	  }
	      }
	    });
	    
	    checkBox5.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactMobile = textBoxMobilnummer.getText();
	    	  if(contactMobile.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  else {
	    		  pv = findCorrectPv(textBoxNickName.getText());
	    	  }
	      }
	    });
	    
	    checkBox6.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactEmail = textBoxEmail.getText();
	    	  if(contactEmail.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  else {
	    		  pv = findCorrectPv(textBoxNickName.getText());
	    	  }
	      }
	    });
	 
	    checkBox7.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactGebuDatum = textBoxGeburtsdatum.getText();
	    	  if(contactGebuDatum.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  else {
	    		  pv = findCorrectPv(textBoxNickName.getText());
	    	  }
	      }
	    });
	    
	    checkBox8.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactAdresse = textBoxAdresse.getText();
	    	  if(contactAdresse.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  else {
	    		  pv = findCorrectPv(textBoxNickName.getText());
	    	  }
	      }
	    });
	       
	     
		/**
		 * Die TextBox um den Kontakt einzugeben, das Label und das Panel für die Buttons werden 
		 * über das Grid Widget untereinander auf dem VerticalPanel angeordnet.
		 * 
		 */		
	    
	    //Grid content = new Grid(4, 2);
	    
		this.add(label);
		this.add(contactGrid);
		this.add(shareGrid);
		this.add(btnPanel);
					
	}		
		
	  /**
	   * Übergabe eines Wertes aus dem Textfeld mit Typ String
	   * und Rückgabe eines dem entsprechend gefundenen PropertyValue Objektes
	   * aus dem übergebenen KontaktObjekt. 
	   * @param string
	   * @return propertyValue
	   * @author janina
	   */
		
	    public PropertyValue findCorrectPv(String s) {
	    	PropertyValue pv = new PropertyValue();
	    	Vector<PropertyValue> result = new Vector<PropertyValue>();
	    	if(s!=null) {
	    		for(PropertyValue p: result) {
	    			if(p.getValue().equals(s)) {
	    				pv = p;
	    			}
	    		}
	    	} return pv;
	    }
	   
		/**
		 * Wenn der anzuzeigende Kontakt gesetzt bzw. gelöscht wird, werden die
		 * zugehörenden Textfelder mit den Informationen aus dem Kontaktobjekt
		 * gefüllt bzw. gelöscht. Dies wird mittels eines Kontakt-Objektes über 
		 * den Aufruf aus dem CellTree an die Methode <code>setSelected()</code> 
		 * übergeben.
		 * @author janina
		 * @param c
		 */
		
		public void setSelected(Contact c) {
			
			if (c != null) {
				contactToDisplay = c;	
				
				if(c.isShared_status()) {
					contactStatus.setText("Status: Geteilt");
				} else { contactStatus.setText("Status: Nicht geteilt");}
				
				label.setText("Kontakt: " + c.getBoId());
												
				Vector <PropertyValue> pv = new Vector <PropertyValue>();
				pv = contactToDisplay.getPropertyValues();
				
				for(PropertyValue p : pv) {	
					if(p!=null) {
						switch(p.getProperty().getId()) {
						case(1):
							textBoxName.setText(p.getValue()); 
							break;
						case(2):
							textBoxNickName.setText(p.getValue()); 
							break;
						case(3):
							textBoxFirma.setText(p.getValue()); 
							break;
						case(4):
							textBoxTelefonnummer.setText(p.getValue()); 
							break;
						case(5):
							textBoxMobilnummer.setText(p.getValue()); 
						 	break;
						case(6):
							textBoxEmail.setText(p.getValue()); 
							break;
						case(7):
							textBoxGeburtsdatum.setText(p.getValue()); 
							break;
						case(8):
							textBoxAdresse.setText(p.getValue()); 
							break;
					}
				}
				
				//Enable Checkboxen falls diese einen Wert enthalten
				if(!textBoxName.getText().isEmpty()) checkBox1.setEnabled(true);		
				if(!textBoxNickName.getText().isEmpty()) checkBox2.setEnabled(true);
				if(!textBoxFirma.getText().isEmpty()) checkBox3.setEnabled(true);
				if(!textBoxTelefonnummer.getText().isEmpty()) checkBox4.setEnabled(true);
				if(!textBoxMobilnummer.getText().isEmpty()) checkBox5.setEnabled(true);
				if(!textBoxEmail.getText().isEmpty()) checkBox6.setEnabled(true);					
				if(!textBoxGeburtsdatum.getText().isEmpty()) checkBox7.setEnabled(true);
				if(!textBoxAdresse.getText().isEmpty()) checkBox8.setEnabled(true);
				
				//Status überprüfen, ob PropertyValues bereits geteilt wurden
				this.setUpCheckBoxValues(c);
				
				//TODO: Überprüfen!! ->> 
				User myself = new User();
				myself.setGoogleID(170);				
				
				//Befüllen der Listbox mit allen User Objekten aus dem System
       			Vector <User> u = new Vector<User>();
       			contactSystemAdmin.getAllUsers(new ShareUserCallback(u));
       			//Befüllen der ListBox mit User Objekten, welche eine Teilhaberschaft haben
       			Vector <Participation> part = new Vector<Participation>();
       			//if(myself is owner) {
       			contactSystemAdmin.getAllParticipationsByBusinessObject(contactToDisplay, new ParticipantUserCallback(part));
       			//}
       			
//       			contactSystemAdmin.getAllParticipationsByOwner(myself, callback);
//       			ListBox shareUser = new ListBox();
//       			ListBox sharedWithUser = new ListBox();
//       			TextBox receivedFrom = new TextBox();
       				
				}
				} else {
				//Löschen eines Kontaktes aus KontaktForm
			    checkBox2.setEnabled(false);
			    checkBox3.setEnabled(false);
			    checkBox4.setEnabled(false);
			    checkBox5.setEnabled(false);
			    checkBox6.setEnabled(false);
			    checkBox7.setEnabled(false);
			    checkBox8.setEnabled(false);
			    
				contactToDisplay = null;
				contactStatus.setText("");
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
		
		 private void updateText(TextBox text, String s) {
			    text.setText(s);
			  }
		 
		 /**
		  * Alle CheckBoxen per default anhacken, wenn diese mit der ausgewählten Person 
		  * bereits geteilt wurden. 
		  * @author janina
		  */
		 private void setUpCheckBoxValues(Contact contact) {
			 
			// User u = getSelectedUser();
			 
			 if(contact.isShared_status()) {
			 Vector<PropertyValue> result = new Vector<PropertyValue>();
			 for(PropertyValue p : result) {				 
			 
				 /*Überprüfen des Status und der Gleichheit der Werte*/
				 if(p.getValue().equals(textBoxName.getText()) 
						 && contactToDisplay.isShared_status()) { //Name muss immer mitgeteilt werden
					 checkBox1.setValue(true);
				 }
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) {
					 checkBox1.setValue(true);
				 }
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) {
					 checkBox1.setValue(true);
				 }
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) {
					 checkBox1.setValue(true);
				 }
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) {
					 checkBox1.setValue(true);
				 }
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) {
					 checkBox1.setValue(true);
				 }
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) {
					 checkBox1.setValue(true);
				 }
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) {
					 checkBox1.setValue(true);
				 }
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) {
					 checkBox1.setValue(true);
				 }
			 	}
			 } else {
				 
			 }
		 }
		 
			/*
			 *  ctvm setter
			 */			
			void setCtvm(ContactTreeViewModel ctvm) {
				this.ctvm = ctvm;
			}
			
			/*
			 * cltvm setter
			 */
			void setCltvm(ContactListTreeViewModel cltvm) {
				this.cltvm = cltvm;
			}
			
			/*
			 * mpvtm setter
			 */	
			void setMptvm(MyParticipationsTreeViewModel mptvm) {
				this.mptvm = mptvm;
			}
			
			/*
			 * mpvtm setter
			 */			
			void setRptvm(ReceivedParticipationTreeViewModel rptvm) {
				this.rptvm = rptvm;
			}
			
			
			public Vector <PropertyValue> getAllUpdatedValues(){				
			
				Vector <PropertyValue> pv = new Vector <PropertyValue>();
				Vector <PropertyValue> result = new Vector <PropertyValue>();
				pv = contactToDisplay.getPropertyValues();
				
				for(PropertyValue p : pv) {	
					
				//Setzen der neuen PropertyValue Werte für Kontakt-Objekt
				if(p.getProperty().getId() == 1 
						&& !textBoxName.getText().isEmpty()) {
					p.setValue(textBoxName.getText()); 
					result.add(p);
				}
				if(p.getProperty().getId() == 2 
						&& !textBoxNickName.getText().isEmpty()) {
					p.setValue(textBoxNickName.getText()); 
					result.add(p);
				}
				if(p.getProperty().getId() == 3 
						&& !textBoxFirma.getText().isEmpty()) {
					p.setValue(textBoxFirma.getText()); 
					result.add(p);
				}
				if(p.getProperty().getId() == 4 
						&& !textBoxTelefonnummer.getText().isEmpty()) {
					p.setValue(textBoxTelefonnummer.getText()); 
					result.add(p);
				}
				if(p.getProperty().getId() == 5 
						&& !textBoxMobilnummer.getText().isEmpty()) {
					p.setValue(textBoxMobilnummer.getText()); 
					result.add(p);
				}
				if(p.getProperty().getId() == 6 
						&& !textBoxEmail.getText().isEmpty()) {
					p.setValue(textBoxEmail.getText()); 
					result.add(p);
				}
				if(p.getProperty().getId() == 7 
						&& !textBoxGeburtsdatum.getText().isEmpty()) {
					p.setValue(textBoxGeburtsdatum.getText()); 
					result.add(p);
				}
				if(p.getProperty().getId() == 8 
						&& !textBoxAdresse.getText().isEmpty()) {
					p.setValue(textBoxAdresse.getText()); 
					result.add(p);
				}
					
				} return result;
			}

			/**
			   * Methode um die Informationen aus den Checkboxen als PropertyValue Objekt
			   * einzusammeln und als Vector zur weiteren Verarbeitung durch den 
			   * entsprechenden Button Aufruf in der Menü Leiste zurück zugeben. 
			   * 
			   * @return Vector <PropertyValue>
			   */


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
						   pv = p;				   
						   result.add(pv);
					   		}
					   	  }
					   } //Checkbox 2 prüfen und Objekt abrufen				   
						if(checkBox2.isChecked()) {		
						for(PropertyValue p : pvList) {
						if(p.getValue().equals(textBoxNickName.getText())) {
							pv = p;
							result.add(pv);	
							}
					   	  }
					   } //Checkbox 3 prüfen und Objekt abrufen					
						if(checkBox3.isChecked()) {		
							for(PropertyValue p : pvList) {
							if(p.getValue().equals(textBoxFirma.getText())) {
								pv = p;
								result.add(pv);	
								}
						   	  }
						   } 	
						//Checkbox 4 prüfen und Objekt abrufen
						if(checkBox3.isChecked()) {		
							for(PropertyValue p : pvList) {
							if(p.getValue().equals(textBoxFirma.getText())) {
								pv = p;
								result.add(pv);
								}
						   	  }
						   } 	
						//Checkbox 5 prüfen und Objekt abrufen
						if(checkBox4.isChecked()) {		
							for(PropertyValue p : pvList) {
							if(p.getValue().equals(textBoxTelefonnummer.getText())) {
								pv = p;
								result.add(pv);
								}
						   	  }
						   } 	
						//Checkbox 6 prüfen und Objekt abrufen
						if(checkBox5.isChecked()) {		
							for(PropertyValue p : pvList) {
							if(p.getValue().equals(textBoxMobilnummer.getText())) {
								pv = p;
								result.add(pv);
								}
						   	  }
						   }
						//Checkbox 7 prüfen und Objekt abrufen
						if(checkBox6.isChecked()) {		
							for(PropertyValue p : pvList) {
							if(p.getValue().equals(textBoxEmail.getText())) {
								pv = p;
								result.add(pv);
								}
						   	  }
						   } 
						//Checkbox 8 prüfen und Objekt abrufen
						if(checkBox7.isChecked()) {		
							for(PropertyValue p : pvList) {
							if(p.getValue().equals(textBoxGeburtsdatum.getText())) {
								pv = p;
								result.add(pv);
								}
						   	  }
						   } 
						//Checkbox 9 prüfen und Objekt abrufen
						if(checkBox8.isChecked()) {		
							for(PropertyValue p : pvList) {
							if(p.getValue().equals(textBoxAdresse.getText())) {
								pv = p;
								result.add(pv);
								}
						   	  }
						   } 
				   }
				   return result;
			}
			
			//TODO: 
			public boolean isOwnedByMe(Contact c) {				
			    owner = new User();
//			   // owner = userInfo
//			   contactSystemAdmin.getAllParticipationsByOwner(owner, new AsyncCallback (owner));
			   return false;
			}
			
			/**
			 * Zum Löschen eines Kontaktes wird zunächst der Eigentümer abgefragt, bevor im
			 * Callback eine Löschung durchgeführt wird.
			 * @author janina
			 */
			private class DeleteClickHandler implements ClickHandler {
				Vector <Participation> part = new Vector <Participation>();
				Vector <PropertyValue> p = new Vector<PropertyValue>();

				@Override
				public void onClick(ClickEvent event) {
					if (contactToDisplay == null) {
						Window.alert("Kein Kontakt ausgewählt!");
					}
					if (contactToDisplay!= null && getAllCheckedValues()!= null){
						p = getAllCheckedValues();
						/*Löschen aller über CheckBoxen ausgewählten Werte*/
						for(PropertyValue pv : p) {
							contactSystemAdmin.deletePropertyValue(pv, new DeleteCallback(pv));
						Window.alert("Die ausgewählten Werte wurden gelöscht.");
						//TODO: Refresh-> setSelected(Contact c)?
						}
					}
					else {
						/*Überprüfen ob Ersteller (Owner)*/
						if(isOwnedByMe(contactToDisplay)) {							
							contactSystemAdmin.deleteContact(contactToDisplay, 
									new DeleteContactCallback(contactToDisplay));
							for(Participation p : part) {
							contactSystemAdmin.deleteParticipation(p, 
									new DeleteMyParticipationCallback(p));
							}
							Window.alert("Der Kontakt" + contactToDisplay.getName() + " wurde gelöscht.");
							/*Wenn nur Teilhaber, dann nur Teilhaberschaft löschen*/
						} else {
							for(Participation p : part) {
								contactSystemAdmin.deleteParticipation(p, 
										new  DeleteReceivedParticipationCallback(p));
								}
							Window.alert("Deine Teilhaberschaft zum Kontakt" 
										+ contactToDisplay.getName() + " wurde gelöscht.");
						}
					}
				}
			}
			
			/**
			 * DeleteCallback Klasse
			 * @author janina
			 *
			 */
			private class DeleteCallback implements AsyncCallback<PropertyValue> {
				
				PropertyValue pv = null;				
				DeleteCallback(PropertyValue pv){
					this.pv = pv;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Löschen der Werte ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(PropertyValue result) {
					if (result != null) {
						//PropertyValues von Kontakt in Liste entfernen
						Vector <PropertyValue> pv = contactToDisplay.getPropertyValues();
						for(PropertyValue p: pv) {
							if(p.equals(result)) pv.remove(p);
						}
						contactSystemAdmin.editContact(contactToDisplay, new EditCallback(contactToDisplay));											
					} else {
						Window.alert("Keine Kontakte gefunden :(");
					}
				}
			}
			
			/**
			 * Callback für Editieren eines Kontaktes
			 * @author janina
			 *
			 */
			
			private class EditCallback implements AsyncCallback<Contact>{
				
				Contact c = null;				
				EditCallback(Contact c){
					this.c = c;
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Editieren des Kontaktes ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Contact result) {
					if (result != null) {
						//Kontakt Objekt aus der Liste löschen
						ctvm.updateContact(contactToDisplay);
					} else {
						Window.alert("Keine Kontakte gefunden :(");
					}
				}
			}
			
			/**
			 * DeleteContactCallback Klasse
			 * @author janina
			 *
			 */
			private class DeleteContactCallback implements AsyncCallback<Contact> {

				Contact c = null;				
				DeleteContactCallback(Contact c){
					this.c = c;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Löschen des Kontaktes ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Contact result) {
					if (result != null) {
						//Kontakt Objekt aus der Liste löschen
						ctvm.removeContact(result);
					} else {
						Window.alert("Keine Kontakte gefunden :(");
					}
				}
			}
			
			/**
			 * Callback Methode zur Löschung meiner Teilhaberschaft
			 * @author janina
			 *
			 */
			private class DeleteMyParticipationCallback implements AsyncCallback<Participation> {

				Participation p = null;				
				DeleteMyParticipationCallback(Participation p){
					this.p = p;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Löschen der Teilhaberschft ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Participation result) {
					if (result != null) {
						//KontaktObjekt aus Teilhaberliste löschen
						mptvm.removeParticipation(result);
						rptvm.removeParticipation(result);
					} else {
						Window.alert("Keine Teilhaberschaft gefunden :(");
					}
				}
			}
			
			/**
			 * Callback Methode zur Löschung der erhaltener Teilhaberschaft
			 * @author janina
			 *
			 */
			private class DeleteReceivedParticipationCallback implements AsyncCallback<Participation> {

				Participation p = null;				
				DeleteReceivedParticipationCallback(Participation p){
					this.p = p;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Löschen der Teilhaberschft ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Participation result) {
					if (result != null) {
						//KontaktObjekt aus Teilhaberliste löschen
						rptvm.removeParticipation(result);
					} else {
						Window.alert("Keine Teilhaberschaft gefunden :(");
					}
				}
			}
			
			/**
			 * SaveClickHandler zum Speichern eines Kontaktes oder dessen Update.
			 * @author janin
			 *
			 */
			private class SaveClickHandler implements ClickHandler {
				
				Vector <PropertyValue> p = new Vector<PropertyValue>();

				@Override
				public void onClick(ClickEvent event) {
					if (contactToDisplay == null) {
						Window.alert("Kein Kontakt ausgewählt");
					} 
					if (contactToDisplay!= null && getAllCheckedValues()!= null){
						p = getAllUpdatedValues();
						contactToDisplay.setPropertyValues(p);
						contactSystemAdmin.editContact(contactToDisplay, new SaveCallback(contactToDisplay));						
				}
				
			}
			}
			
			/**
			 * Callback Methode zur Speicherung eines Kontaktes oder dessen Update. 
			 * @author janina
			 *
			 */
			private class SaveCallback implements AsyncCallback<Contact> {

				Contact c = null;				
				SaveCallback(Contact c){
					this.c = c;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Speichern des Kontaktes ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Contact result) {
					if (result != null) {
						//KontaktObjekt updaten
						ctvm.updateContact(result);
					} else {
						Window.alert("Kein Kontakt gefunden :(");
					}
				}
			}
			
			/**
			 * UserCallnack Klasse zum befüllen der ListBox mit User -Objekten 
			 * aus dem System. 
			 * @author janina
			 *
			 */
			
			private class ShareUserCallback implements AsyncCallback<Vector<User>>{
				Vector <User> user = null;				
				ShareUserCallback(Vector<User> user){
					this.user = user;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Aufruf der Nutzer ist misglückt! :(");
				}

				@Override
				public void onSuccess(Vector <User> result) {
					int count = 0;
					if (result != null) {
						for(User user: result) {						
						//User Liste updaten
						shareUser.addItem(user.getUserContact().getName().getValue() + " , " + user.getGMail());	
						++count;
						}
					//Genug Platz schaffen für alle Elemente
					shareUser.setVisibleItemCount(count);
					} else {
						Window.alert("Kein Nutzer gefunden :(");
					}
				}
			}
			
		
			private class ParticipantUserCallback implements AsyncCallback<Vector<Participation>>{
				
				Vector <Participation> part = null;				
				ParticipantUserCallback(Vector<Participation> part){
					this.part = part;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Aufruf der Teilhaber ist misglückt! :(");
				}

				@Override
				public void onSuccess(Vector<Participation> result) {
						if (result != null) {
						for(Participation p: result) {						
						//User Liste updaten
						sharedWithUser.addItem(p.getParticipant().getUserContact().getName().getValue() + " , " + p.getParticipant().getGMail());	
						++count;
						}
					//Genug Platz schaffen für alle Elemente
					shareUser.setVisibleItemCount(count);
					} else {
						Window.alert("Kein Teilhaber gefunden :(");
					}
				}
			}
			
			/**
			 * ShareClickHandler zum Teilen eines Kontaktes und/oder einer Eigenschaft
			 * zu dem bereits geteilten Kontakt mit einem bestimmten Nutzer.
			 * @author janina
			 *
			 */
			private class ShareClickHandler implements ClickHandler {				
				Vector <PropertyValue> pv = new Vector<PropertyValue>();				
				int index = shareUser.getSelectedIndex();
				
				@Override
				public void onClick(ClickEvent event) {
					if (shareUser.getSelectedIndex() == -1) { //gibt -1 zurück, wenn nichts ausgewaehlt wurde
						Window.alert("Kein Nutzer zum teilen ausgewählt :( ...");
					} 
					if (getAllCheckedValues()== null) { 
						Window.alert("Keine Werte zum teilen ausgewählt :( ...");
					} 
					
					if((shareUser.getSelectedIndex() == -1) & (getAllCheckedValues()!= null)){
						pv = getAllCheckedValues();
						String s = shareUser.getItemText(index);
						String [] email = s.split(",", 2); //Email als eindeutiger Identifier aus String auslesen
						contactToDisplay.setPropertyValues(pv); //Geänderte Werte abrufen
						contactSystemAdmin.editContact(contactToDisplay, new SaveCallback(contactToDisplay));
						
						User share = new User();
						contactSystemAdmin.getUserBygMail(email[1], new UserCallback(share));
						share = sharedFor;
						Participation part = new Participation();
						part.setParticipant(share);
						part.setReference(contactToDisplay);
						contactSystemAdmin.createParticipation(part, new ParticipationCallback(part)); //Neuen Teilhaber hinzufügen
				}				
			}
			}
			
			/**
			 * Callback Methode zur Speicherung eines Kontaktes oder dessen Update. 
			 * @author janina
			 *
			 */
			private class ShareCallback implements AsyncCallback<Contact> {

				Contact c = null;				
				ShareCallback(Contact c){
					this.c = c;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Speichern des Kontaktes ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Contact result) {
					if (result != null) {
						//KontaktObjekt updaten
						ctvm.updateContact(result);
					} else {
						Window.alert("Kein Kontakt gefunden :(");
					}
				}
			}
			
			
			private class UserCallback implements AsyncCallback<User> {

				User u = null;				
				UserCallback(User u){
					this.u = u;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Oups ein Fehler ist aufgetreten! :(");
				}

				@Override
				public void onSuccess(User result) {
					if (result != null) {
						sharedFor = result;
					} else {
						Window.alert("Kein Nutzer wurde gefunden :(");
					}
				}
			}
			
			
			private class ParticipationCallback implements AsyncCallback<Participation>{
								
				Participation part = null;				
				ParticipationCallback(Participation part){
					this.part = part;
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Oups ein Fehler ist aufgetreten! :(");					
				}

				@Override
				public void onSuccess(Participation result) {
						
						if (result != null) {
							sharedWithUser.addItem(result.getParticipant().getUserContact().getName().getValue() + " , " + result.getParticipant().getGMail());	
							count++;
							}
						//Genug Platz schaffen für alle Elemente
						shareUser.setVisibleItemCount(count);
						
						Window.alert("Du hast hast " + result.getParticipant().getUserContact().getName().getValue() + 
								" erfolgreich zum Teilhaber gemacht.");	
						//TODO: zur ListBox hinzufügen
					}
					
				}
				
			
		  
}