package de.hh.changeRing.calendar;

import com.google.common.collect.Lists;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.TzId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Url;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URI;
import java.util.List;

import static net.fortuna.ical4j.model.property.CalScale.GREGORIAN;
import static net.fortuna.ical4j.model.property.Version.VERSION_2_0;

@Stateless
@Path("calendar")
public class CalendarResource {

	private static final TzId TIMEZONE_BERLIN = TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone("Europe/Berlin").getVTimeZone().getTimeZoneId();
	@PersistenceContext
	private EntityManager entityManager;

	@GET
	@Path("tauschring.ics")
	@Produces("text/Calendar")
	public StreamingOutput exportICal(@QueryParam("exclude") List<String> exclude) {
		final List<EventType> filter = EventType.withoutType(exclude);
		return new StreamingOutput() {
			@Override
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				try {
					new CalendarOutputter().output(createCalendar(filter), outputStream);
				} catch (ValidationException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	private Calendar createCalendar(List<EventType> filter) throws SocketException {
		Calendar result = new Calendar();
		result.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		result.getProperties().add(VERSION_2_0);
		result.getProperties().add(GREGORIAN);

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
		result.getProperties().add(new Url(URI.create("http://www.tauschring-hamburg.org")));
		VAlarm alarm = new VAlarm(new DateTime(event.getWhen().minusHours(1).toDate()));
		alarm.getProperties().add(Action.DISPLAY);
		alarm.getProperties().add(new Description(event.getDisplayTitle()));
		result.getAlarms().add(alarm);
		return result;
	}
}
