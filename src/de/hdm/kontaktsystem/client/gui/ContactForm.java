package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;


/**
 * <p>
 * Die Klasse <code>ContactForm</code> enthält Elemente für die Darstellung
 * eines Kontaktformulars des jeweilig selektierten Kontaktes aus dem Kontaktsystem. 
 * </p>
 * <p>
 * Darüber hinaus ermöglicht dies auch durch Austausch einzelner Elemente, wie bspw.
 * Buttons die Darstellung eines leeren Kontaktformulars zur Ausformulierung der Kontakteigenschaften,
 * vor der Erzeugung eines neuen <code>Contact</code> Objektes in der DB. 
 * </p>
 * <pre>
 * Analog zur Klasse ContactForm wurde auch die Klasse ContactListForm {@link ContactListForm} 
 * für das Anzeigen und die Darstellung der Kontaktlisten angelegt.
 * </pre>
 * 
 * @author Janina Mattes
 */


public class ContactForm extends VerticalPanel {
	
		//Referenzattribute
		User myUser = null;
		User editPartUser = null;
		
		// Lade overlay
		PopupPanel loadPanel;
		
		
		Contact contactToDisplay = null;
		CellTreeViewModel tvm = null;
		
		Grid contactGrid = new Grid(12, 3);
		
		//Proxy-Objekt erzeugen
		ContactSystemAdministrationAsync contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();		
		
		/**
		 * Widgets, deren Inhalte variable sind, werden als Attribute angelegt.
		 */	
		Vector <TextBox> tbv = new Vector <TextBox>();
		Vector<CheckBox> cbv = new Vector<CheckBox>();
		FlexTable ft = new FlexTable();
		
		final VerticalPanel vp  = new VerticalPanel();
				
		HorizontalPanel newPanel = new HorizontalPanel(); // Buttons zum anlegen eines Kontakts
		HorizontalPanel sharePanel = new HorizontalPanel();// Buttons zum Teilen eines Kontakts
		HorizontalPanel editPanel = new HorizontalPanel();// Buttons zum bearbeiten eines Kontakts
		HorizontalPanel btnPanel = new HorizontalPanel(); // Standart Buttons
		HorizontalPanel gpPanel = new HorizontalPanel();
		Grid gp = new Grid(4,3);
		
		Label cLabel = new Label("Kontakt:");
		
		Label labelShare = new Label("Kontakt teilen mit: ");
		Label labelSharedWith = new Label("Kontakt Teilhaber: ");
		Label labelReceivedFrom = new Label("Kontakt Ersteller: ");
		Label labelAddElement = new Label("Ein weiteres Feld hinzufügen:");
		Label contactStatus = new Label("");
				
		Button deleteButton = new Button("Löschen");
		Button deletePartButton = new Button("Löschen");
		Button saveButton = new Button("Speichern");
		Button shareButton = new Button("Teilen");
		Button addButton = new Button("Hinzufügen");
		Button editButton = new Button("Bearbeiten");
		Button editPartButton = new Button("Teilhaberschaft bearbeiten");
		//Button cancelButton = new Button("Abbrechen"); // Teilen abbrechen
		Button okButton = new Button("Teilen");
		Button cancelNewButton = new Button("Abbrechen");
		Button cancelEditButton = new Button("Abbrechen");
		Button cancelShareButton = new Button("Abbrechen");
		Button createButton = new Button("Speichern");
		
		ListBox addElement = new ListBox();
		ListBox sharedWithUser = new ListBox();

		TextBox email = new TextBox();
		TextBox newProperty = new TextBox();
								
		boolean edit = false;
		boolean editPart = false;
		
		/** 
		 * Der Startpunkt der Klasse <code>ContactForm</code>ist die <em>onLoad()</em> Methode.
		 * Beim Anzeigen des Kontaktformulars werden die weiteren Widgets erzeugt.  
		 * 
		 */
		
		public void onLoad(){
//			vp.clear();
//			
//			super.onLoad();
//			this.add(vp);
			log("Kontakt Formular Laden");
			
		}
		
		/**
		 * Die Inhalte eines von der Datenbank abgerufenen Kontaktes, welcher über das Kontaktformular
		 * zur Anzeige gebracht werdden soll, werden dynamisch mittels der Erzeugung von Feldern 
		 * innerhalb eines <em>Flextables</em> erzeugt. 
		 */
		
