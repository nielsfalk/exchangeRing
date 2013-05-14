package de.hh.changeRing.calendar;

import com.google.common.collect.Lists;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.List;

import static net.fortuna.ical4j.model.property.CalScale.GREGORIAN;
import static net.fortuna.ical4j.model.property.Version.VERSION_2_0;

@Stateless
@Path("calendar")
public class CalendarResource {

    private static final TzId TIMEZONE_BERLIN = TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone("Europe/Berlin").getVTimeZone().getTimeZoneId();

    @GET
    @Path("tauschring.ics")
    @Produces("text/Calendar")
    public StreamingOutput exportICal() {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                try {
                    new CalendarOutputter().output(createCalendar(), outputStream);
                } catch (ValidationException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private Calendar createCalendar() throws SocketException {
        Calendar result = new Calendar();
        result.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        result.getProperties().add(VERSION_2_0);
        result.getProperties().add(GREGORIAN);
        result.getComponents().add(createEvents());
        return result;
    }

    private List<VEvent> createEvents() {
        List<VEvent> result = Lists.newArrayList();
        VEvent event = new VEvent(
                new Date(new DateTime().toDate()),
                new Date(new DateTime().plusHours(2).toDate()),
                "Blubb");
        event.getProperties().add(TIMEZONE_BERLIN);
        event.getProperties().add(new Uid("blubb"));
        result.add(event);
        return result;
    }
}
