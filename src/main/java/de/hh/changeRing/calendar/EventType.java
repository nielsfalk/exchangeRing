package de.hh.changeRing.calendar;

import java.util.ArrayList;
import java.util.List;

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

    public static List<EventType> allButInfo() {
        List<EventType> result = new ArrayList<EventType>();
        for (EventType eventType : EventType.values()) {
            if (eventType != EventType.info) {
                result.add(eventType);
            }
        }
        return result;
    }

    public static List<EventType> publicTypes() {
        List<EventType> result = new ArrayList<EventType>();
        for (EventType eventType : EventType.values()) {
            if (eventType != EventType.individual) {
                result.add(eventType);
            }
        }
        return result;
    }
}
