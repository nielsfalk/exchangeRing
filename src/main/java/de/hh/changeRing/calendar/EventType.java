package de.hh.changeRing.calendar;

import com.google.common.collect.Lists;
import de.hh.changeRing.DatabaseMappableEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * ----------------GNU General Public License--------------------------------
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * ----------------in addition-----------------------------------------------
 * <p/>
 * In addition, each military use, and the use for interest profit will be
 * excluded.
 * Environmental damage caused by the use must be kept as small as possible.
 */

public enum EventType implements DatabaseMappableEnum<String> {
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

    @Override
    public String getDatabaseValue() {
        return this.name();
    }

    public static List<EventType> withoutType(List<String> databaseValues) {
        ArrayList<EventType> result = Lists.newArrayList();
        result.addAll(asList(values()));
        for (String databaseValue : databaseValues) {
            result.remove(forDatabaseValue(databaseValue));
        }
        return result;
    }

    private static EventType forDatabaseValue(String databaseValue) {
        for (EventType eventType : values()) {
            if (eventType.getDatabaseValue().equals(databaseValue)) {
                return eventType;
            }
        }
        throw new RuntimeException(databaseValue + " is unknown");
    }

    public static List<EventType> reverse(Collection<EventType> values) {
        ArrayList<EventType> eventTypes = Lists.newArrayList(values());
        for (EventType value : values) {
            eventTypes.remove(value);
        }
        return eventTypes;
    }
}
