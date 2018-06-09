package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.gargoylesoftware.htmlunit.javascript.host.Popup;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
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
	
	//Referenzvariable zum Speichern eines Participant
	User accountOwner = null; //Eingeloggter User
	User sharedUser = null; //User zum teilen ausgewählt
	Vector <User> contactParticipants = null; //Teilhaber eines Kontaktes 
	
	//TODO: Owner im System abrufen
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
	
	//PopUp
	DialogBox popUp = new DialogBox();
	
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
	
	Label deleteMessage = new Label("Soll der Kontakt gelöscht werden?");
		
	Button deleteButton = new Button("Löschen");
	Button saveButton = new Button("Speichern");
	Button shareButton = new Button("Teilen");
	
//	CheckBox checkBox1 = new CheckBox();
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
		 * id für CSS
		 */		
		//Buttons in CSS
		//gleicher Stylename wie die anderen share-, delete- und save-Buttons

		deleteButton.removeStyleName("gwt-Button"); //um den von GWT f�r Buttons vorgegebenen Style zu loeschen
		deleteButton.getElement().setId("deleteButton");
		deleteButton.addClickHandler(new DeleteClickHandler());

		saveButton.removeStyleName("gwt-Button"); //um den von GWT f�r Buttons vorgegebenen Style zu l�schen

		saveButton.getElement().setId("saveButton");
		saveButton.addClickHandler(new SaveClickHandler());
		
		shareButton.removeStyleName("gwt-Button"); //um den von GWT f�r Buttons vorgegebenen Style zu l�schen
		shareButton.getElement().setId("shareButton");
		shareButton.addClickHandler(new ShareClickHandler());
		
		
		//Labels in CSS
		label.getElement().setId("ueberschriftlabel");
		labelName.getElement().setId("namelabel");
		labelNickName.getElement().setId("namelabel");
		
		//Hinzufügbare Eigenschaften gleicher StyleName
		labelFirma.getElement().setId("eigenschaftlabel");
		labelTeleNr.getElement().setId("eigenschaftlabel");
		labelMobilNr.getElement().setId("eigenschaftlabel");
		labelEmail.getElement().setId("eigenschaftlabel");
		labelGeburtsdatum.getElement().setId("eigenschaftlabel");
		labelAdresse.getElement().setId("eigenschaftlabel");

		isShared.getElement().setId("geteiltlabel");
		labelShare.getElement().setId("teilenlabel");
		labelSharedWith.getElement().setId("teilenlabel");
		labelReceivedFrom.getElement().setId("teilenlabel");
		contactStatus.getElement().setId("contactstatus");
		
		//Textboxen in CSS
		//bekommen alle den gleichen StyleName (wie in ContactLIstForm.java)
		textBoxName.getElement().setId("Textbox");
		textBoxNickName.getElement().setId("Textbox");
		textBoxFirma.getElement().setId("Textbox");
		textBoxTelefonnummer.getElement().setId("Textbox");
		textBoxMobilnummer.getElement().setId("Textbox");
		textBoxEmail.getElement().setId("Textbox");
		textBoxGeburtsdatum.getElement().setId("Textbox");
		textBoxAdresse.getElement().setId("Textbox");
		
		//Pop in CSS
		popUp.getElement().setId("PopUp");
		
		//ListBox CSS
		receivedFrom.getElement().setId("teilenmitlistbox");//.setId("geteiltvontextfeld");
		shareUser.getElement().setId("teilenmitlistbox");//.setId("geteiltmitlistbox");
		sharedWithUser.getElement().setId("teilenmitlistbox");
	    
		shareUser.getElement().setId("ListBox");
		receivedFrom.getElement().setId("ListBox");
		labelSharedWith.getElement().setId("ListBox");
				
		//Checkboxen CSS
//		checkBox1.getElement().setId("checkBox");
		checkBox2.getElement().setId("checkBox");
		checkBox3.getElement().setId("checkBox");
		checkBox4.getElement().setId("checkBox");
		checkBox5.getElement().setId("checkBox");
		checkBox6.getElement().setId("checkBox");
		checkBox7.getElement().setId("checkBox");
		checkBox8.getElement().setId("checkBox");		
		
		/**
		 * CheckBoxen für das Teilen einzelner Elemente einer ContactForm 
		 * per Default auf "false" setzen.
		 */
