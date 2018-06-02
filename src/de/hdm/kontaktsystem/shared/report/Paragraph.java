/**
 * 
 */
package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;

/**
 * <p>
 * Um Texte strukturiert abspeichern zu können, reicht die Verwendung der Klasse
 * <code>String</code> nicht aus, da allein das Hinzufügen eines Zeilenumbruchs
 * Kenntnisse über das Zielformat voraussetzt. Beispielsweise würde man bei einer 
 * rein textuellen Darstellung <code>\n</code> einfügen, um Absätze zu markieren,
 * für HTML muss der ganze Absatz in entsprechendes Markup eingefügt werden.
 * Die Klasse <code>Paragraph</code> ermöglicht es hingegen, Text so anzuspeichern,
 * dass er später durch <code>ReportWriter</code> in verschiedene
 * Zielformate konvertiert werden kann.
 * </p>
 * <p>
 * <code>Paragraph</code> ist <code>Serializable</code>, so das Objekte dieser
 * Klasse durch das Netzwerk übertragbar sind.
 * </p>
 * 
 * @see de.hdm.thies.bankProjekt.shared.report.Paragraph
 * @author Sandra
 *
 */
public abstract class Paragraph implements Serializable {

	private static final long serialVersionUID = 1L;

}
