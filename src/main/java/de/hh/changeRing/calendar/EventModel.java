package de.hh.changeRing.calendar;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import de.hh.changeRing.Context;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import javax.enterprise.context.SessionScoped;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private final Map<String, EventType> typeFilters;
    private List<EventType> selectedTypeFilters = new ArrayList<EventType>();

    @PersistenceContext
    private EntityManager entityManager;
    private Integer notification = 90;
    private Event selectedEvent;


    public EventModel() {
        typeFilters = new HashMap<String, EventType>();
        for (EventType eventType : EventType.values()) {
            typeFilters.put(eventType.translation, eventType);
        }
        selectedTypeFilters = EventType.allButInfo();
    }

    public List<Event> getEventsToDisplay() {
        if (selectedEvent!=null) {
            return Lists.newArrayList(selectedEvent);
        }
        return Event.findEvents(entityManager, timeFilter, selectedTypeFilters);
    }

    public void refresh() {
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
        refresh();
    }

    public List<EventType> getSelectedTypeFilters() {
        return selectedTypeFilters;
    }

    public void setSelectedTypeFilters(List<EventType> selectedTypeFilters) {
        this.selectedTypeFilters = selectedTypeFilters;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public String getiCalUrl() {
        List<String> parameter = Lists.newArrayList();
        if (notification > 0) {
            parameter.add("alarm=" + notification);
        }
        for (EventType eventType : EventType.reverse(selectedTypeFilters)) {
            parameter.add("exclude=" + eventType.getDatabaseValue());
        }
        return Context.context().webCalUrl() + '?' + Joiner.on('&').join(parameter);
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public boolean isEventSelected() {
        return selectedEvent!=null;
    }

    public static enum TimeFilter {
        future, past;

        public DateMidnight today() {
            return new DateMidnight();
        }

        public DateMidnight tomorrow() {
            return new DateMidnight().plusDays(1);
        }

        public DateTime relevant() {
            return (this.equals(future) ? today() : tomorrow()).toDateTime();
        }
    }

    @FacesConverter("eventTypeConverter")
    public static class EventTypeConverter extends EnumConverter {
        public EventTypeConverter() {
            super(EventType.class);
        }
    }
}
