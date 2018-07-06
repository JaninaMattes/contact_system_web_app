package de.hdm.kontaktsystem.client.gui;

import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.TreeNode;
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

/**
 * Anzeige der <code>BusinessObject</code>-Vectoren in einem TreeView.
 * Ersellt einen Treeview mit zwei Ebenen,
 * 1. Ebene: KontaktListen, Kontakte,
 * 2. Ebene: Kontakte die sich in den Listen befinden.
 * Die Daten die in dem <code>CellTreeViewModel</code> angezigt werden,
 * werden dabei in Dataprovidern abgelegt, welche es ermöglichen,
 * die Daten im TreeView zu einem späternen Zeitpunkt hinzuzufügen, zu verändern
 * oder zu entfernen.
 * Es besteht die erweiterungs Möglichkeit, in einer dritten Ebene die 
 * Eigenchaftsausprägungen anzuzeigen.
 *   
 * @author Oliver Gorges, Marco Pracher
 *
 */
public class CellTreeViewModel implements TreeViewModel {

	private ContactSystemAdministrationAsync csa = ClientsideSettings.getContactAdministration();
	private ContactListForm clForm;
	private ContactForm cForm;
	private BusinessObject selectedContactContactlist;
	private BoKeyProvider boKey = null;
	private SingleSelectionModel<BusinessObject> selectionModel;
	private Vector<BusinessObject> serverData = null;
	private ListDataProvider<BusinessObject> dataProvider = null;
	// Ablage der Dataprovider in einer Map mit der BO_ID als Key, um zu einem späteren Zeitpunkt auf die Daten zugeriefen zu können.
	private HashMap<Integer, ListDataProvider<BusinessObject>> dpMap = new HashMap<Integer, ListDataProvider<BusinessObject>>();
	
	/**
	 * Gibt eindeutigen Schluessel für das BusinessObject zurück.
	 * @author Marco Pracher
	 */
	private class BoKeyProvider implements ProvidesKey<BusinessObject> {
		/**
		 * Gibt einen eindeutigen schlüssel zu einem BusinessObjekt zurück.
		 * @param BusinessObject
		 * @return BusinessObject-ID
		 */
		@Override
		public Integer getKey(BusinessObject item) {
			if (item != null) {
				return item.getBoId();
			}
			return null;
		}
	}