//		checkBox1.setEnabled(false);
	    checkBox2.setEnabled(false);
	    checkBox3.setEnabled(false);
	    checkBox4.setEnabled(false);
	    checkBox5.setEnabled(false);
	    checkBox6.setEnabled(false);
	    checkBox7.setEnabled(false);
	    checkBox8.setEnabled(false);
		
		/**
		 * Panel für Anordnung der Button
		 */
		HorizontalPanel btnPanel = new HorizontalPanel(); 
		
		 /**
	     * Clickhandler zum Überprüfen ob TextBox angeklickt wurde.
	     * Unterstützt darin herauszufinden, ob eine Veränderung in 
	     * TextBox vorgenommen wurde und updated deren Inhalt.	     
	     * @author Janina
	     */	
		
	    //TODO: Prüfen -> onClick Methode wird nicht ausgelöst
		textBoxName.addClickHandler(new ClickHandler() {	    
			@Override
			public void onClick(ClickEvent event) {
//			if(!checkBox1.isEnabled()) {
//				checkBox1.setEnabled(true);
//			}
			updateText(textBoxName, textBoxName.getText());
			}
	    });
	    
		textBoxNickName.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!checkBox2.isEnabled()) {
				checkBox2.setEnabled(true);
			}
			updateText(textBoxNickName, textBoxNickName.getText());
			}
	    });
		
		textBoxFirma.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!checkBox3.isEnabled()) {
				checkBox3.setEnabled(true);
			}
			updateText(textBoxFirma, textBoxFirma.getText());
			}
	    });
		
		textBoxTelefonnummer.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!checkBox4.isEnabled()) {
				checkBox4.setEnabled(true);
			}
			updateText(textBoxTelefonnummer, textBoxTelefonnummer.getText());
			}
	    });
		
		textBoxMobilnummer.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!checkBox5.isEnabled()) {
				checkBox5.setEnabled(true);
			}
			updateText(textBoxMobilnummer, textBoxMobilnummer.getText());
			}
	    });
		
		textBoxEmail.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!checkBox6.isEnabled()) {
				checkBox6.setEnabled(true);
			}
			updateText(textBoxEmail, textBoxEmail.getText());
			}
	    });
		
		textBoxGeburtsdatum.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!checkBox7.isEnabled()) {
				checkBox7.setEnabled(true);
			}
			updateText(textBoxGeburtsdatum, textBoxGeburtsdatum.getText());
			}
	    });
		
		textBoxAdresse.addClickHandler(new ClickHandler() {	        
			@Override
			public void onClick(ClickEvent event) {
			if(!checkBox8.isEnabled()) {
				checkBox8.setEnabled(true);
			}
			updateText(textBoxAdresse, textBoxAdresse.getText());
			}
	    });
		
	    /**
	     * ClickHandler Verbindung zu CheckBoxen.
	     * @author janina
	     */
	    
		//TODO: Prüfen ob Abruf hier sinnvoll ist oder in einer einzigen Methode
