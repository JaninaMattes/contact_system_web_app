package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;
import java.util.Vector;


/**
 * Die Klasse <code>CompositeParagraph</code> stellt eine Menge einzelner Absätze (
 * <code>SimpleParagraph</code>-Objekte) dar. Diese werden als Unterabschnitte
 * in einem <code>Vector</code> abgelegt und verwaltet.
 * 
 * @see Paragraph
 * @see de.hdm.thies.bankProjekt.shared.report.CompositeParagraph
 * @author Sandra Prestel
 *
 */
public class CompositeParagraph extends Paragraph implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Speichern der Unterabschnitte
	 */
	private Vector<SimpleParagraph> subParagraphs = new Vector<SimpleParagraph>();
	

	/**
	 * Hinzufügen eines Unterabschnitts
	 * 
	 * @param paragraph Der hinzuzufügende Unterabschnitt.
	 */
	public void addSubParagraph(SimpleParagraph paragraph) {
		this.subParagraphs.addElement(paragraph);
	}

	/**
	 * Entfernen eines Unterabschnitts
	 * 
	 * @param paragraph Der zu entfernende Unterabschnitt.
	 */
	public void removeSubParagraph(SimpleParagraph paragraph) {
		this.subParagraphs.removeElement(paragraph);
	}
	
	/**
	 * Auslesen aller Unterabschnitte.
	 * 
	 * @return <code>Vector</code>, der sämtliche Unterabschnitte enthält.
	 */
	public Vector<SimpleParagraph> getSubParagraphs() {
		return this.subParagraphs;
	}

	/**
	 * Auslesen der Anzahl der Unterabschnitte.
	 * 
	 * @return Anzahl der Unterabschnitte
	 */
	public int getNumParagraphs() {
		return this.subParagraphs.size();
	}
	
	/**
	 * Auslesen eines einzelnen Unterabschnitts.
	 * 
	 * @param i Der Index des gewünschten Unterabschnitts (0 <= i <n), mit n =
	 *          Anzahl der Unterabschnitte.
	 * 
	 * @return der gewünschte Unterabschnitt.
	 */
	public SimpleParagraph getParagraphAt(int i) {
		return this.subParagraphs.elementAt(i);
	}
	
	/**
	 * Umwandeln des <code>CompositeParagraph</code>-Objekts in einen String.
	 */
	@Override
	public String toString() {
		/*
		 * Leeres <code>StringBuffer</code>-Objekt, in das nacheinander alle 
		 * String-Repräsentationen der Unterabschnitte eingetragen werden.
		 */
		StringBuffer result = new StringBuffer();
		
		//Schleife über alle Unterabschnitte
		for (SimpleParagraph subParagraph : subParagraphs) {
			/*
			 * Umwandeln des jeweiligen Unterabschnitt in einen String und
			 * Anhängen an das <code>StringBuffer</code>-Objekt
			 */
			result.append(subParagraph.toString() + "\n");
		}
		/*
		 * Umwandeln des <code>StringBuffer</code>-Objekts in einen String
		 * und Rückgabe dessen.
		 */
		return result.toString();
	}
	
}
