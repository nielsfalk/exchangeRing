package de.hh.changeRing;

import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.calendar.Event;
import de.hh.changeRing.user.User;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.util.List;

import static de.hh.changeRing.advertisement.Advertisement.AdvertisementType.offer;
import static de.hh.changeRing.advertisement.Advertisement.AdvertisementType.request;

@Model
public class GlobalData {
    private List<User> getNewestMembers(int count) {
        return InitTestData.getNewestMembers(count);
    }

    private List<Event> getNextEventsInternal(int count) {
        return InitTestData.getNextEventsInternal(count);
    }

    private List<Event> getNextEventsPublic(int count) {
        return InitTestData.getNextEventsPublic(count);
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

    public List<User> getMembers() {
        return InitTestData.getUsers();
    }
}
