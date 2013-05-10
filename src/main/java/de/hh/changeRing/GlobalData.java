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
import static de.hh.changeRing.advertisement.Advertisement.getNewestAdvertisements;
import static de.hh.changeRing.calendar.EventModel.TimeFilter.future;

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
@Model
public class GlobalData {
    @PersistenceContext
    private EntityManager entityManager;

    @Named
    @Produces
    public List<Event> getNext3EventsInternal() {
        return Event.findEvents(entityManager, EventModel.TimeFilter.future, EventType.allButInfo(), 3);
    }

    @Named
    @Produces
    public List<Event> getNext3EventsPublic() {
        return Event.findEvents(entityManager, EventModel.TimeFilter.future, EventType.publicTypes(), 3);
    }

    @Named
    @Produces
    public List<User> getNewestMembers8() {
        return entityManager.createNamedQuery("newestMembers", User.class).setMaxResults(8).getResultList();
    }

    @Named
    @Produces
    public List<Advertisement> getNewestRequests() {
        return getNewestAdvertisements(3, request, entityManager);
    }

    @Named
    @Produces
    public List<Advertisement> getNewestOffers() {
        return getNewestAdvertisements(3, offer, entityManager);
    }

    @Named
    @Produces
    public List<Event> getEventsToDisplay() {
        return Event.findEvents(entityManager, future, EventType.publicTypes());
    }
}
