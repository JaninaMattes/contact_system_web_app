/**
 * 
 */
package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;

/**
 * Die Klasse <code>SimpleParagraph</code> stellt einzelne Absätze dar.
 * Der Inhalt der Absätze wird als String gespeichert.
 * Der Anwender sollte in diesem String keinerlei Formatierungssymbole einfügen, 
 * da diese in der Regel zielformatspezifisch sind.
 * 
 * @see Paragraph
 * @see de.hdm.thies.bankProjekt.shared.report.SimpleParagraph
 * @author Sandra
 *
 */
public class SimpleParagraph extends Paragraph implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Inhalt des Absatzes.
	 */
	private String text = "";

	
	/**
	 * Serialisierbare Klassen, die mittels GWT-RPC transportiert werden sollen, müssen 
	 * einen Konstruktor ohne übergebene Parameter besitzen. Besitzt eine Klasse min. einen
	 * explizit angegebenen Konstruktor, so gibt es nur diese explizit implementierten 
	 * Konstruktoren, der Default-Konstruktor (No-Argument-Konstruktor, der automatisch 
	 * existiert, wenn kein Konstruktor explizit angegeben wird) ist dann nicht vorhanden.
	 * Da mit {@link #SimpleParagraph(String)} ein expliziter Konstruktor angegeben wird, muss der
	 * No-Argument-Konstruktor also explizit implementiert werden.
	 * 
	 * @see #SimpleParagraph(String)
	 */
	public SimpleParagraph() {
	}
	
	/**
	 * Mit diesem Konstruktor kann bei der Initialisierung von <code>SimpleParagraph</code>-Objekten
	 * deren Inhalt bereits angegeben werden.
	 * 
	 * @param value Inhalt des Paragraphs
	 */
	public SimpleParagraph(String text) {
		this.text = text;
	}

	
	/**
	 * Zurückgeben des Inhalts
	 * @return Inhalt als String
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Setzen des Inhalts
	 * @param text Neuer Inhalt des Paragraphs (String)
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Umwandeln des <code>SimpleParagraph</code>-Objekts in einen String.
	 * Da das einzige Attribut <code>text</code> bereits ein String ist, gibt
	 * die Methode nur dieses zurück.
	 */
	@Override
	public String toString() {
		return this.text;
	}
}
