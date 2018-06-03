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
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

/**
 * Formular für die Darstellung und Anzeige eines Kontaktes
 * @author janina
 */

public class ContactForm extends VerticalPanel{
	
	ContactSystemAdministrationAsync contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
	Contact contactToDisplay = null;
	ContactListTreeViewModel ctvm = null;
	ContactListsTreeViewModel cltvm = null; /*Kann von beiden TreeViewModeln aufgerufen werden*/
	
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
		
		/*id für CSS*/
		deleteButton.getElement().setId("Delete-Button");
		deleteButton.addClickHandler(new DeleteClickHandler());
		
		saveButton.getElement().setId("Save-Button");
		saveButton.addClickHandler(new SaveClickHandler());
		
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
	     * Clickhandler zum Überprüfen ob TextBox abgeklickt wurde
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
	    	  if(contactNickName.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	    	  //else pv = getValue()
	      }
	    });
	    	  
	    checkBox3.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactFirma = textBoxFirma.getText();
	    	  if(contactFirma.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });

	    checkBox4.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactPhone = textBoxTelefonnummer.getText();
	    	  if(contactPhone.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox5.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactMobile = textBoxMobilnummer.getText();
	    	  if(contactMobile.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox6.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactEmail = textBoxEmail.getText();
	    	  if(contactEmail.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	 
	    checkBox7.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactGebuDatum = textBoxGeburtsdatum.getText();
	    	  if(contactGebuDatum.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox8.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactAdresse = textBoxAdresse.getText();
	    	  if(contactAdresse.isEmpty()) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    		  
	    	  }
	      }
	    });
	       
	     
		/**
		 * Die TextBox um den Kontakt einzugeben, das Label und das Panel für die Buttons werden 
		 * über das Grid Widget untereinander auf dem VerticalPanel angeordnet.
		 * 
		 */		
	    
	    Grid content = new Grid(4, 1);
	    
		vp.add(label);
		vp.add(contactGrid);
		vp.add(shareGrid);
		vp.add(btnPanel);
				
	}		
		
	  
	
	   
		/**
		 * Wenn der anzuzeigende Kontakt gesetzt bzw. gelöscht wird, werden die
		 * zugehörenden Textfelder mit den Informationen aus dem Kontaktobjekt
		 * gefüllt bzw. gelöscht.
		*/
		
		public void setSelected(Contact c) {
			
			if (c != null) {
				contactToDisplay = c;	
				
				if(c.isShared_status()) {
					contactStatus.setText("Status: Geteilt");
				} else { contactStatus.setText("Status: Nicht geteilt");}
				
				label.setText("Kontakt: " + c.getBoId());
				
				//Enable Checkboxen
				if(!textBoxName.getText().isEmpty()) checkBox1.setEnabled(true);		
				if(!textBoxNickName.getText().isEmpty()) checkBox2.setEnabled(true);
				if(!textBoxFirma.getText().isEmpty()) checkBox3.setEnabled(true);
				if(!textBoxTelefonnummer.getText().isEmpty()) checkBox4.setEnabled(true);
				if(!textBoxMobilnummer.getText().isEmpty()) checkBox5.setEnabled(true);
				if(!textBoxEmail.getText().isEmpty()) checkBox6.setEnabled(true);					
				if(!textBoxGeburtsdatum.getText().isEmpty()) checkBox7.setEnabled(true);
				if(!textBoxAdresse.getText().isEmpty()) checkBox8.setEnabled(true);
				
				//Status überprüfen, ob PropertyValue bereits geteilt wurde
				this.setUpCheckBoxValues();
								
				Vector <PropertyValue> pv = new Vector <PropertyValue>();
				pv = contactToDisplay.getPropertyValues();
				
				for(PropertyValue p : pv) {	
					
				//Setzen der PropertyValue Werte aus Kontakt-Objekt
				if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
				if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
				if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
				if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
				if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
				if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
				if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
				if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());	
				
				}
				
				//Befüllen der Listbox mit Usern aus dem System
//     			Vector <User> u = contactSystemAdmin.getAllUsers(new AsyncCallback Vector<User> {
//     						public void onFailure(Throwable error) {
//     							Window.alert("Login Error :(");
//     						}
//     						public void onSuccess(Vector<User>) {
//     							for (User user: u) {
//     								shareUser.addItem(user.getUserContact().getName().getValue());
//     								}
//     						}
//     			}
	
			} else {
				//Löschen eines Kontaktes
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
		 
		 /*Alle CheckBoxen anhacken, wenn diese mit der ausgewählten Person bereits geteilt wurden*/
		 private void setUpCheckBoxValues() {
			 
			 //User u = this.getSharedTo();
			 
			 Vector<PropertyValue> result = new Vector<PropertyValue>();
			 for(PropertyValue p : result) {
				 
			 
				 /*Überprüfen des Status und der Gleichheit der Werte*/
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
				 if(p.getValue().equals(textBoxName.getText()) 
						 && p.isShared_status()) checkBox1.setValue(true);
			 }
		 }
		 
			/*
			 *  ctvm setter
			 */
			
			void setCtvm(ContactListTreeViewModel ctvm) {
				this.ctvm = ctvm;
			}
			
			/*
			 * cltvm setter
			 */
			void setCltvm(ContactListsTreeViewModel cltvm) {
				this.cltvm = cltvm;
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
			
			
			public boolean isOwnedByMe(Contact c) {				
			    User owner = new User();
//			   // owner = userInfo
//			   contactSystemAdmin.getAllParticipationsByOwner(owner, new AsyncCallback (owner));
			   return false;
			}
			
			/**
			 * Zum Löschen eines Kontaktes wird zunächst der Eigentümer abgefragt, bevor im
			 * Callback eine Löschung durchgeführt wird.
			 * 
			 */
			private class DeleteClickHandler implements ClickHandler {
				
				Vector <PropertyValue> p = new Vector<PropertyValue>();

				@Override
				public void onClick(ClickEvent event) {
					if (contactToDisplay == null) {
						Window.alert("Kein Kontakt ausgewählt");
					}
					if (contactToDisplay!= null && getAllCheckedValues()!= null){
						p = getAllCheckedValues();
						/*Löschen aller über CheckBoxen ausgewählten Werte*/
						for(PropertyValue pv : p) {
//						contactSystemAdmin.deletePropertyValue(pv, new AsyncCallback(pv));
						Window.alert("Die ausgewählten Werte wurden gelöscht");
						//Refresh-> setSelected(Contact c)?
						}
					}
					else {
						/*Überprüfen ob Owner*/
						if(isOwnedByMe(contactToDisplay)) {
							Participation p = new Participation();
//							contactSystemAdmin.deleteContact(contactToDisplay, new AsyncCallback(contactToDisplay));
//							contactSystemAdmin.deleteParticipation(p, new AsyncCallback(p));
						}
					}
				}
			}
			
			
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
//						contactSystemAdmin.editContact(contactToDisplay, new AsyncCallback(contactToDisplay));						
				}
				
			}
			}
		  
}