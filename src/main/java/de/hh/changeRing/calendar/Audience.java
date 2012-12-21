package de.hh.changeRing.calendar;

/**
 * User: nielsfalk
 * Date: 20.12.12 09:46
 */
public enum Audience {
	internal("Nur für Tauschringmitglieder"),
	external("Informationen für werdende Mitglieder"),
	both("Alle");
	private String translation;

	Audience(String translation) {
		this.translation = translation;
	}
}
