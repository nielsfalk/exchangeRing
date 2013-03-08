package de.hh.changeRing.calendar;

import de.hh.changeRing.FunctionalTest;
import de.hh.changeRing.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.hh.changeRing.calendar.EventModel.TimeFilter.future;
import static de.hh.changeRing.calendar.EventModel.TimeFilter.past;
import static de.hh.changeRing.calendar.EventType.fleaMarket;
import static de.hh.changeRing.calendar.EventType.individual;
import static de.hh.changeRing.calendar.EventType.info;
import static java.util.Calendar.DAY_OF_MONTH;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

@RunWith(Arquillian.class)
public class EventTest extends FunctionalTest {
    private static final User USER = createTestUser();
    private static final List<Event> EVENTS = newArrayList();

    private static final Event PRESENT_EVENT = createEvent(0, fleaMarket);
    private static final Event PAST_EVENT1 = createEvent(-1, fleaMarket);
    private static final Event PAST_EVENT2 = createEvent(-2, individual);
    private static final Event PAST_EVENT3 = createEvent(-3, info);
    private static final Event FUTURE_EVENT1 = createEvent(1, fleaMarket);
    private static final Event FUTURE_EVENT2 = createEvent(2, individual);
    private static final Event FUTURE_EVENT3 = createEvent(3, info);

    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(DataPump.class);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void findFilteredFutureEvents() {
        List<Event> events = Event.findEvents(entityManager, future, newArrayList(individual));
        expectEventsContainInCorrectOrder(events, FUTURE_EVENT2);
    }

    @Test
    public void findAllFutureEvents() {
        List<Event> events = Event.findEvents(entityManager, future, allEvents());
        expectEventsContainInCorrectOrder(events, PRESENT_EVENT, FUTURE_EVENT1, FUTURE_EVENT2, FUTURE_EVENT3);
    }

    private ArrayList<EventType> allEvents() {
        return newArrayList(EventType.values());
    }

    @Test
    public void find2FutureEvents() {
        List<Event> events = Event.findEvents(entityManager, future, allEvents(), 2);
        expectEventsContainInCorrectOrder(events, PRESENT_EVENT, FUTURE_EVENT1);
    }

    @Test
    public void findAllPastEvents() {
        List<Event> events = Event.findEvents(entityManager, past, allEvents());
        expectEventsContainInCorrectOrder(events, PRESENT_EVENT, PAST_EVENT1, PAST_EVENT2, PAST_EVENT3);
    }

    @Test
    public void find2PastEvents() {
        List<Event> events = Event.findEvents(entityManager, past, allEvents(), 2);
        expectEventsContainInCorrectOrder(events, PRESENT_EVENT, PAST_EVENT1);
    }

    private void expectEventsContainInCorrectOrder(List<Event> events, Event... expectedEvents) {
        assertThat(events.size(), is(expectedEvents.length));
        for (int i = 0; i < expectedEvents.length; i++) {
            Event expectedEvent = expectedEvents[i];
            assertThat(events.get(i), is(expectedEvent));
        }
    }

    private static Date date(int daysToAdd) {
        GregorianCalendar from = new GregorianCalendar();
        from.set(Calendar.HOUR_OF_DAY, 19);
        from.set(Calendar.MINUTE, 0);
        from.add(DAY_OF_MONTH, daysToAdd);
        return from.getTime();
    }

    private static Event createEvent(int dayToAdd, EventType fleaMarket) {
        Event event = new Event();
        event.setWhen(date(dayToAdd));
        event.setEventType(fleaMarket);
        event.setTitle("bla" + System.currentTimeMillis());
        event.setUser(USER);
        EVENTS.add(event);
        return event;
    }

    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void createUser() {
            entityManager.persist(USER);
            for (Event event : EVENTS) {
                entityManager.persist(event);
            }
        }
    }
}
