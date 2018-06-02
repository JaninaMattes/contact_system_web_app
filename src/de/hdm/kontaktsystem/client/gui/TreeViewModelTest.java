package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;

public class TreeViewModelTest implements TreeViewModel {
	
	private ContactSystemAdministrationAsync csa = ClientsideSettings.getContactAdministration();

	/**
	 * Hier wird die Baumstruktur getestet.
	 */
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		//((ContactList) value).getContacts()
		
		//log("NodeInfo: "+ value);
		ListDataProvider<BusinessObject> dataProvider = new ListDataProvider<BusinessObject>();
		
		csa.getAllContactLists(new AsyncCallback<Vector<ContactList>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				log("Keine Listen gefunden");
			}

			@Override
			public void onSuccess(Vector<ContactList> result) {
				// TODO Auto-generated method stub
				log("Es wurden "+ result.size() +" Listen gefunden");
				for (ContactList cl : result) {
					//dataProvider.getList().add(cl);
				}
			}
			
		});
		
		
		//Anzahl der Elemente
		for (int i = 0; i < 2; i++) {
			ContactList cl = new ContactList();
			Vector<Contact> cv = new Vector<Contact>();
			for(int j = 0; j < 5; j++) {
				Contact c = new Contact();
				c.setBo_Id(j);
				cv.add(c);
			}
			
			cl.setContacts(cv);
			cl.setName("Liste " + i);
			cl.setBo_Id(i);
			
			dataProvider.getList().add(cl);
			log(cl.getName()); 
		}
		return new DefaultNodeInfo<BusinessObject>(dataProvider, new DataCell());
	}

	@Override
	public boolean isLeaf(Object value) {
		log(value.toString());
		// Anzahl der Ebenen
		return (value instanceof Contact ); //value.toString().length() > 8;
	}

	

	native void log(String s)/*-{
		console.log(s);
	}-*/;
	
	
}


class DataCell extends AbstractCell<BusinessObject>{

	@Override
	public void render(Context context, BusinessObject value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<p>" + value.getBoId() + "</p>" );
		
		
	}
}