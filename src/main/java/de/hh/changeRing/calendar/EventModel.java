package de.hh.changeRing.calendar;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hh.changeRing.calendar.EventModel.TimeFilter.future;

/**
 * ----------------GNU General Public License--------------------------------
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p/>
 * ----------------in addition-----------------------------------------------
 * <p/>
 * In addition, each military use, and the use for interest profit will be excluded. Environmental damage caused by the
 * use must be kept as small as possible.
 */
@Named
@SessionScoped
public class EventModel implements Serializable {
    private TimeFilter timeFilter = future;
    private Map<String, EventType> typeFilters;
    private List<String> selectedTypeFilters = new ArrayList<String>();

    public EventModel() {
        typeFilters = new HashMap<String, EventType>();
        for (EventType eventType : EventType.values()) {
            typeFilters.put(eventType.translation, eventType);
            if (eventType != EventType.info) {
                selectedTypeFilters.add(eventType.name());
            }
        }
    }

    public void refresh(){
        System.out.println("bla");
    }

    public String selectedStyleClass(TimeFilter timeFilter) {
        return getTimeFilter().equals(timeFilter) ? "ui-state-active" : "";
    }

    public TimeFilter getTimeFilter() {
        return timeFilter;
    }

    public Map<String, EventType> getTypeFilters() {
        return typeFilters;
    }

    public void setTimeFilter(TimeFilter timeFilter) {
        this.timeFilter = timeFilter;
    }

    public List<String> getSelectedTypeFilters() {
        return selectedTypeFilters;
    }

    public void setSelectedTypeFilters(List<String> selectedTypeFilters) {
        this.selectedTypeFilters = selectedTypeFilters;
    }

    public static enum TimeFilter {
        future("Kommende"), past("Vergangene");
        private String translation;

        TimeFilter(String translation) {
            this.translation = translation;
        }

        public String getTranslation() {
            return translation;
        }
    }

    public static enum EventType {
        summerFestival("Sommerfest"),
        fleaMarket("Flohmarkt"),
        regularsTable("Stammtisch"),
        individual("Individuel"),
        organization("Orga"),
        info("Infostand");
        private String translation;

        EventType(String translation) {
            this.translation = translation;
        }
    }
}
