package de.hh.changeRing.calendar;

import com.google.common.collect.Lists;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.net.URI;
import java.util.List;

import static net.fortuna.ical4j.model.property.CalScale.GREGORIAN;
import static net.fortuna.ical4j.model.property.Version.VERSION_2_0;

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

@Stateless
@Path("calendar")
public class CalendarResource {

    private static final TzId TIMEZONE_BERLIN = TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone("Europe/Berlin").getVTimeZone().getTimeZoneId();
    @PersistenceContext
    private EntityManager entityManager;

    @Context
    private HttpServletRequest httpServletRequest;

    @GET
    @Path("tauschring.ics")
    @Produces("text/Calendar")
    public String exportICal(@QueryParam("exclude") List<String> exclude) {

        try {
            Calendar calendar = createCalendar(EventType.withoutType(exclude));
            calendar.validate();
            return calendar.toString();
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private Calendar createCalendar(List<EventType> filter) {
        Calendar result = new Calendar();
        result.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        result.getProperties().add(VERSION_2_0);
        result.getProperties().add(GREGORIAN);

        //noinspection unchecked
        result.getComponents().addAll(createEvents(filter));
        return result;
    }

    private List<VEvent> createEvents(List<EventType> filter) {
        List<VEvent> result = Lists.newArrayList();
        for (Event event : Event.findEvents(entityManager, filter)) {
            result.add(toVEvent(event));
        }
        return result;
    }

    private VEvent toVEvent(Event event) {
        VEvent result = new VEvent(
                new DateTime(event.getWhen().toDate()),
                new DateTime(event.getEnd().toDate()),
                event.getDisplayTitle());
        result.getProperties().add(TIMEZONE_BERLIN);
        result.getProperties().add(new Uid("exchangeRingHH-" + event.getEventType().getDatabaseValue() + "-" + event.getId()));
        result.getProperties().add(new Location(event.getLocation()));
        result.getProperties().add(new Description(event.getContent()));
        result.getProperties().add(new Url(URI.create(baseUrl() + event.getUri())));
        VAlarm alarm = new VAlarm(new DateTime(event.getWhen().minusHours(1).toDate()));
        alarm.getProperties().add(Action.DISPLAY);
        alarm.getProperties().add(new Description(event.getDisplayTitle()));
        result.getAlarms().add(alarm);
        return result;
    }

    private String baseUrl() {
        String url = httpServletRequest.getRequestURL().toString();
        return url.substring(0, url.indexOf(httpServletRequest.getRequestURI()));
    }
}