//	    checkBox1.addClickHandler(new ClickHandler() {
//	      
//	      @Override
//	      public void onClick(ClickEvent event) {
//	    	  if(textBoxName.getText().isEmpty()) {
//	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
//	    	  }
//	    	  else {
//	    		  //TODO:
//	    	  }	    	  
//	      }
//	    });	    
	    
	    checkBox2.addClickHandler(new ClickHandler() {
	     	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  if(textBoxName.getText().isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    	  
	    checkBox3.addClickHandler(new ClickHandler() {
	      	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  if(textBoxFirma.getText().isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });

	    checkBox4.addClickHandler(new ClickHandler() {
	      
	      @Override
	      public void onClick(ClickEvent event) {
	    	  if(textBoxTelefonnummer.getText().isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox5.addClickHandler(new ClickHandler() {
	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  if(textBoxMobilnummer.getText().isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox6.addClickHandler(new ClickHandler() {
	     
	      @Override
	      public void onClick(ClickEvent event) {
	    	  if(textBoxEmail.getText().isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	 
	    checkBox7.addClickHandler(new ClickHandler() {
	     	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  if(textBoxGeburtsdatum.getText().isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox8.addClickHandler(new ClickHandler() {
	     	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  if(textBoxAdresse.getText().isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	
		//PopUp DialogBox aufsetzen
		Button okBtn = new Button("OK");
		okBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				contactSystemAdmin.deleteContact(contactToDisplay, new DeleteContactCallback());
			popUp.hide();
			}		    	
		});
		    
		Button cancelBtn = new Button("Abbrechen");
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
				public void onClick(ClickEvent event) {
					popUp.hide();				
				}		    	
		});
		    
		popUp.setAnimationEnabled(true);
		popUp.setGlassEnabled(true);
		    
		popUp.add(deleteMessage);
		popUp.add(cancelBtn);
		popUp.add(okBtn);		    
		popUp.hide();
	    
		//ListBox ist nicht sichtbar
		labelShare.setVisible(false);
		shareUser.setVisible(false);
		
		labelReceivedFrom.setVisible(false);
		receivedFrom.setVisible(false);
		
		sharedWithUser.setVisible(false);
		labelSharedWith.setVisible(false);
		
		/**
		 * Anordnung der einzelnen Inhalte für ContactForm.
		 * 
		 */
		Grid contactGrid = new Grid(12, 3);
				
		contactGrid.setWidget(0, 0, label);
		contactGrid.setWidget(0, 1, contactStatus);
		contactGrid.setWidget(0, 2, isShared);
		
		contactGrid.setWidget(1, 0, labelName);
		contactGrid.setWidget(1, 1, textBoxName);
//		contactGrid.setWidget(1, 2, checkBox1); Entwicklerentscheidung -> Name muss immer vorhanden sein 
				
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
		
		contactGrid.setWidget(9, 0, labelReceivedFrom);
		contactGrid.setWidget(9, 1, receivedFrom);
		
		contactGrid.setWidget(10, 0, labelSharedWith);
		contactGrid.setWidget(10, 1, sharedWithUser);
		
		contactGrid.setWidget(11, 0, labelShare);
		contactGrid.setWidget(11, 1, shareUser);
		
		/**
		 * Anordnung der Buttons auf einem Button Panel
		 */
		btnPanel.add(saveButton);
		btnPanel.add(shareButton);
		btnPanel.add(deleteButton);		   
	     
		/**
		 * Die TextBox um den Kontakt einzugeben, das Label und das Panel für die Buttons werden 
		 * über das Grid Widget untereinander auf dem VerticalPanel angeordnet.
		 * 
		 */		
	    this.add(popUp);	    
		this.add(label);
		this.add(contactGrid);
		this.add(btnPanel);
					
	}		
		

	
		/**
		 * Wenn der anzuzeigende Kontakt gesetzt bzw. gelöscht wird, werden die
		 * zugehörenden Textfelder mit den Informationen aus dem Kontaktobjekt
		 * gefüllt bzw. gelöscht. Dies wird mittels eines Kontakt-Objektes über 
		 * den Aufruf aus dem CellTree an die Methode <code>setSelected()</code> 
		 * übergeben.
		 * @author janina
		 * @param contact Objekt 
		 */
		
		public void setSelected(Contact c) {
			
		if (c != null) {
				contactToDisplay = c;	
				
				if(c.isShared_status()) {
					contactStatus.setText("Status: Geteilt");
				} else { contactStatus.setText("Status: Nicht geteilt");
				}				
				
				label.setText("Kontakt ID: " + c.getBoId());
												
				Vector <PropertyValue> pv = new Vector <PropertyValue>();
				pv = contactToDisplay.getPropertyValues();
				
				for(PropertyValue p : pv) {						
					if(p!= null) this.setUpTextBoxes(p);
				}
				//Status überprüfen, ob PropertyValues bereits geteilt wurden
				this.setUpCheckBoxValues(c);	
				//CheckBox auf enable setzen, falls Wert in TextBox vorhanden
				this.setUpCheckBoxes();
				
				//Befüllen der Listbox mit allen User Objekten aus dem System
				labelShare.setVisible(true);
				shareUser.setVisible(true);
				shareUser.clear(); //Löschen alter Einträge
				
       			contactSystemAdmin.getAllUsers(new UserToShareCallback());
       			//Befüllen der ListBox mit User Objekten, welche eine Teilhaberschaft haben
      			
       			if(isOwnedByMe(c)) { 
       				labelSharedWith.setVisible(true);
       				sharedWithUser.setVisible(false);
       				sharedWithUser.clear(); //Löschen alter Einträge
       				
       				contactSystemAdmin.getAllParticipationsByOwner(c.getOwner(), new ParticipantCallback());
       			}
       			else { //Wenn User nicht selbst Ersteller ist, dann wird ihm dieser dargestellt
       				labelReceivedFrom.setVisible(true);
       				receivedFrom.setVisible(true);
       				
       				receivedFrom.clear(); //Löschen alter Einträge
       				receivedFrom.addItem(c.getOwner().getUserContact().getName().getValue() + " , " 
       					+ c.getOwner().getGMail());
       				receivedFrom.setVisibleItemCount(receivedFrom.getItemCount()); //Platz schaffen für alle Elemente   		
       			 } 
       	 } else {
       			
       			//Kontaktreferenz 
    			contactToDisplay = null;
    				
				//Löschen eines Kontaktes aus KontaktForm			    
			    checkBox2.setValue(false, false);
			    checkBox2.setEnabled(false);			    
			    
			    checkBox3.setValue(false, false);
			    checkBox3.setEnabled(false);			    
			    
			    checkBox4.setValue(false, false);
			    checkBox4.setEnabled(false);
			    
			    checkBox5.setValue(false, false);
			    checkBox5.setEnabled(false);			    
			    
			    checkBox6.setValue(false, false);
			    checkBox6.setEnabled(false);			    
			    
			    checkBox7.setValue(false, false);
			    checkBox7.setEnabled(false);			    
			    
			    checkBox8.setValue(false, false);
			    checkBox8.setEnabled(false);			    
			   
				//Alle angezeigten Werte zurück setzen
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
				
				//ListBoxen werden ausgeblendet
				labelShare.setVisible(false);
				shareUser.setVisible(false);
				
				labelReceivedFrom.setVisible(false);
				receivedFrom.setVisible(false);
				
				sharedWithUser.setVisible(false);
				labelSharedWith.setVisible(false);				
				
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
		
		
		 private void updateText(TextBox text, String s) {
			    text.setText(s);
			  }
		 
		 /**
		  * Methode zur Befüllung der TextBox Elemente mit bereits vorhandenen PropertyValues,
		  * oder zur der Leerung alter Werte, wenn keine Werte vorhanden sind. 
		  * @param p
		  */
		 private void setUpTextBoxes(PropertyValue p) {
			//Befüllung neuer Werte	
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

		 }
		 
		 public void cleanUpTextBoxes(PropertyValue p) {
			 
		 }
		 
		 public void setUpCheckBoxes() {
			    //Enable Checkboxen falls die Textbox bereits einen Wert enthält
//				if(!textBoxName.getText().isEmpty()) {
//					checkBox1.setEnabled(true);
//					}
				if(!textBoxNickName.getText().isEmpty()) {
					checkBox2.setEnabled(true);
					}
				if(!textBoxFirma.getText().isEmpty()) {
					checkBox3.setEnabled(true);
					}
				if(!textBoxTelefonnummer.getText().isEmpty()) {
					checkBox4.setEnabled(true);
					}
				if(!textBoxMobilnummer.getText().isEmpty()) {
					checkBox5.setEnabled(true);
					}
				if(!textBoxEmail.getText().isEmpty()) {
				    checkBox6.setEnabled(true);
				    }				
				if(!textBoxGeburtsdatum.getText().isEmpty()) {
					checkBox7.setEnabled(true);
					}
				if(!textBoxAdresse.getText().isEmpty()) {
					checkBox8.setEnabled(true);
					}
		 }
		 
		 /**
		  * Alle CheckBoxen per default anhacken, wenn diese mit der ausgewählten Person 
		  * bereits geteilt wurden. 
		  * @author janina
		  */
		 private void setUpCheckBoxValues(Contact contact) {			 		 
			 if(contact.isShared_status()) {
			 for(PropertyValue p : contact.getPropertyValues()) {				 
				 switch(p.getProperty().getId()) {
					case(1):
//						checkBox1.setEnabled(true);					
//						if(p.isShared_status())checkBox1.setValue(true, true);
//						break;
					case(2):
						checkBox2.setEnabled(true);
						if(p.isShared_status())checkBox2.setValue(true, true);
						break;
					case(3):
						checkBox3.setEnabled(true);
						if(p.isShared_status())checkBox3.setValue(true, true);
						break;
					case(4):
						checkBox4.setEnabled(true);
						if(p.isShared_status())checkBox4.setValue(true, true);
						break;
					case(5):
						checkBox5.setEnabled(true);
						if(p.isShared_status())checkBox5.setValue(true, true);
					 	break;
					case(6):
						checkBox6.setEnabled(true);
						if(p.isShared_status())checkBox6.setValue(true, true);
						break;
					case(7):
						checkBox7.setEnabled(true);
						if(p.isShared_status())checkBox7.setValue(true, true);
						break;
					case(8):
						checkBox8.setEnabled(true);
						if(p.isShared_status())checkBox8.setValue(true, true);
						break;
				}
			  }
			 }
		 }
		 
		/**
		 * Abruf aller PropertyValue Werte. Welche bei einer bestimmten Property-ID
		 * in der TextBox eingegeben wurden oder bereits im Kontakt vorhanden sind. 
		 * @author Janina
		 * @return
		 */			
			@SuppressWarnings("unused")
			public Vector <PropertyValue> getCheckedValues(){				
			 
				Vector <PropertyValue> pv = new Vector <PropertyValue>();
				pv = contactToDisplay.getPropertyValues();	
				Vector <PropertyValue> result = new Vector<PropertyValue>();
				PropertyValue prop = null;
										
				for(PropertyValue p : pv) {
				switch(p.getProperty().getId()) {
						case(1): //Keine Checkbox, da Name nicht einzeln geteilt werden kann
							p.setValue(textBoxName.getText());
							break;
						case(2):
							if(checkBox2.getValue()) {
							if(p!=null) {
								p.setValue(textBoxNickName.getText());
								result.add(p);
								}
							else {prop = new PropertyValue(textBoxName.getText());
								result.add(prop);
								}							
							} break;
						case(3):
							if(checkBox3.getValue()) {
							if(p!=null) {
								p.setValue(textBoxFirma.getText());
								result.add(p);
								}
							else {prop = new PropertyValue(textBoxFirma.getText());
								result.add(prop);
								}							
							} break;
						case(4):
							if(checkBox4.getValue()) {
							if(p!=null) {
								p.setValue(textBoxTelefonnummer.getText());
								result.add(p);
								}
							else {prop = new PropertyValue(textBoxTelefonnummer.getText());
								result.add(prop);
								}							
							} break;
						case(5):
							if(checkBox5.getValue()) {
							if(p!=null) {
								p.setValue(textBoxMobilnummer.getText());
								result.add(p);
								}
							else {prop = new PropertyValue(textBoxMobilnummer.getText());
								result.add(prop);
								}						 	
							} break;
						case(6):
							if(checkBox6.getValue()) {
							if(p!=null) {
								p.setValue(textBoxEmail.getText());
								result.add(p);}
							else {prop = new PropertyValue(textBoxEmail.getText());
								result.add(prop); 
								}
							} break;
						case(7):
							if(checkBox7.getValue()) {
							if(p!=null) {
								p.setValue(textBoxGeburtsdatum.getText());
								result.add(p);}
							else {prop = new PropertyValue(textBoxGeburtsdatum.getText());
								result.add(prop);
								}
							} break;
						case(8):
							if(checkBox8.getValue()) {
							if(p!=null) {
								p.setValue(textBoxAdresse.getText());
								result.add(p);
								}
							else {prop = new PropertyValue(textBoxAdresse.getText());
								result.add(prop);
								}
							}break;
										
						}
					}
				 return result;
				}

			
			/**
			 * Abrufen des Users TODO
			 * @param c
			 * @return
			 */
			public boolean isOwnedByMe(Contact c) {				
			   contactSystemAdmin.getAccountOwner(new OwnerCallback());
			   if(accountOwner.equals(c.getOwner())){
				   return true;
			   }
			   return false;
			}
			
			/**
			 * Methode um aus der ListBox über Split Operation die Email Adresse eines ausgewählten
			 * Users herauszufinden und anhand dieser ein User Objekt aus der DB abzurufen. 
			 * @author Janina
			 * @return
			 */
			public User getUserFromList() {
				if(shareUser.getSelectedIndex()!= -1) {
					String s = shareUser.getSelectedValue();
					String [] s2= s.split(",");
					contactSystemAdmin.getUserBygMail(s2[1], new SharedUserCallback());
				}
				return null;
				
			}
			

			/**
			 * Zum Löschen eines Kontaktes wird zunächst der Eigentümer abgefragt, bevor im
			 * Callback eine Löschung durchgeführt wird.
			 * @author Janina
			 */
			private class DeleteClickHandler implements ClickHandler {
				Vector <PropertyValue> p = new Vector<PropertyValue>();
				Participation part = new Participation();
				
				@Override
				public void onClick(ClickEvent event) {
					if (contactToDisplay != null) {		
					//Prüfe ob Werte abgehackt wurden
					if (getCheckedValues()!= null){ 
						p = getCheckedValues(); //holt alle PVs der ausgewählten Checkboxen 
						/*Löschen aller über CheckBoxen ausgewählten Werte*/
						for(PropertyValue pv : p) {
							contactSystemAdmin.deletePropertyValue(pv, new DeletePVCallback());
						Window.alert("Die ausgewählten Werte wurden gelöscht.");
						
						}
					}
					//Wenn keine Werte abgehackt wurden lösche gesamten Kontakt
					else {
						//Überprüfen ob Ersteller (Owner)
						if(isOwnedByMe(contactToDisplay)) {	
							 //PopUp anzeigen
							 popUp.show();
							 Window.alert("Der Kontakt" + contactToDisplay.getName() + " wurde gelöscht.");							
						//Wenn nur Teilhaber, dann nur Teilhaberschaft löschen -> damit kein Zugriff mehr auf Kontakt
						} else {							
							part.setReference(contactToDisplay);
							part.setParticipant(accountOwner);
								contactSystemAdmin.deleteParticipation(part, 
										new DeleteReceivedParticipationCallback());
								}
							Window.alert("Deine Teilhaberschaft zum Kontakt" 
										+ contactToDisplay.getName() + " wurde gelöscht.");
						}
					} else {
					Window.alert("Kein Kontakt ausgewählt!");
				 } 
				}
			}
			
			
			/**
			 * DeleteCallback zur Löschung einzelner PropertyValues.
			 * @author janina
			 *
			 */
			private class DeletePVCallback implements AsyncCallback<PropertyValue> {

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
							if(p.equals(result)) pv.remove(p); //Löschen eines Pv Objekts aus Vektor
						}
						contactSystemAdmin.editContact(contactToDisplay, new EditContactCallback());	
						contactToDisplay.setPropertyValues(pv);
						//Update ContactForm Darstellung
						setSelected(contactToDisplay);
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

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Löschen der Teilhaberschft ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Participation result) {
					if (result != null) {
						//KontaktObjekt aus Listen
						ctvm.removeContact(result.getReferencedObject());
						mptvm.removeParticipation(result);
						
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

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Löschen der Teilhaberschft ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Participation result) {
					if (result != null) {
						//Contact-Objekt aus Listen löschen
						ctvm.removeContact(result.getReferencedObject());
						rptvm.removeParticipation(result);
					} else {
						Window.alert("Keine Teilhaberschaft gefunden :(");
					}
				}
			}
			
			/**
			 * Callback für das Editieren und speichern eines bereits vorhandenen Kontaktes.
			 * @author janina
			 *
			 */
			
			private class EditContactCallback implements AsyncCallback<Contact>{
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Editieren des Kontaktes ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Contact result) {
					if (result != null) {
						//Kontakt Objekte in Liste updaten analog zu speichern
						ctvm.updateContact(contactToDisplay);
						setSelected(contactToDisplay);
					} else {
						Window.alert("Keine Kontakte gefunden :(");
					}
				}
			}
			
			
			private class EditParticipationCallback implements AsyncCallback<Participation>{

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Editieren der Teilhaberschaft ist fehlgeschlagen! :(");					
				}

				@Override
				public void onSuccess(Participation result) {
					mptvm.update(result.getReferencedObject());					
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
					if (contactToDisplay!= null && getCheckedValues()!= null){
						p = getCheckedValues();
						contactToDisplay.setPropertyValues(p);
						contactSystemAdmin.editContact(contactToDisplay, new SaveCallback());						
					} else {
					Window.alert("Kein Kontakt ausgewählt");
					}			
				}
			}
			
			/**
			 * Callback Methode zur Speicherung eines Kontaktes oder dessen Update. 
			 * @author Janina
			 *
			 */
			private class SaveCallback implements AsyncCallback<Contact> {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Speichern des Kontaktes ist fehlgeschlagen! :(");
				}

				@Override
				public void onSuccess(Contact result) {
					if (result != null) {
						//KontaktObjekt updaten
						ctvm.updateContact(result);
						setSelected(contactToDisplay);
					} else {
						Window.alert("Oups da ist was schief gelaufen! :(");
					}
				}
			}
			
			/**
			 * Abruf eines bestimmten Users, um diesen aus der ListBox
			 * zurück zu transferieren als User-Objekt zur weiteren Verarbeitung. 
			 * @author janin
			 *
			 */
			
			private class SharedUserCallback implements AsyncCallback<User>{

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Nutzer Abruf ist misglückt! :(");					
				}
				
				@Override
				public void onSuccess(User result) {
					if(result != null) {
					sharedUser = result;
					} else {
						Window.alert("Der Nutzer " + result.getGoogleID() + " konnte nicht gefunden werden :(");
					}
				}
			}
			
			/**
			 * Abruf eines bestimmten Users, um diesen aus der ListBox
			 * zurück zu transferieren als User-Objekt zur weiteren Verarbeitung. 
			 * @author janin
			 *
			 */
			
			private class OwnerCallback implements AsyncCallback<User>{

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Nutzer Abruf ist misglückt! :(");					
				}

				@Override
				public void onSuccess(User result) {
					if(result != null) {
						accountOwner = result; //Owner Referenzattribut befüllen
					} else {
						Window.alert("Der Nutzer " + result.getGoogleID() + " konnte nicht gefunden werden :(");
					}
				}				
			}
			
			
			/**
			 * UserCallnack Klasse zum befüllen der ListBox mit User -Objekten 
			 * aus dem System. 
			 * @author janina
			 *
			 */
			
			private class UserToShareCallback implements AsyncCallback<Vector<User>>{

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Nutzer Abruf ist misglückt! :(");
				}

				@Override
				public void onSuccess(Vector <User> result) {					
					if (result != null) {
						for(User user: result) {						
						//User Liste updaten
						shareUser.addItem(user.getUserContact().getName().getValue() + " , " + user.getGMail());
					}
					//Genug Platz schaffen für alle Elemente
					shareUser.setVisibleItemCount(result.size());
					} else {
						Window.alert("Kein Nutzer gefunden :(");
					}
				}
			}
			
		
			private class ParticipantCallback implements AsyncCallback<Vector<Participation>>{

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Abruf der Teilhaber ist misglückt! :(");
				}

				@Override
				public void onSuccess(Vector<Participation> result) {					
					if (result != null) {
						for(Participation p: result) {						
						   //User Liste updaten
						   sharedWithUser.addItem(p.getParticipant().getUserContact().getName().getValue() + " , " + p.getParticipant().getGMail());	
						   contactParticipants.add(p.getParticipant());
						}
					    //Genug Platz schaffen für alle Elemente
						sharedWithUser.setVisibleItemCount(result.size());
					} else {
						Window.alert("Kein Teilhaber gefunden :(");
					}
				}
			}
			
			
			private class ParticipationsByBOCallback implements AsyncCallback<Vector<Participation>>{
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Abruf der Teilhaber ist misglückt! :(");					
				}

				@Override
				public void onSuccess(Vector<Participation> result) {
					if(result != null) {
						for(Participation p : result) {
							
						}
					}
					
				} 
				
			}
			
			private class CreateParticipationCallback implements AsyncCallback<Participation>{

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Kontakt konnte nicht geteilt werden. :(");						
				}

				@Override
				public void onSuccess(Participation result) {
					mptvm.addContact(result.getReferencedObject());					
				}				
			}
			
			/**
			 * ShareClickHandler zum Teilen eines Kontaktes und/oder einer Eigenschaft
			 * zu dem bereits geteilten Kontakt mit einem bestimmten Nutzer.
			 * @author janina
			 *
			 */
			private class ShareClickHandler implements ClickHandler {				
				Vector <PropertyValue> p = new Vector<PropertyValue>();
				@Override
				public void onClick(ClickEvent event) {				
					if (contactToDisplay!= null & getCheckedValues()!= null){
						contactToDisplay.setPropertyValues(getCheckedValues()); //Abrufen der ausgewählten Werte
						contactSystemAdmin.editContact(contactToDisplay, new SaveCallback()); //Kontakt wird upgedated in DB
						
						contactSystemAdmin.getAllParticipationsByBusinessObject(contactToDisplay, new ParticipationsByBOCallback());
						Participation part = new Participation();
						part.setParticipant(sharedUser); //Den zuvor aus ListBox gewählten User setzen
						part.setReference(contactToDisplay);							
							contactSystemAdmin.createParticipation(part, new CreateParticipationCallback());					
						}else{
							Window.alert("Kein Kontakt ausgewählt.");
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
						//ctvm.updateContact(result);
					} else {
						Window.alert("Kein Kontakt gefunden :(");
					}
				}
			}
			
			
			
		  
}