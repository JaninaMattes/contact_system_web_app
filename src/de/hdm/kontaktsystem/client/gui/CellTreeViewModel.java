package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.mysql.jdbc.log.Log;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;

public class CellTreeViewModel implements TreeViewModel {

	private ContactSystemAdministrationAsync csa = ClientsideSettings.getContactAdministration();
	private ListDataProvider<BusinessObject> dataProvider = null;
	private ListDataProvider<BusinessObject> rootData = null;
	private BusinessObject selectedContactContactlist;
	private ContactListForm clForm;
	private ContactForm cForm;
	private BoKeyProvider boKey = null;
	private SingleSelectionModel<BusinessObject> selectionModel;
	private Vector<BusinessObject> serverData = null;
	
	/**
	 * Gibt eindeutigen Schluessel f�r das BusinessObject zur�ck.
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
	
	public CellTreeViewModel() {
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
		RootPanel.get("Details").clear();
		if(sccl instanceof ContactList) {
			clForm.setSelected((ContactList)sccl);
			log("Update clForm");
			RootPanel.get("Details").add(clForm);
		}
		else if (sccl instanceof Contact) {
			cForm.setSelected((Contact) sccl);
			log("Update cForm");
			RootPanel.get("Details").add(cForm);
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

	
	public void updateData(Vector<BusinessObject> bov){
		log("Update Data");
		rootData.getList().clear();
		dataProvider = null;
		for (BusinessObject bo : bov) {
			rootData.getList().add(bo);

		}
		//getNodeInfo(vbo);
	}
	
	public void updateBusinessObject(BusinessObject bo){
		log("Update Data");
		if(bo instanceof Contact) {
			dataProvider.getList().remove(bo);
			dataProvider.getList().add(bo);
		} else if (bo instanceof ContactList){
			rootData.getList().remove(bo);
			rootData.getList().add(bo);
		}
		
	}
	
	/**
	 * AddBusinessObjekt Methode f�r den AddButton im CellTree
	 * Die Methode muss erst pr�fen, ob das BusinessObject ein Kontakt,
	 * oder eine Kontaktliste ist.
	 * @param bo
	 */
	
	public void addBusinessObject(BusinessObject bo){
		if(bo instanceof Contact) {
		dataProvider.getList().add(bo);
		} else if (bo instanceof ContactList){
		rootData.getList().add(bo);
		}
	}
	
	/**
	 * RemoveBusinessObjekt Methode f�r den RemoveButton im CellTree
	 * Die Methode muss erst pr�fen, ob das BusinessObject ein Kontakt,
	 * oder eine Kontaktliste ist.
	 * @param bo
	 */
	
	public void removeBusinessObject(BusinessObject bo){
		if(bo instanceof Contact) {
		dataProvider.getList().remove(bo);
		} else if (bo instanceof ContactList){
		rootData.getList().remove(bo);
		}
		
	}
	


	/**
	 * Hier wird die Baumstruktur getestet.
	 */

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {

		log("NodeInfo: " + value.getClass());
		dataProvider = new ListDataProvider<BusinessObject>();

		if (value instanceof Vector) {
			log("NodeInfo: is Vector" );
			rootData = dataProvider; // Speichert RootListe damit die UpdateData methode die Daten Überschreiben kann;
			Vector v = (Vector) value;
			Vector<BusinessObject> bov = v;	
			
				for (BusinessObject bo : bov) {
					
					dataProvider.getList().add(bo);
				}
		}else 
		if(value instanceof ContactList){
			for (Contact c : ((ContactList) value).getContacts()) {
				dataProvider.getList().add(c);
			}
		}
		

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
			
			sb.appendHtmlConstant("<p class='Cell'>" + "<img src='/images/group.png' height='22px' width='22px' >  " + ((ContactList) value).getName() + "</p>");

		} else if (value instanceof Contact) {
			
			sb.appendHtmlConstant("<p class='Cell'>" + "<img src='/images/person.png' height='22px' width='22px'>  " + ((Contact) value).getName().getValue() + "</p>");

		} else {

			sb.appendHtmlConstant("<p class='Cell'>" + value.getBoId() + "</p>");

		}
		}
	}
}