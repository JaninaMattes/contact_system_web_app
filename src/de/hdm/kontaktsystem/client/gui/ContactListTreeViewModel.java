package de.hdm.kontaktsystem.client.gui;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;

public class ContactListTreeViewModel implements TreeViewModel{

	private ContactListForm contactListForm;
	private ContactForm contactForm;
	private ContactList selectedContactList;
	private Contact conFromCL;
	
	private ContactSystemAdministrationAsync contactSystemAdmin = null;
	private ListDataProvider<ContactList> contactListDataProvider = null;
	/*
	 * In dieser Map werden die ListDataProviders für die Kontaktlisten
	 * der im Kontaktlisten- und Kontatktbaum expandierten Kontaktknoten gespeichert.
	 */
	private Map<Contact, ListDataProvider<ContactList>> contactListDataProviders = null;
	
	
	/**
	 * ContactList als eindeutige Zielobjekte, welche als Schluessel f�r Baumknoten
	 * dienen. 
	 * Dadurch werden im Selektmodell alle Objekte mit der gleichen ID ausgew�hlt,
	 * wenn eines davon selectiert wird.
	 * ... 
	 */
	
	private class ContactListKeyProvider implements ProvidesKey<ContactList> {
		
	public Integer getKey(ContactList cl) {
		if (cl == null) {
			return null;
		}
		return new Integer(Integer.parseInt(String.valueOf(cl.getBoId()+ "" + cl.getName()))); //TODO: Noch mit Sandra besprechen.NACHBESSERN
	}
}
	
	private ContactListKeyProvider clKeyProvider = null;
	private SingleSelectionModel<ContactList> selectionModel = null;
	
	/**
	 * Nested Class
	 * W�hlt die aktuell fokusierte Kontaktlist
	 */
	
	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {
		
		public void onSelectionChange(SelectionChangeEvent event) {
			ContactList selection = selectionModel.getSelectedObject();
			setSelectedContactList((ContactList) selection);
		}
		
	}
	
	/**
	 * In diesem Kontruktor wird fuer die Kontaktliste die
	 * lokalen Variablen initialisiert.
	 */
	
	public ContactListTreeViewModel() {
		contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
		clKeyProvider = new ContactListKeyProvider();
		selectionModel = new SingleSelectionModel<ContactList>(clKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
	}
	
	void setContactListForm(ContactListForm clf) {
		this.contactListForm = clf;
	}
	
	ContactList getSelectedContactList() {
		return selectedContactList;
	}
	
	
	void setSelectedContactList(ContactList cl) {
		selectedContactList = cl;
		contactListForm.setSelected(cl);
	}
	
	Contact getSelectedContactFromCl() {
		Vector<Contact> conVec = new Vector<Contact>();
		Contact conFromCl = new Contact();
		conVec = selectedContactList.getContacts() ;
		
		for (Contact c : conVec) {
			conFromCl = c;
		}	
		return conFromCl;
	}
	
	
	void removeContactList(ContactList cl) {
		contactListDataProvider.getList().remove(cl);
	}
	
	
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if(value.equals("Root")) {
			
			//Erzeugt eine ListDataProvider fuer ContactList Daten
			contactListDataProvider = new ListDataProvider<ContactList>();
			contactSystemAdmin.getAllContactLists(new AsyncCallback<Vector<ContactList>>() {
				public void onFailure(Throwable t) {
				}
		
			public void onSuccess(Vector<ContactList> contactLists) {
				for (ContactList cl : contactLists) {
					Window.alert(contactLists.toString());
					contactListDataProvider.getList().add(cl);
				}
			}
		
		});
		return new DefaultNodeInfo<ContactList>(contactListDataProvider, new ContactListCell(), selectionModel, null);

	}

		return null;

}
	@Override
	public boolean isLeaf(Object value) {
		// value is type of ContactList
		return (value instanceof ContactList);
	}
	
	/*
	 * Ein Kontakt in der Baumstruktur soll ersetzt werden durch einen Kontakt mit derselben id.
	 * Dies ist sinnvoll, wenn sich die Eigenschaften eines Kontakts z.B. durch <em>editieren</em> 
	 * geändert haben und in der Baumstruktur noch ein "veraltetes" Kontakt - Objekt enthalten ist.
	 */
	public void updateContactList(ContactList clist) {
		List<ContactList> contactList = contactListDataProvider.getList();
		int i = 0;
		for (ContactList con : contactList) {
			if (con.getBoId() == clist.getBoId()) {
				contactList.set(i, clist);
				break;
			} else {
				i++;
			}
		}
		contactListDataProvider.refresh();
	}

	public void addContact(Contact c) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Kontakte aus DefaultNodeInfo<ContactList> extrahieren und in Form als Kontakt anzeigen
	 */
	


}
