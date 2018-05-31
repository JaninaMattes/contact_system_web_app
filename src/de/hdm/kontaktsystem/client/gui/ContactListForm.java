package de.hdm.kontaktsystem.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.User;
//import com.smartgwt.client.types.Alignment;  
//import com.smartgwt.client.types.DragDataAction;  
//import com.smartgwt.client.widgets.Canvas;  
//import com.smartgwt.client.widgets.TransferImgButton;  
//import com.smartgwt.client.widgets.events.ClickEvent;  
//import com.smartgwt.client.widgets.events.ClickHandler;  
//import com.smartgwt.client.widgets.layout.HStack;  
//import com.smartgwt.client.widgets.layout.VStack;  
//import com.smartgwt.sample.showcase.client.data.PartData;  


//Anzeige und Bearbeiten einer Kontaktliste

public class ContactListForm extends VerticalPanel{
	ContactSystemAdministrationAsync contactSystemAdmin = ClientsideSettings.getContactAdministration();
	ContactList contactListToDisplay = null;
	ContactListsTreeViewModel cltvm = null;
	
	/**
	 * Widgets mit variablen Inhalten.
	 */
	
	TextBox nameContactList = new TextBox();
	TextBox firstNameContact = new TextBox();
	TextBox lastNameContact = new TextBox();
	//TextBoox sharedContact = new TextBox();
	
	/**
	 * WIdgets um die Attributte einer Kontaktliste anzuzeigen.
	 */
	
	Label contactListLabel = new Label("Kontaktliste: ");
	Label firstNameLabel = new Label("Vorname: ");
	Label lastNameLabel = new Label("Nachname: ");
	//Label sharedContactLabel = new Label("X");
	
	/**
	 * Loeschenbutton um eine Kontaktliste zu loeschen.
	 */

	Button addButton = new Button ("Kontakt der Liste hinzuf�gen");
	Button shareButton = new Button ("Kontaktliste teilen");
	Button deleteButton = new Button ("Kontakt aus eine Liste l�schen");
	Button deleteClButton = new Button ("Kontaktliste l�schen");
	

	public void onLoad() {
		super.onLoad();
		// Keine Tabelle sondern ein VertialPanel / ScrollPanel dem Kontaktelemente aus dem ContactVector hinzugef�gt werden.
		// ... Mit einer while oder for-each Schleife die Kontakt-Elemente erzeugen.
		Grid contactListGrid = new Grid(8, 2);
		this.add(contactListGrid);
		
		contactListGrid.setWidget(0, 0, contactListLabel);
		contactListGrid.setWidget(0, 1, nameContactList);
		
		contactListGrid.setWidget(1, 0, firstNameLabel);
		contactListGrid.setWidget(1, 1, firstNameContact);
		
		contactListGrid.setWidget(2, 0, lastNameLabel);
		contactListGrid.setWidget(2, 1, lastNameContact);
		
		//contactListGrid.setWidget(3, 0, sharedContactLabel);  //widget um anzuzeigen, welche Kontakte einer Liste geteilt wurden
		//contactListGrid.setWidget(3, 1, sharedContact);
		
		addButton.addClickHandler(new addClickHandler());
		addButton.setEnabled(false);
		contactListGrid.setWidget(4, 1, addButton);
		
		shareButton.addClickHandler(new shareClickHandler());
		shareButton.setEnabled(false);
		contactListGrid.setWidget(5, 1, shareButton);
		
		//deleteButton kommt in das Kontakt-Element rein. Es l�scht einzelne Kontakte aus der Liste.
		
		deleteButton.addClickHandler(new DeleteClickHandler());
		deleteButton.setEnabled(false);
		contactListGrid.setWidget(6, 1, deleteButton);
		
		deleteClButton.addClickHandler(new DeleteClClickHandler());
		deleteClButton.setEnabled(false);
		contactListGrid.setWidget(7, 1, deleteClButton);
		
	    /** Buttons in CSS
	     * 
	     * Der Name, mit welchem der Delete-Button in CSS formatiert werden kann, wird festgelegt. 
	     */ 

	    deleteButton.addStyleName("DeleteContactfromCL"); 
	    
	    /** 
	     * Der Name, mit welchem der Teilen-Button in CSS formatiert werden kann, wird festgelegt. 
	     */ 

	    shareButton.addStyleName("shareCL"); 
	    
	    /**
	     * Der Name, mit welchem der hinzufügen-Button in CSS formatiert werden kann, wird festgelegt. 
	     */ 

	    addButton.addStyleName("addContacttoCL"); 
	    
	    /** 
	     * Der Name, mit welchem der löschen-Button in CSS formatiert werden kann, wird festgelegt. 
	     */ 

	    deleteClButton.addStyleName("deleteCL"); 
	    
	    
		/*
		 * Drag Liste TODO: Smart GWT Lizenz + Test 
		 * Eventuelle Lösung, da ComboBox nicht möglich war
		 * Quelle: https://www.smartclient.com/smartgwt/showcase/#effects_dd_move_list
		 */
//		
//		HStack hStack = new HStack(10);  
//        hStack.setHeight(160);  
//  
//        final PartsListGrid myList1 = new PartsListGrid();  
//        myList1.setCanDragRecordsOut(true);  
//        myList1.setCanAcceptDroppedRecords(true);  
//        myList1.setCanReorderFields(true);  
//        myList1.setDragDataAction(DragDataAction.MOVE);  
//        myList1.setData(PartData.getRecords());  
//        hStack.addMember(myList1);  
//  
//        final PartsListGrid myList2 = new PartsListGrid();  
//        myList2.setCanDragRecordsOut(true);  
//        myList2.setCanAcceptDroppedRecords(true);  
//        myList2.setCanReorderRecords(true);  
//  
//        VStack vStack = new VStack(10);  
//        vStack.setWidth(32);  
//        vStack.setHeight(74);  
//        vStack.setLayoutAlign(Alignment.CENTER);  
//  
//        TransferImgButton rightImg = new TransferImgButton(TransferImgButton.RIGHT);  
//        rightImg.addClickHandler(new ClickHandler() {  
//            public void onClick(ClickEvent event) {  
//                myList2.transferSelectedData(myList1);  
//            }  
//        });  
//        vStack.addMember(rightImg);  
//  
//        TransferImgButton leftImg = new TransferImgButton(TransferImgButton.LEFT);  
//        leftImg.addClickHandler(new ClickHandler() {  
//            public void onClick(ClickEvent event) {  
//                myList1.transferSelectedData(myList2);  
//            }  
//        });  
//        vStack.addMember(leftImg);  
//  
//        hStack.addMember(vStack);  
//        hStack.addMember(myList2);  
//  
//        hStack.draw();  
//    }  
//  
//}  
	}


