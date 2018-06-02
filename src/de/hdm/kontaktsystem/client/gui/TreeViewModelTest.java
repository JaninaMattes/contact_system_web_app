package de.hdm.kontaktsystem.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class TreeViewModelTest implements TreeViewModel {

	/**
	 * Hier wird die Baumstruktur getestet.
	 */
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		String text = "";
		if(value.toString().length() == 5) {text = value.toString();}
		else { text = "Kontakt";}
		
		log("NodeInfo: "+ value);
		ListDataProvider<String> dataProvider = new ListDataProvider<String>();
		//Anzahl der Elemente
		for (int i = 0; i < 2; i++) {
			
			dataProvider.getList().add(text +":"+ i);
			log(value + "." + i); 
		}
		return new DefaultNodeInfo<String>(dataProvider, new DataCell());
	}

	@Override
	public boolean isLeaf(Object value) {
		log(value.toString());
		// Anzahl der Ebenen
		return value.toString().length() > 8;
	}

	

	native void log(String s)/*-{
		console.log(s);
	}-*/;
	
	
}


class DataCell extends AbstractCell<String>{

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<p>" + value + "</p>" );
		
		
	}
}