	/**
	 * Händler der beschreibt, was passieren soll, wenn im TreeViewModel ein Element angeklick wird.
	 * => Es wird das ausgewählte Objekt in dem dafür vorgesehen Formular angezeigt.
	 * @author Oliver Gorges
	 */
	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {
		
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			
			setSelectedContactContactlist(selectionModel.getSelectedObject());
		}
		
	}
	
	/**
	 * Initialisiert das <code>CellTreeViewModel</code>.
	 * Instanziert ein SelectionModel mithilfe des BOKeyProviders
	 * und fügt diesem den ChangeHandler hinzu.
	 * 
	 */
	public CellTreeViewModel() {
		boKey = new BoKeyProvider();
		selectionModel = new SingleSelectionModel<BusinessObject>(boKey);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		
	}
	
	/**
	 * Gibt das aktuell in dem TreeView ausgewählten BusinessObjekt zurück
	 * @return BusinessObject
	 */
	public BusinessObject getSelectedContactContactlist() {
		return selectedContactContactlist;
	}

	
	/**
	 * Im Setter wird abgefragt, ob das BusinessObject ein Kontakt oder eine Kontaktliste ist.
	 * Diese wird dann in dem dafür entsprechenden Form angezeigt.
	 * @param BusinessObject
	 */
	
	public void setSelectedContactContactlist(BusinessObject sccl) {
		this.selectedContactContactlist = sccl;
		log("Selected: "+ sccl);
		if(sccl != null){
			RootPanel.get("Details").clear();
			if(sccl instanceof ContactList) {
				// Befüllt ein ContactListForm mit dem ausgewählten BusinessObject
				clForm.setSelected((ContactList) sccl);
				RootPanel.get("Details").add(clForm);
				log("Update clForm");
			}
			else if (sccl instanceof Contact) {
				// Befüllt ein ContactForm mit dem ausgewählten BusinessObject
				cForm.setSelected((Contact) sccl);
				RootPanel.get("Details").add(cForm);
				log("Update cForm");
			}
		}
	}

	/*
	 *  Setzt die Auswahl in dem <code>SelectionModel</code> zurück.
	 *  Die Detailansicht wird geleert und im TreeViewModel wird kein
	 *  Objekt als ausgewählt angezeigt. 
	 */
	public void restSelection(){
		log("Reset");
		RootPanel.get("Details").clear();
		selectionModel.clear();
	}


	/**
	 * Gibt das ContactList-Objekt zurück, das in dem TreeViewModel verwendet wird
	 * @return ContactListForm
	 */
	public ContactListForm getClForm() {
		return clForm;
	}

	/**
	 * Übergibt dem TreeViewModel das <code>ContactListForm</code>-Objekt welches zur Anzeige von 
	 * <code>ContactList</code>-Objekten benötigt wird.
	 * @param ContactListForm
	 */
	public void setClForm(ContactListForm clForm) {
		this.clForm = clForm;
	}

	/**
	 * Gibt das ContactList-Objekt zurück, das in dem TreeViewModel verwendet wird
	 * @return ContactListForm
	 */
	public ContactForm getCForm() {
		return cForm;
	}

	/**
	 * Übergibt dem TreeViewModel das <code>ContactForm</code>-Objekt welches zur Anzeige von 
	 * <code>Contact</code>-Objekten benötigt wird.
	 * @param ContactListForm
	 */
	public void setCForm(ContactForm cForm) {
		this.cForm = cForm;
	}

	/**
	 * Update aller Dataprovier.
	 * z.B. wenn die Ansicht des TreeViewModels verändert wird (Navigations Buttons)
	 * @param Vector<BusinessObject>
	 */
	public void updateData(Vector<BusinessObject> bov){
		log("Update Data");
		dpMap.get(0).getList().clear();
		dataProvider = null;
		for (BusinessObject bo : bov) {
			dpMap.get(0).getList().add(bo);
		}
		dpMap.get(0).refresh();
	}
	
	/**
	 * Updatet ein einzelnes <code>BusinessObject</code> in allen Dataprovidern des TreeViewModels.
	 * @param BusinessObjekt
	 */
	public void updateBO(BusinessObject bo){
		log("Update Leef");		
		for(Integer i : dpMap.keySet()){
			if(dpMap.get(i).getList().contains(bo)){
				int index = 0;
				for(BusinessObject listElement : dpMap.get(0).getList()){
					if(listElement.equals(bo)) break;
					index++;
				}
				dpMap.get(i).getList().set(index, bo);
				dpMap.get(i).refresh();
			}
		}
			
	}
	
	/**
	 * Fügt ein <code>BusinessObject</code> dem RootDataprovider hinzu.
	 * @param BusinessObject
	 */
	
	public void addToRoot(BusinessObject bo){
		
		log("Add to root "+bo);
		dpMap.get(0).getList().add(bo);
		dpMap.get(0).refresh();
	}
	
	/**
	 * Fügt ein <code>BusinessObject</code> dem einem ausgewählten RootDataprovider hinzu.
	 * Der Dataprovider wird dabei über den Key identifiziert.
	 * Key = <code>BusinessObject<code>-ID des übergeordnete Elements
	 * @param BusinessObject
	 */
	public void addToLeef(int key, BusinessObject bo){
		
		log("Add to Leef");
		dpMap.get(key).getList().add(bo);
		dpMap.get(key).refresh();
	}
	
	/**
	 * RemoveBO Methode für den RemoveButton im CellTree
	 * Das ausgewählte <code>BusinessObject</code> wird aus allen DataProvidern entfernt.
	 * @param BusinessObject
	 */
	
	public void removeBO(BusinessObject bo){
		log("Delete from Tree");
		for(Integer i : dpMap.keySet()){
			dpMap.get(i).getList().remove(bo);
			dpMap.get(i).refresh();
		}
		
	}
	
	/**
	 * RemoveBO Methode für den RemoveButton im CellTree
	 * Das ausgewählte <code>BusinessObject</code> wird nur 
	 * aus dem, über den Key, bestimmten DataProvidern entfernt.
	 * Key = <code>BusinessObject<code>-ID des übergeordnete Elements
	 * 
	 * @param BusinessObject
	 */
	public void removeFromLeef(int key, BusinessObject bo){
		
		log("Delete from Leef");
		dpMap.get(key).getList().remove(bo);
		dpMap.get(key).refresh();	
	}
	


	/**
	 * Befüllt mit den Daten aus dem Übergeben <code>BusinessObject</code> Vector
	 * einen DataProvider und befüllt diesen in den Tree.
	 * Wenn nur ein BusinessObject übergeben wurde, wird der Inhalt des Objekts
	 * verwendet, um den DataProvider zu befüllen.
	 * @param Vector<BusinessObject> oder BusinessObject
	 * @return NodeInfo<?>
	 */

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		log("NodeInfo: " + value.getClass());
		dataProvider = new ListDataProvider<BusinessObject>();

		if (value instanceof Vector) {
			log("NodeInfo: is Vector" );
			dpMap.put(0, dataProvider); // Speichert RootListe damit die UpdateData methode die Daten Überschreiben kann;
			Vector v = (Vector) value;
			Vector<BusinessObject> bov = v;	
			
				for (BusinessObject bo : bov) {
					
					dataProvider.getList().add(bo);
				}
		}else 
		if(value instanceof ContactList){
			log("Open List: "+ ((ContactList) value).getName());
			dpMap.put(((ContactList) value).getBoId(), dataProvider);
			this.csa.getContactsFromList(((ContactList) value), new AsyncCallback<Vector<Contact>>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					log("Kontaktsuche Fehlerhaft");
				}

				@Override
				public void onSuccess(Vector<Contact> result) {
					
					for(Contact c : result){
						dataProvider.getList().add(c);
					}
				}
				
			});
		}
		

		return new DefaultNodeInfo<BusinessObject>(dataProvider, new DataCell(), selectionModel, null);
	}

	/**
	 * Aktuell werden in diesem TreeViewModel <code>Contact</code>-Objekte aus letzes Element im Baum verwendet.
	 *
	 * @param Object (BusinessObject)
	 * @return boolean
	 */
	@Override
	public boolean isLeaf(Object value) {
		return (value instanceof Contact); 
	}

	/**
	 * JavaScript: Verwendung vom JavaScript Element "console.log()" um Ausgaben in 
	 * der Browserkonsole anziegen zu lassen.
	 * 
	 * @param String
	 */
	native void log(String s)/*-{
								console.log(s);
								}-*/;


}
/**
 * Die Klasse wandelt die <code>BusinessObject</code>-Objekte in HTML-Elemente um, um diese
 * im TreeView anzuzeigen.
 * Bei <code>Contact</code> und <code>ContactList</code> Objekten wird der Name zur anzeige verwendet.
 * 
 * @author Marco Pracher
 */
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