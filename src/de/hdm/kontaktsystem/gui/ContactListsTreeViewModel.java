package de.hdm.kontaktsystem.gui;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.kontaktsystem.shared.bo.ContactList;

public class ContactListsTreeViewModel implements TreeViewModel {

	private ContactListForm contactListForm;
	private ContactList selectedContactList;
	
	private ContactSystemAdministrationAsync contactSystemVerwaltung = null;
	private ListDataProvider<ContactList> contactListDataProvider = null;
	
	/**
	 * ContactList als eindeutige Zielobjekte, welche als Schluessel für Baumknoten
	 * dienen. 
	 * Dadurch werden im Selektmodell alle Objekte mit der gleichen ID ausgewählt,
	 * wenn eines davon selectiert wird.
	 * ... 
	 */
	
	private class ContactListKeyProvider implements ProvidesKey<ContactList> {
		
	public Integer getKey(ContactList cl) {
		if (cl == null) {
			return null;
		}
		return new Integer(Integer.parseInt(String.valueOf(cl.getContacts()+ " " + cl.getReference()))); //NACHBESSERN
	}
}
	
	private ContactListKeyProvider clKeyProvider = null;
	private SingleSelectionModel<ContactList> selectionModel = null;
	
	/**
	 * Nested Class für...
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
		contactSystemVerwaltung = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
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
			contactSystemVerwaltung.getAllContactLists(new AsyncCallback<Vector<ContactList>>() {
				public void onFailure(Throwable t) {
			}
		
			public void onSuccess(Vector<ContactList> contactLists) {
				for (ContactList cl : contactLists) {
					contactListDataProvider.getList().app(cl);  //NACHPRÜFEN
				}
			}
		
		});
		return new DefaultNodeInfo<ContactList>(contactListDataProvider, new ContactListCell(), selectionModel, null);

	}
}
	@Override
	public boolean isLeaf(Object value) {
		// value is type of ContactList
		return (value instanceof ContactList);
	}

}
