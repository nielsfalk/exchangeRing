package de.hh.changeRing.calendar;

public enum EventType {
    summerFestival("Sommerfest"),
    fleaMarket("Flohmarkt"),
    regularsTable("Stammtisch"),
    individual("Individuel"),
    organization("Orga"),
    info("Infostand");
    public final String translation;

    EventType(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }
}
