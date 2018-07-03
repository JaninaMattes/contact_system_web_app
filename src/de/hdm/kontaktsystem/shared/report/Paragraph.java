package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;

/**
 * <p>
 * Um Texte strukturiert abspeichern zu koennen, reicht die Verwendung der Klasse
 * <code>String</code> nicht aus, da allein das Hinzufuegen eines Zeilenumbruchs
 * Kenntnisse ueber das Zielformat voraussetzt. Beispielsweise wuerde man bei einer 
 * rein textuellen Darstellung <code>\n</code> einfuegen, um Absaetze zu markieren,
 * fuer HTML muss der ganze Absatz in entsprechendes Markup eingefuegt werden.
 * Die Klasse <code>Paragraph</code> ermoeglicht es hingegen, Text so anzuspeichern,
 * dass er spaeter durch <code>ReportWriter</code> in verschiedene
 * Zielformate konvertiert werden kann.
 * </p>
 * <p>
 * <code>Paragraph</code> ist <code>Serializable</code>, so das Objekte dieser
 * Klasse durch das Netzwerk uebertragbar sind.
 * </p>
 * 
 * @see de.hdm.thies.bankProjekt.shared.report.Paragraph
 * @author Sandra Prestel
 *
 */
public abstract class Paragraph implements Serializable {

	private static final long serialVersionUID = 1L;

}
