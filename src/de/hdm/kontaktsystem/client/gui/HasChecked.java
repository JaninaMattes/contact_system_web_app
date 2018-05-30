package de.hdm.kontaktsystem.client.gui;

/**
 * HasChecked Interface für die Umsetzung der Multiple Combobox
 * 
 */

public interface HasChecked {

	  // genutzt für interne Speicherung
	/*
	 * setzen der ID
	 */
	  void setId(String id);
	  /*
	   * abrufen der ID
	   */
	  String getId();

	  // boolean for checkbox
	  void setChecked(boolean checked);

	  // boolean for checkbox
	  boolean getChecked();

	  // label
	  String getName();

	  // label
	  void setName(String name);
	}