	class addClickHandler implements ClickHandler {
	public void onClick(ClickEvent event) {
		if (contactListToDisplay == null) {
			Window.alert("Keine Kontaktliste ausgew�hlt");
			} else {
				
			}
	}
}

	class shareClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgew�hlt");
			} else {
				User u = new User();
				u.setGMail("NeuerUser@gmail.com"); // Testdaten, sollte beim Teilen �ber ein Popup abgefragt werden.
				Participation part = new Participation();
				part.setParticipant(u); 
				part.setReference(contactListToDisplay);
				// TODO:
				//contactSystemAdmin.shareContactListWith(part, new shareContactListWithCallback(part));
				//ALTERNATIV: contactSystemAdmin.createParticipation(contactListToDisplay, u , new AsyncCallback<Participation>() { 
				//In andere NestedKlasse ausgliedern, wie bei "delete Clickhandler"
			}
		}
	}
	
	class shareContactListWithCallback implements AsyncCallback<Participation> {

		Participation participation = null;
	
		shareContactListWithCallback(Participation part) {
			participation = part;
		}
	
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			// User nicht gefunden
		}
	
		//@Override
		public void onSuccess(Participation result) {
			// Eventuell ein Update der Liste
			
			Window.alert("KontaktListe wurde erfolgreich geteilt.");
		}
				
	}
		

/**
 * ClickHandler zum Loeschen eines Kontaktes in einer Kontaktliste
 */

class DeleteClickHandler implements ClickHandler {
	
	public void onClick(ClickEvent event) {
		/** Nach Erstellung der kompletten GUI, nochmals �berpr�fen, wie man den Kontakt �bergeben kann um ihn mit einem X zu loeschen.
		 if (contact == null) {
			Window.alert("Keinen Kontakt ausgew�hlt");
		}else {
			contactSystemAdmin.removeContactFromList(contact, contactListToDisplay, 
					new deleteContactfromListCallback());
		}*/
	}
}
// NOCHMAL PR�FEN
class deleteContactfromListCallback implements AsyncCallback<ContactList> {
	
	
	public void onFailure(Throwable caught) {
		Window.alert("Löschen eines Kontaktes fehlgeschlagen");
	}
	
	public void onSuccess(ContactList result) {
		if (result != null) {
			setSelected(null);
			cltvm.removeContactList(result);
			Window.alert("Kontakt wurde erfolgreich aus der Liste gel�scht");
		}
	}
}

/**
 * Clickhandler zum Loeschen einer Kontaktliste.
 * @author Marco
 *
 */

class DeleteClClickHandler implements ClickHandler {

	@Override
	public void onClick(ClickEvent event) {
		if (contactListToDisplay == null) {
			Window.alert("keine Kontaktliste ausgew�hlt");
		} else {
			contactSystemAdmin.deleteContactList(contactListToDisplay,
					new deleteContactListCallback());
		}
	}


class deleteContactListCallback implements AsyncCallback<ContactList> {

	@Override
	public void onFailure(Throwable caught) {
		Window.alert("L�schen einer Kontaktliste fehlgeschlagen");
	}

	@Override
	public void onSuccess(ContactList result) {
		if (result != null) {
			setSelected(null);
			cltvm.removeContactList(result);
		}
	}
}
}
	/**
	 *  cstvm setter
	 */

	void setCltvm(ContactListsTreeViewModel cltvm) {
	this.cltvm = cltvm;
	}

	void setSelected(ContactList cl) {
		
		if (cl != null) {
			
			contactListToDisplay = cl;
			deleteButton.setEnabled(true);
			nameContactList.setText(cl.getName());
			

			
		} else {
			nameContactList.setText("");
			firstNameContact.setText("");
			lastNameContact.setText("");
			//sharedContact.setText("");

			deleteButton.setEnabled(false);
		}
		
	}
		public void setSelected(Object object) {
			// TODO Auto-generated method stub
			

		}
	    



}