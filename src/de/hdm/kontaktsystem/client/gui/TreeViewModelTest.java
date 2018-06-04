package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;

public class TreeViewModelTest implements TreeViewModel {

	private ContactSystemAdministrationAsync csa = ClientsideSettings.getContactAdministration();
	private ListDataProvider<BusinessObject> dataProvider = null;
	private BusinessObject selectedContactContactlist;
	private ContactListForm clForm;
	private ContactForm cForm;
	private BoKeyProvider boKey = null;
	private SingleSelectionModel<BusinessObject> selectionModel;
	
	/**
	 * Gibt eindeutigen Schluessel für das BusinessObject zurück.
	 * @author Marco
	 *
	 */
	
	private class BoKeyProvider implements ProvidesKey<BusinessObject> {

		@Override
		public Integer getKey(BusinessObject item) {
			if (item != null) {
				return item.getBoId();
			}
			return null;
		}
	}

	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			
			setSelectedContactContactlist(selectionModel.getSelectedObject());
			log("event" + event.toDebugString());
			
			
			
		}
		
	}
	
	public TreeViewModelTest() {
		BoKeyProvider keyProvider = new BoKeyProvider();
		selectionModel = new SingleSelectionModel<BusinessObject>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
	}
	
	
	
	
	
	public BusinessObject getSelectedContactContactlist() {
		return selectedContactContactlist;
	}

	
	/**
	 * Im Setter wird abgefragt, ob das BusinessObject ein Kontakt oder eine Kontaktliste ist.
	 * Diese wird dann entsprechend der Form gesetzt.
	 * @param sccl
	 */
	
	public void setSelectedContactContactlist(BusinessObject sccl) {
		this.selectedContactContactlist = sccl;
		if(sccl instanceof ContactList) {
			clForm.setSelected((ContactList)sccl);
		}
		else if (sccl instanceof Contact) {
			cForm.setSelected((Contact) sccl);
		}
	}





	public ContactListForm getClForm() {
		return clForm;
	}





	public void setClForm(ContactListForm clForm) {
		this.clForm = clForm;
	}





	public ContactForm getCForm() {
		return cForm;
	}





	public void setCForm(ContactForm cForm) {
		this.cForm = cForm;
	}





	/**
	 * Hier wird die Baumstruktur getestet.
	 */

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {

		log("NodeInfo: " + value);
		dataProvider = new ListDataProvider<BusinessObject>();
		
		if (value instanceof ContactList) {
			for (Contact c : ((ContactList) value).getContacts()) {
				dataProvider.getList().add(c);
			}
		} else {
			csa.getAllContactLists(new AsyncCallback<Vector<ContactList>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					log("Keine Listen gefunden");
				}

				@Override
				public void onSuccess(Vector<ContactList> result) {
					// TODO Auto-generated method stub
					log("Es wurden " + result.size() + " Listen gefunden");
					for (ContactList cl : result) {
						dataProvider.getList().add(cl);

					}
				}

			});
		}

		/*
		 * //Anzahl der Elemente for (int i = 0; i < 2; i++) { ContactList cl = new
		 * ContactList(); Vector<Contact> cv = new Vector<Contact>(); for(int j = 0; j <
		 * 5; j++) { Contact c = new Contact(); c.setBo_Id(j); cv.add(c); }
		 * 
		 * cl.setContacts(cv); cl.setName("Liste " + i); cl.setBo_Id(i);
		 * 
		 * dataProvider.getList().add(cl); log(cl.getName()); }
		 */
		return new DefaultNodeInfo<BusinessObject>(dataProvider, new DataCell(), selectionModel, null);
	}

	@Override
	public boolean isLeaf(Object value) {
		// log(value.toString());
		// Anzahl der Ebenen
		return (value instanceof Contact); // value.toString().length() > 8;
	}

	native void log(String s)/*-{
								console.log(s);
								}-*/;

}

class DataCell extends AbstractCell<BusinessObject> {

	@Override
	public void render(Context context, BusinessObject value, SafeHtmlBuilder sb) {
		if (value != null) {
		if (value instanceof ContactList) {
			sb.appendHtmlConstant("<p>" + ((ContactList) value).getName() + "</p>");
		} else if (value instanceof Contact) {
			sb.appendHtmlConstant("<p>" + ((Contact) value).getName().getValue() + "</p>");
		} else {
			sb.appendHtmlConstant("<p>" + value.getBoId() + "</p>");
		}
		}
	}
}