package de.hh.changeRing.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<String> allButInfo() {
        List<String> result = new ArrayList<String>();
        for (EventType eventType : EventType.values()) {
            if (eventType != EventType.info) {
                result.add(eventType.name());
            }
        }
        return result;
    }
}
