package de.hh.changeRing;

import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.calendar.Event;
import de.hh.changeRing.calendar.EventModel;
import de.hh.changeRing.calendar.EventType;
import de.hh.changeRing.user.User;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static de.hh.changeRing.advertisement.Advertisement.AdvertisementType.offer;
import static de.hh.changeRing.advertisement.Advertisement.AdvertisementType.request;
import static de.hh.changeRing.calendar.EventModel.TimeFilter.future;

@Model
public class GlobalData {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Event> getNextEventsInternal(int count) {
        return Event.findEvents(entityManager, EventModel.TimeFilter.future, EventType.allButInfo(), count);
    }

    public List<Event> getNextEventsPublic(int count) {
        return Event.findEvents(entityManager, EventModel.TimeFilter.future, EventType.publicTypes(), count);
    }

    private List<User> getNewestMembers(int count) {
        return entityManager.createNamedQuery("newestUser").setMaxResults(count).getResultList();
    }

    @Named
    @Produces
    public List<Event> getNext3EventsInternal() {
        return getNextEventsInternal(3);
    }

    @Named
    @Produces
    public List<Event> getNext3EventsPublic() {
        return getNextEventsPublic(3);
    }

    @Named
    @Produces
    public List<User> getNewestMembers8() {
        return getNewestMembers(8);
    }

    @Named
    @Produces
    public List<Advertisement> getNewestRequests() {
        return InitTestData.getNewestAdvertisements(3, request);
    }

    @Named
    @Produces
    public List<Advertisement> getNewestOffers() {
        return InitTestData.getNewestAdvertisements(3, offer);
    }

    @Named
    @Produces
    public List<Event> getEventsToDisplay() {
        return Event.findEvents(entityManager, future, EventType.publicTypes());
    }

    public List<User> getMembers() {
        return entityManager.createNamedQuery("allUsers").getResultList();
    }
}
