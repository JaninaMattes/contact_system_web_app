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

public class ContactListsTreeViewModel implements TreeViewModel{

	private ContactListForm contactListForm;
	private ContactList selectedContactList;
	
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
	
	public ContactListsTreeViewModel() {
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
	void updateContact(ContactList clist) {
		contactSystemAdmin.getContactById(clist.getBoId(), new UpdateContactCallback(clist));
	
	}

	private class UpdateContactCallback implements AsyncCallback<Contact> {

		ContactList cl = null;

		@SuppressWarnings("unused")
		UpdateContactCallback (ContactList list) {
			this.cl = list;
		}

		@Override
		public void onFailure(Throwable t) {
			Window.alert("Update nicht durchgeführt.");
		}

		@Override
		public void onSuccess(Contact c) {
			List<ContactList> contactList = contactListDataProviders.get(c)
					.getList();
			for (int i=0; i<contactList.size(); i++) {
				if (cl.getBoId() == contactList.get(i).getBoId()) {
					contactList.set(i, cl);
					break;
				}
			}
		}
	}

}
