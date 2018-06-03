/**
 * 
 */
package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;

/**
 * Spalte eines <code>Row</code>-Objekts. <code>Column</code>-Objekte
 * implementieren das <code>Serializable</code>-Interface und können dadurch als
 * Kopie z.B. vom Server an den Client übertragen werden.
 * 
 * @see Row
 * @see de.hdm.thies.bankProjekt.shared.report.Column
 * @author Sandra
 *
 */
public class Column implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Der Wert eines <code>Column</code>-Objekts entspricht dem Zelleneintrag
	 * einer Tabelle. Hier handelt es sich um einen einfachen textuellen Wert.
	 */
	private String value = "";
	
	/**
	 * Serialisierbare Klassen, die mittels GWT-RPC transportiert werden sollen, müssen 
	 * einen Konstruktor ohne übergebene Parameter besitzen. Besitzt eine Klasse min. einen
	 * explizit angegebenen Konstruktor, so gibt es nur diese explizit implementierten 
	 * Konstruktoren, der Default-Konstruktor (No-Argument-Konstruktor, der automatisch 
	 * existiert, wenn kein Konstruktor explizit angegeben wird) ist dann nicht vorhanden.
	 * Da mit {@link #Column(String)} ein expliziter Konstruktor angegeben wird, muss der
	 * No-Argument-Konstruktor also explizit implementiert werden.
	 * 
	 * @see #Column(String)
	 * @see SimpleParagraph#SimpleParagraph()
	 */
	 public Column() {
	 }
	 
	 /**
	  * Konstruktor, der die Angabe eines Werts (Spalteneintrag) erzwingt.
	  * 
	  * @param s Der Wert, der durch das Column-Objekt dargestellt werden soll.
	  * @see #Column()
	  */
	 public Column(String s) {
		 this.value = s;
	 }

	/**
	 * Zurückgeben des Spaltenwerts
	 * @return Der Eintrag als String
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setzen des Spaltenwerts
	 * @param value Neuer Spaltenwert
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Umwandeln des <code>Column</code>-Objekts in einen String.
	 * Da das einzige Attribut <code>value</code> bereits ein String ist, gibt
	 * die Methode nur dieses zurück.
	 */
	@Override
	public String toString() {
		return this.value;
	}
	 
}