		public ContactForm() {
			
			
			final User user = new User();
			
			//CSS
			addButton.setStyleName("sideButton");
			editPartButton.setStyleName("sideButton");
			sharedWithUser.setStyleName("ListBox");
			gp.getElement().setId("grid-panel");
			contactStatus.setStyleName("Label");
			labelAddElement.getElement().setId("labelfeldhinzu");
			addElement.setStyleName("ListBox");
			cLabel.getElement().setId("ueberschriftlabel");
			
			// Main Buttonpanels
			okButton.setStyleName("mainButton");
			saveButton.setStyleName("mainButton");
			editButton.setStyleName("mainButton");
			shareButton.setStyleName("mainButton");
			createButton.setStyleName("mainButton");
			deleteButton.setStyleName("mainButton");
			cancelNewButton.setStyleName("mainButton");
			deletePartButton.setStyleName("mainButton");
			cancelEditButton.setStyleName("mainButton");
			cancelShareButton.setStyleName("mainButton");
			
			btnPanel.setStyleName("mainButtonPanel");
			newPanel.setStyleName("mainButtonPanel");
			editPanel.setStyleName("mainButtonPanel");
			sharePanel.setStyleName("mainButtonPanel");
			
			
			//Teilhaberschaften
			labelSharedWith.setVisible(false);
			labelReceivedFrom.setVisible(false);			
			sharedWithUser.setVisible(false);	
			
			
			// Überschrift des DetailPanel;
			cLabel.setText("Kontakt");	
			
			/*
		     * GridPanel für Abbildung der Teilhaber
		     */								
			
			gp.setWidget(0, 0, labelAddElement);
			gp.setWidget(0, 1, addElement);
			gp.setWidget(0, 2, addButton);
			
			gp.setWidget(1, 0, labelSharedWith);
			gp.setWidget(1, 1, sharedWithUser);	
			gp.setWidget(1, 2, editPartButton);
			
			gp.setWidget(2, 0, labelReceivedFrom);
			
			gp.setWidget(3, 0, labelShare);
			gp.setWidget(3, 1, email);
			
			gpPanel.add(gp);
			
			email.getElement().setPropertyString("placeholder", "Email");
			
			
			
			/*
			 * Standard ButtonPanel 
			 */
			
			btnPanel.add(editButton);
			btnPanel.add(shareButton);
			btnPanel.add(deleteButton);
					
			/*
			 * ButtonPanelbeimanlegen eines neuen Kontakts
			 */
			newPanel.add(createButton);
			newPanel.add(cancelNewButton);
			
			
			/*
			 * ButtonPanel beim beabriten eines Kontakts
			 */
			editPanel.add(saveButton);
			editPanel.add(cancelEditButton);
			editPanel.add(deletePartButton);
			
			/*
			 * ButtonPanel beim beabriten eines Kontakts
			 */
			sharePanel.add(okButton);
			sharePanel.add(cancelShareButton);
			
			/*
			 * Zuordnung zum VP			
			 */
			vp.add(cLabel);
			vp.add(contactStatus);
			vp.add(ft);
			vp.add(gpPanel);
			vp.add(btnPanel);
			
			
			this.add(vp);
			
			/**
			 * Bei Auswahl des Buttons <em>Bearbeiten</em> wird das Panel des <code>ContactForm</code>
			 * erweitert und kann nun mit zusätzlichen Funktionalitäten, wie dem hinzufügen eines 
			 * weiteren Elementes oder einer weiteren Teilhaberschaft mit einem Nutzer bearbeitet werden.
			 */
			
			// Bearbeiten Button
			editButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// Ändern der Standart Buttons
					edit = true;
					editGui();
				}
				
			});
			
		
			/**
			 * Bei Auswahl des Buttons <em>Teilen</em> erfolgt die Anzeige der
			 * GUI Elemente, welche dem Nutzer weitere Auswahlmöglichkeiten bietet
			 * einen Kontakt mit einem bestimmten Nutzer im System zu teilen. 
			 */
			
			//Click-Handler
			shareButton.addClickHandler(new ClickHandler(){
				
				@Override
				public void onClick(ClickEvent event) {
					shareGui();
					
				}
				
			});
			
			/**
			 * 
			 */
			deletePartButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					log("Teilhaberschaft bearbeiten");
					
					Participation part = new Participation();
					part.setShareAll(true);
					part.setReference(contactToDisplay);
					part.setParticipant(editPartUser);
					log("Delete Part: "+part);
					contactSystemAdmin.deleteParticipation(part, new AsyncCallback<Participation>(){

						@Override
						public void onFailure(Throwable caught) {
							loadPanel.setVisible(false);
							ContactSystem.triggerNotify("Teilhaberschaft kann nicht gelöscht werden");
						}

						@Override
						public void onSuccess(Participation result) {
							loadPanel.setVisible(false);
							sharedWithUser.removeItem(sharedWithUser.getSelectedIndex());
							ContactSystem.triggerNotify("Teilhaberschaft wurde gelöscht");
							
						}
						
					});
					
					// Speichern der Bearbeitetet Teilhaberschaften
					for(CheckBox cb: cbv){
						cb.setValue(false);
					}
					defaultGui();
					editPart = false;
				}
			});
			
			/**
			 * Bei Auswahl des Buttons <em>Teilen</em> erfolgt der Aufruf der
			 * Service-Methode "createParticipation". Diese erhält als Übergabeparameter
			 * ein <code>Participation</code> Objekt, welches den zuvor ausgewählten Nutzer, 
			 * als auch das zu teilende, referenzierte <code>Contact</code> Objekt enthält. 
			 * Durch die neu angelegte Teilhaberschaft können alle verändernden Aktivitäten 
			 * (create, read, update, delete) an alle Teilhaber weitergegeben werden. 
			 */
			
			okButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					loadPanel.setVisible(true);
					contactSystemAdmin.getUserBygMail(email.getText(), new AsyncCallback<User>(){
						@Override
						public void onFailure(Throwable caught) {
							loadPanel.setVisible(false);
							Window.alert("User wurde nicht gefunden");
						}
	
						@Override
						public void onSuccess(User result) {
							Participation part = new Participation();
							Contact c = contactToDisplay;
							boolean somethingChecked = false;
							// Überprüft ob eine Checkbox ausgewählt wurde, wenn nicht dann wird derKontaktebenso vollständig geteilt
							for(CheckBox cb : cbv){
								log("CB "+cb.getTitle() +" = "+cb.getValue());
								if(cb.getValue()){
									somethingChecked = true; 
									break;
								}
							}
							log("Something Checked? " + somethingChecked);
							
							
							if(!cbv.get(0).getValue() && somethingChecked){ 
								part.setShareAll(false);
								log("Kontakt nicht vollständig Geteilt");
								// Wenn nicht der Vollständige Kontakt geteilt wird, muss überprüft werden, was geteilt werden soll
								Vector<PropertyValue> pvv = new Vector<PropertyValue>();
								for(CheckBox cb: cbv){
									// Eine Ausprägung wird geteilt wenn die Checkbox True ist. Der Name (cbv[1]) wird immer geteilt
									if(cb.getValue() || cb.equals(cbv.get(1))){ 
										for(PropertyValue pv : c.getPropertyValues()){
											if(pv.getBoId() == Integer.parseInt(cb.getTitle())){
												pvv.add(pv);
												log("Share: " + pv.getValue() + " With " + email.getText());
											}
										}
									}
								}
								c.setPropertyValues(pvv);
							}else{
								// Wenn der Kontakt vollständig geteilt werden soll werden keine PropertyValues geteilt
								c.setPropertyValues(null);
								part.setShareAll(true);
							}
							part.setParticipant(result);
							part.setReference(c);
							log("### Share:" + part);
							
							
							contactSystemAdmin.createParticipation(part, new CreateParticipationCallback());
							
						}
					});
					// Zurücksetzten der GUI;
					defaultGui();
				}
			});
			
			
			/**
			 * Bei Auswahl des Buttons <em>Löschen</em> erfolgt der Aufruf der
			 * Service-Methode "deleteContact". Dies führt bei Besitztümerschaft
			 * eines Nutzers zu der Löschung eines <code>Contact</code> Objektes aus 
			 * der DB. Ist der Nutzer jedoch nur Teilhaber an dem ausgewählten Kontakt
			 * so wird <em>nur</em> die damit verbundene Teilhaberschaft aus der DB gelöscht. 
			 */
			
			deleteButton.addClickHandler(new ClickHandler(){
				Vector <PropertyValue> result = new Vector <PropertyValue>();
				@Override
				public void onClick(ClickEvent event) {
					if(!contactToDisplay.equals(myUser.getUserContact())){		
						if(Window.confirm("Bitte bestätigen Sie dass der Kontakt gelöscht werden soll.")) {	
							loadPanel.setVisible(true);
							contactSystemAdmin.deleteContact(contactToDisplay, new AsyncCallback<Contact>() {
		
								@Override
								public void onFailure(Throwable caught) {
									loadPanel.setVisible(false);
									ContactSystem.triggerNotify("Der Kontakt konnte nicht gelöscht werden.");
								}
		
								@Override
								public void onSuccess(Contact result) {
									ContactSystem.triggerNotify("Kontakt gelöscht");
									loadPanel.setVisible(false);
									contactToDisplay= null;
									tvm.removeBO(result);
									RootPanel.get("Details").clear();
								}						
							});
						}	
					}else{
						loadPanel.setVisible(false);
						Window.alert("Sie können Ihren eigenen Kontakt nicht löschen");
					}
				} 				
			});
			
			/**
			 * Bei Auswahl des Buttons <em>Speichern</em> erfolgt der Aufruf der
			 * Service-Methode "editContact". Diese übergibt die neuen Werte aus den
			 * Textfeldern der <code>ContactForm</code> an die DB und speichert so 
			 * Änderungen am dargestellten Kontakt. 
			 * 
			 */
			
			saveButton.addClickHandler(new ClickHandler() {
				
				
				@Override
				public void onClick(ClickEvent event) {
				
					loadPanel.setVisible(true);
					if(editPart){
						log("Teilhaberschaft bearbeiten");
						Contact c = new Contact();
						c.setBo_Id(contactToDisplay.getBoId());
						c.setOwner(contactToDisplay.getOwner());
						
						Participation part = new Participation();
						
						if(!cbv.get(0).getValue()){
							// Es werden nur die ausgewählten Eigenschaftsausrägungen geteilt
							Vector<PropertyValue> pvv = new Vector<PropertyValue>();
							for(CheckBox cb: cbv){
								// Eine Ausprägung wird geteilt wenn die Checkbox True ist. Der Name (cbv[1]) wird immer geteilt
								if(cb.getValue() || cb.equals(cbv.get(1))){ 
									for(PropertyValue pv : contactToDisplay.getPropertyValues()){
										if(pv.getBoId() == Integer.parseInt(cb.getTitle())){
											pvv.add(pv);
											log("Edit Share: " + pv.getValue() + " With " + editPartUser.getGMail());
											break;
										}
									}
								}
							}
							c.setPropertyValues(pvv);
						}else{
							log("Share All");
							c.setPropertyValues(contactToDisplay.getPropertyValues());	
						}
						
						
						
						part.setReference(c);
						part.setShareAll(cbv.get(0).getValue());
						part.setParticipant(editPartUser);
						log("New Part: "+part);
						contactSystemAdmin.editParticpation(part, new AsyncCallback<Participation>(){

							@Override
							public void onFailure(Throwable caught) {
								loadPanel.setVisible(false);
								ContactSystem.triggerNotify("Teilhaberschaft kann nicht bearbeitet werden");
							}

							@Override
							public void onSuccess(Participation result) {
								loadPanel.setVisible(false);
								ContactSystem.triggerNotify("Teilhaberschaft wurde bearbeitet");
								
							}
							
						});
						
						// Speichern der Bearbeitetet Teilhaberschaften
						for(CheckBox cb: cbv){
							cb.setValue(false);
						}
						editPart = false;
					}else{
						// Speichern des bearbeiteten Kontakts
						Vector<PropertyValue>editResult = new Vector<PropertyValue>();
						if(contactToDisplay!=null) {
							
							// Erstes TextFeld (Name) darf nicht leer sein
							if(tbv.elementAt(0).getText().isEmpty()){ 
								loadPanel.setVisible(false);
								Window.alert("Das Feld Name darf nicht leer sein.");
								return;							
							}
							
							log("Der Kontakt besteht bereits. "+tbv.size());
							for(TextBox tb: tbv) {							 
								 log("Titel: "+tb.getTitle());
								 // für neue Eigenschaftsausrägungen wird eine die Property anhand des Titels "Neu:PropertyID" erstellt
								 if(tb.getTitle().contains("Neu")) {
									 	String[]s=tb.getTitle().split(":");
										Property p = new Property();//1.1.1.1 Erzeuge neuesObjekt		
										p.setId(Integer.parseInt(s[1]));
										// Wenn die ID 0 ist dann muss die Beschreibung aus dem Textfeld "newProperty" verwendet werden.
										if(p.getId() == 0){
											if(newProperty.getText() == ""){
												log("Text ist Leer");
												continue; // überpringt die neue Eigenschaft, da keine Beschreibung angegeben wurde
											}
											log("Neue Eigenschaft anlegen");
											p.setDescription(newProperty.getText());
										}else{
											p.setDescription(s[0]);
										}
										PropertyValue ppv = new PropertyValue();
										ppv.setContact(contactToDisplay);
										ppv.setOwner(contactToDisplay.getOwner());
										ppv.setValue(tb.getText());
										ppv.setProperty(p);
										editResult.add(ppv);
										log("Neuen Pv: "+ppv);
								 	}else{
										log("Update");
										for(PropertyValue pv : contactToDisplay.getPropertyValues()) { //editieren + hinzufügen
											if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
												pv.setValue(tb.getText());
												editResult.add(pv);
												log("Editieren des Pv: "+pv);
											}
										}									
								 }
	
								tb.setReadOnly(true);	// Textboxen dürfen nach dem Speichern nicht weiter bearbeitet werden.			
							}
							contactToDisplay.setPropertyValues(editResult);
							log("Save: "+contactToDisplay);
							contactSystemAdmin.editContact(contactToDisplay, new SaveCallback());
						}
						
					}
					defaultGui();
				}
			});
			
			/**
			 * Bei Auswahl des Buttons <em>Speichern</em> nach dem Anlegen neuer Kontakt
			 * Felder/Elemente erfolgt der Aufruf der Service-Methode "createContact".
			 * Diese übernimmt als Parameter ein <code>Contact</code> Objekt, welches alle
			 * neu erzeugten <code>PropertyValue</code> Objekte als <em>Vector</em> enthält
			 * und generiert einen neuen Eintrag in der DB für den Kontakt.  
			 */
			createButton.addClickHandler(new ClickHandler() {
				

				@Override
				public void onClick(ClickEvent event) {
					Vector<PropertyValue>createResult = new Vector<PropertyValue>();
					loadPanel.setVisible(true);
					for(TextBox tb: tbv) {
						 if(tb.getTitle().equals("Neu:1") & tb.getText().isEmpty()) { //1.0 verhindern dass Kontakt Name leer
							 	loadPanel.setVisible(false);
								Window.alert("Das Feld 'Name's darf nicht leer sein.");
								return;
						 }
						 if(!tb.getText().isEmpty()) {
							 	Property p = new Property();//1.1.1.1 Erzeuge neuesObjekt		
							 	String[]s=tb.getTitle().split(":");
								p.setId(Integer.parseInt(s[1]));
								p.setDescription(s[0]);
								
								PropertyValue ppv = new PropertyValue();
								ppv.setContact(contactToDisplay);
								ppv.setOwner(contactToDisplay.getOwner());
								ppv.setProperty(p);
								ppv.setValue(tb.getText());
								createResult.add(ppv);
								log("Erzeugen eines neuen Pv: "+ppv);
						 }
					}
					
					contactToDisplay.setPropertyValues(createResult);
					contactSystemAdmin.createContact(contactToDisplay, new SaveCallback());					
				}
			});
			
			/**
			 * Bei Auswahl des Buttons <em>Abbrechen</em> erfolgt der Abbruch der Anzeige.
			 * 
			 */
			cancelNewButton.addClickHandler(new CancelClickHandler());
			cancelEditButton.addClickHandler(new CancelClickHandler());
			cancelShareButton.addClickHandler(new CancelClickHandler());
			
			/**
			 * Bei Auswahl des Buttons <em>Hinzufügen</em> erfolgt die Zuordnung eines
			 * neuen TextBox Elements mit einer dazugehörigen ListBox zum <em>Flextable</em>.
			 * Nach der Speicherung der Änderungen wird der neue Eintrag dem <code>Contact</code>
			 * Objekt hinzugefügt.
			 * 
			 */
			
			addButton.addClickHandler(new ClickHandler() {
				
				@Override 
				public void onClick(ClickEvent event) {
					
					Property p = new Property();					
					
					p.setId(Integer.parseInt(addElement.getSelectedValue()));
					
					
					TextBox tb = new TextBox();
					CheckBox cb = new CheckBox();	
					tb.setStyleName("TextBox");					
					tb.setTitle("Neu:"+p.getId());
					cb.setTitle("Neu:"+p.getId());
					cb.setVisible(false);
					tbv.add(tb);
					cbv.add(cb);
					
					int row = ft.getRowCount()+1;

					// Überprüft ob die Eigenschaft bereits existiert oder ob der User eine neue Eigenschaft angelegen möchte
					if(p.getId() == 0){
						newProperty.getElement().setPropertyString("placeholder", "Neue Eigenschaft");
						ft.setWidget(row, 0, newProperty);
						// Entfernt die Möglichkeit eine neue eigenschaft an zu legen, da nur eine Eigenschaft auf einmal neu angelegt werden kann
						addElement.removeItem(addElement.getItemCount()-1); 
					}else{
						// Zeigt das Beschreibung der Eigenschaft an
						Label label = new Label(addElement.getSelectedItemText());
						ft.setWidget(row, 0, label);
					}
					
					ft.setWidget(row, 1, tb);
					ft.setWidget(row, 2, cb);
				}
				
			});
			
			/**
			 * Bei Auswahl des Buttons <em>Bearbeiten</em> erfolgt der Aufruf der
			 * Service-Methode "getUserBygMail" und "getAllPVFromContactSharedWithUser".
			 * Dies ermöglicht das Darstellen der einzelnen, geteilten Elemente eines
			 * Kontaktes mit einem anderen Nutzer. Die Darstellung des Kontaktes wird
			 * als Folge aktualisiert. 
			 */
			
			editPartButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					loadPanel.setVisible(true);
					for(CheckBox cb: cbv) {
						cb.setValue(false);
					}
					if(sharedWithUser.getSelectedIndex()>=0) {
						String mail= sharedWithUser.getSelectedItemText();
						log("Mail gefunden: "+mail);
						contactSystemAdmin.getUserBygMail(mail, new AsyncCallback<User>() {
	
							@Override
							public void onFailure(Throwable caught) {
								loadPanel.setVisible(false);
								log("Nutzer Abruf fehlgeschlagen "+caught);
							}
	
							@Override
							public void onSuccess(final User userResult) {
								log("User abgerufen: "+userResult);
								editPartUser = userResult;
								
								contactSystemAdmin.getAllPVFromContactSharedWithUser(contactToDisplay, userResult, new AsyncCallback<Vector<PropertyValue>>(){
							
									@Override
									public void onFailure(Throwable caught) {
										loadPanel.setVisible(false);
										log("Teilhaberschaft Abruf fehlgeschlagen "+caught);									
									}
	
									@Override
									public void onSuccess(Vector<PropertyValue> result) {
										// Ändern der Gui
										editShareGui();
										editPart = true;
										loadPanel.setVisible(false);
										if(result.size() > 0){
											for(PropertyValue pv: result) {
													for(CheckBox cb: cbv) {
															if(Integer.parseInt(cb.getTitle())==pv.getBoId()){	
																cb.setValue(true); //Anzeigen triggern
															}												
													}
											}
										}else{
											log("All Shared");
											for(CheckBox cb: cbv) {
												cb.setValue(true);
											}
										}
									}
									
								});			
							}
							
						});
					}else if(sharedWithUser.getSelectedIndex()==-1){
						loadPanel.setVisible(false);
						Window.alert("Bitte wählen Sie zuerst einen Kontakt Teilhaber "
								+ "vor der Bearbeitung dessen Teilhaberschaft aus.");
					}
				}
					
			});


		}
		
		/**
		 * Wenn der anzuzeigende Kontakt gesetzt, bzw. gelöscht wird, werden die 
		 * zugehörigen Textfelder, ListBoxen und weiteren Elemente welche Informationen aus
		 * dem Kontakt Objekt enthalten befüllt, oder analog dazu gelöscht. 
		 * 
		 * Wird ein leeres Kontakt Objekt übergeben an die Methode, so wird zudem ein
		 * leeres Kontaktformular zur Erzeugung neuer Kontakte dargestellt. 
		 * @param contact
		 */
		
		public void setSelected(Contact contact) {
			// leeren der Vektoren, ListBoxen und Textfelder		
			addElement.clear();
			sharedWithUser.clear();
			tbv.clear();
			cbv.clear();
			ft.clear();
			labelReceivedFrom.setText("");
			newProperty.setText("");
			email.setText("");
			
			log("MyUser: "+ myUser);
			
			if(contact!=null) {
				this.contactToDisplay = contact;
				// Anzeigen der Standard Anzeige GUI Elemente
				this.defaultGui();
				contactSystemAdmin.getContactById(contact.getBoId(), new AsyncCallback<Contact>() {
				
					@Override
					public void onFailure(Throwable caught) {
						log("Kontakt Callback fehlgeschlagen");
					}

					@Override
					public void onSuccess(Contact result) {
						contactToDisplay= result;	
						
						
						int row = 0;
						Label label = new Label();
						CheckBox cb = new CheckBox();
						TextBox tb = new TextBox();
						
						/**
						 *  Gibt eine Fehlermeldung aus, wenn der Nutzer keine berechtigung besitzt, diesen Kontakt anzuzeigen
						 *  Dieser fall tritt nur auf, wenn mit einem Nutzer eine Liste geteilt wurde und dieser ein Kontakt,
						 *  der sich in der Liste befindet aus seinen eigenen Kontakten löscht.
						 *  Dadurch ird der Kontakt noch in der Listen ansicht angezeigt, ist aber nicht mehr mit dem Nutzer geteilt 
						 *  und kann dadurch nicht angezeigt werden.
						 */
						
						if(result.getPropertyValues().isEmpty()){
							ft.setWidget(0, 0, new Label("Sie haben leider keine Berechtigung, sich diesen Kontakt anzeigen zu lassen.")); 
							ft.setWidget(1, 0, new Label("Bitte wenden Sie sich an den Besitzer des Kontakts "));
							ft.setWidget(2, 0, new Label("("+result.getOwner().getUserContact().getName().getValue() + " / " +result.getOwner().getGMail()+"),"));
							ft.setWidget(3, 0, new Label("um den Kontakt erneut geteilt zu bekommen."));
						}else{
							cb.setText("Alle");
							cb.setTitle(0+"");
							cb.setVisible(false);
							cb.addClickHandler(new ClickHandler(){
								@Override
								public void onClick(ClickEvent event) {
									for(CheckBox cb : cbv) cb.setValue(cbv.get(0).getValue());
								}
							});
							cbv.add(cb);
							ft.setWidget(row, 2, cb);
							row ++;
							
							//Flextable befüllen
							for(PropertyValue pv : result.getPropertyValues()){
								
								label = new Label();
								cb = new CheckBox();
								tb = new TextBox();
								tb.setStyleName("TextBox");
								label.setStyleName("Label");
								label.setTitle(pv.getProperty().getId()+"");
								label.setText(pv.getProperty().getDescription()+": ");
								cb.setTitle(pv.getBoId()+"");
								tb.setTitle(pv.getBoId()+"");
								tb.setText(pv.getValue());
								
								// Textboxen können nicht bearbeitet werden
								tb.setReadOnly(true);
								
								// Ändert den zustand der ShareAllCheckBox (cbv[0])
								cb.addClickHandler(new ClickHandler(){
									@Override
									public void onClick(ClickEvent event) {
										cbv.get(0).setValue(false);
									}
								});
								cb.setVisible(false); // Checkboxen werden nur beim Teilen und Teilhaberschaft bearbeiten angezeigt
								
								cbv.add(cb);
								tbv.add(tb);
										
								ft.setWidget(row, 0, label);
								ft.setWidget(row, 1, tb);
								ft.setWidget(row, 2, cb);
								
								row++;
							}
						}
						
						if(myUser.getGoogleID()!=result.getOwner().getGoogleID()) {
							log("Kontakt Besitzer anzeigen: " +result.getOwner().getGMail());
							labelReceivedFrom.setVisible(true);
							labelReceivedFrom.setText("Geteilt von: "+result.getOwner().getGMail());
						}else if(myUser.getGoogleID()==result.getOwner().getGoogleID()){	
							log("My User: "+myUser.getGoogleID());
							contactSystemAdmin.getAllParticipationsByBusinessObject(contactToDisplay, new AsyncCallback<Vector<Participation>>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									loadPanel.setVisible(false);
									Window.alert("Teilhaberschaften konnten nicht abgerufen werden.");
								}

								@Override
								public void onSuccess(Vector<Participation> result) {
									loadPanel.setVisible(false);
									if(result.size()>0){

										labelSharedWith.setVisible(true);
										sharedWithUser.setVisible(true);
										editPartButton.setVisible(true);
										
										int i = 0;
										for(Participation part: result) {
											if(part.getParticipant().getGoogleID()==myUser.getGoogleID()) {
												result.remove(part);
											}else {
												sharedWithUser.addItem(part.getParticipant().getGMail());										
												log("Teilhaber: "+part.getParticipant().getGMail());
											}  
											i++;								
										}
									}
								}						
							});					
						}
					}					
				});	
						

				// Zeigt den den SharedStatus an

				if (contactToDisplay.getShared_status()) {
					if(contactToDisplay.getOwner().getGoogleID() == myUser.getGoogleID()){
						contactStatus.setText("Status: Von mir geteilt");
					}else{
						contactStatus.setText("Status: An mich geteilt");
					}	
				} else {
					contactStatus.setText("Status: Nicht geteilt");
				}
							
				//Elemente füllen
				contactSystemAdmin.getAllProperties(new AsyncCallback<Vector<Property>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub	
						log("Properties nicht abgerufen");
					}
	
					@Override
					public void onSuccess(Vector<Property> result) {
						 addElement.clear();
						 for(Property p : result) {
							 if(p.getId()!=1) { 
								 // Fügt der Listbox die Property Beschreibung als text und die ID als Value hinzu
								 // Der Value wird verwendet um die Property wieder zuordnen zu können
								 addElement.addItem(p.getDescription(), p.getId()+"");
							 }
						 }	
						 // Fügt ein listen element hinzu um eine neue Eigenschaft zu erstellen
						 addElement.addItem("Neu..", "0"); // ID 0wird in der Property tabelle nicht verwendert
					}
				});
							
			} else if(contact==null) {
				
				//Anzeigen der GUI Elemente die man für das anlegen eines neuen Kontakts benötigt
				this.newGui();
				
				// Es wir ein leeres Formular angezeigt, um einen neuen Kontakt anzulegen
				contact = new Contact();
				contactToDisplay=contact;
				log("Neuer Kontakt " + contact);
						
				contactStatus.setText("Status: Noch nicht erstellt");	
				
				contactSystemAdmin.getAllProperties(new AsyncCallback<Vector<Property>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						loadPanel.setVisible(false);
						Window.alert("Eigenschaften konnten nicht abgerufen");						
					}

					@Override
					public void onSuccess(Vector<Property> result) {	
						loadPanel.setVisible(false);
						log("Kontakt Properties: " + result);
						
						addElement.clear();
						 for(Property p : result) {
							 if(p.getId()!=1) { 
								 // Fügt der Listbox die Property Beschreibung als text und die ID als Value hinzu
								 // Der Value wird verwendet um die Property wieder zuordnen zu können
								 addElement.addItem(p.getDescription(), p.getId()+"");
							 }
						 }	
						 // Fügt ein listen element hinzu um eine neue Eigenschaft zu erstellen
						 addElement.addItem("Neu..", "0"); // ID 0 wird in der Property Tabelle nicht verwendert
						
						
						// Es werden nur die ersten 8 Standard eigenschaften angezeigt. 
						// diese anzeige kann aber durch den Nutzer erweiter werden
						result.setSize(8); 
						
						int row = 0;
						//Flextable befüllen						
						for(Property p : result){
							Label label = new Label();
							TextBox tb = new TextBox();
							
							label.setTitle("Neu:"+p.getId());
							label.setText(p.getDescription());
							tb.setTitle("Neu:"+p.getId());
							tb.setText("");
							tb.setStyleName("TextBox");
							tbv.add(tb);
										
							ft.setWidget(row, 0, label);
							ft.setWidget(row, 1, tb);
							
							row++;
						}	
					}					
				});	
				
			}
		}

		private class SaveCallback implements AsyncCallback<Contact> {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				loadPanel.setVisible(false);
				Window.alert("Speichern ist fehlgeschlagen.");
			}

			@Override
			public void onSuccess(Contact result) {
				loadPanel.setVisible(false);
				ContactSystem.triggerNotify("Der Kontakt wurde gespeichert");
				if(edit){
					tvm.updateBO(result); 
				}else{
					log("Add New Contact to Root");
					tvm.addToRoot(result);
				}
				tvm.setSelectedContactContactlist(result);
				// Update des TreeViewModels
				
				edit = false;
			}							
		}
		
		/**
		 * Eine neue Teilhaberschaft anlegen.
		 * @author Janina Mattes
		 *
		 */
		private class CreateParticipationCallback implements AsyncCallback<Participation>{

			@Override
			public void onFailure(Throwable caught) {
				loadPanel.setVisible(false);
				ContactSystem.triggerNotify("Kontakt konnte nicht geteilt werden. ");						
			}

			@Override
			public void onSuccess(Participation result) {
				loadPanel.setVisible(false);
				sharedWithUser.addItem(result.getParticipant().getGMail());
				
				defaultGui(); // Zurück zur Standard Gui	
				ContactSystem.triggerNotify("Der Kontakt wurde geteilt.");
			}				
		}
		
		// Cancel Clickhandler
		private class CancelClickHandler implements ClickHandler{

			@Override
			public void onClick(ClickEvent event) {
				
				if(labelShare.isVisible()){ // Abbrechen bei Teilhaberschaft anlegen
					defaultGui();
				}else if(editPart){ // Abbrechen von Teilhaberschaft bearbeiten
					editPart = false;
					defaultGui();
				}else if(edit){ // Abbrechen von Kontakt bearbeiten
					// Wird neu geladen, da ansonsten der Flextable und dieVectoren überprüft / geleert werden müssten.
					tvm.setSelectedContactContactlist(contactToDisplay);
				}else{
					tvm.restSelection();
					contactToDisplay = null;
					RootPanel.get("Details").clear();
				}
			}	
		}
		
		/**
		 * GUI Methoden
		 * Bauen das Contactform je nach funktion um, dadruch soll die Übersichtlichkeit verbessert werden
		 * @author Oliver Gorges
		 */
		
		// Standart ansicht der GUI, Anzeige des Kontakts mit Teilhaber/Besitzer
		private void defaultGui(){
			// Alle Elemente ausblenden
			for(CheckBox cb : cbv){
				cb.setVisible(false);
			}
			labelReceivedFrom.setVisible(false);
			labelSharedWith.setVisible(false);
			sharedWithUser.setVisible(false);
			editPartButton.setVisible(false);
			labelReceivedFrom.setVisible(false);
			labelShare.setVisible(false);
			email.setVisible(false);
			labelAddElement.setVisible(false);
			addElement.setVisible(false);
			addButton.setVisible(false);
			
			// Buttonpanels austauschen falls es vorher noch nicht passiert ist
			vp.remove(editPanel);
			vp.remove(sharePanel);
			vp.remove(newPanel);
			vp.add(btnPanel);
			
			// Teilhaberschaftselemente wieder einblenden
			if(myUser.getGoogleID()!=contactToDisplay.getOwner().getGoogleID()){
				log("Not Owner");
				labelReceivedFrom.setVisible(true);
			}else if(sharedWithUser.getItemCount()>0){
				log("Not Empty");
				labelSharedWith.setVisible(true);
				sharedWithUser.setVisible(true);
				editPartButton.setVisible(true);
			}else{
				log("Nothing");
			}
			
		}
		
		// GUI Elemente zum neu anlegen eines Kontakts
		private void newGui(){
			labelReceivedFrom.setVisible(false);
			labelSharedWith.setVisible(false);
			sharedWithUser.setVisible(false);
			editPartButton.setVisible(false);
			labelReceivedFrom.setVisible(false);
			labelShare.setVisible(false);
			email.setVisible(false);
			
			// Eingeblendete GUI Elemnte
			labelAddElement.setVisible(true);
			addElement.setVisible(true);
			addButton.setVisible(true);
			
			// Buttonpanels austauschen falls es vorher noch nicht passiert ist
			vp.remove(editPanel);
			vp.remove(sharePanel);
			vp.remove(btnPanel);
			vp.add(newPanel);
		}
		
		// GUI Elemente zum Teilen eines Kontakts
		private void shareGui(){
			// Buttonpanels austauschen falls es vorher noch nicht passiert ist
			vp.remove(editPanel);
			vp.remove(btnPanel);
			vp.remove(newPanel);
			vp.add(sharePanel);
			
			labelSharedWith.setVisible(false);
			sharedWithUser.setVisible(false);
			editPartButton.setVisible(false);
			labelReceivedFrom.setVisible(false);
			
			labelShare.setVisible(true);
			email.setVisible(true);
			
			for(CheckBox cb : cbv){
				cb.setVisible(true);
			}
		}
		
		// GUI Elemente zum bearbeiten der Teilhaberschaft
		private void editShareGui(){
			// Buttonpanels austauschen falls es vorher noch nicht passiert ist
			vp.remove(sharePanel);
			vp.remove(btnPanel);
			vp.remove(newPanel);
			vp.add(editPanel);
			
			labelReceivedFrom.setVisible(false);
//			labelSharedWith.setVisible(false);
//			sharedWithUser.setVisible(false);
			editPartButton.setVisible(false);
			labelReceivedFrom.setVisible(false);
			deletePartButton.setVisible(true);
			
			for(CheckBox cb : cbv){
				cb.setVisible(true);
			}
		}
		
		// Gui Elemente zum bearbeiten des Kontakts
		private void editGui(){
			// Buttonpanels austauschen falls es vorher noch nicht passiert ist
			vp.remove(sharePanel);
			vp.remove(btnPanel);
			vp.remove(newPanel);
			vp.add(editPanel);
			
			labelReceivedFrom.setVisible(false);
			labelSharedWith.setVisible(false);
			sharedWithUser.setVisible(false);
			editPartButton.setVisible(false);
			deletePartButton.setVisible(false);
			
			labelAddElement.setVisible(true);
			addElement.setVisible(true);
			addButton.setVisible(true);
			
			// Textfelder können bearbeitet werden
			for(TextBox tb : tbv){
				tb.setReadOnly(false);
			}
			
		}
		
				
		/*
		 * TreeViewModel setter
		 */
		
		void setTree(CellTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		/*
		 * LoadPanel setter
		 */
		void setLoad(PopupPanel load) {
			this.loadPanel = load;
		}
		
		/*
		 * Eigenen User setzen
		 */
		void setMyUser(User user) {
			this.myUser = user;
		}
		
		/*
		 * Logger
		 */
		native void log(String s)/*-{
		console.log(s);
		}-*/;
}