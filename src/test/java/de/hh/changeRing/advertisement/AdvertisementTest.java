package de.hh.changeRing.advertisement;

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
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.hh.changeRing.advertisement.Advertisement.AdvertisementType.offer;
import static de.hh.changeRing.advertisement.Advertisement.AdvertisementType.request;
import static de.hh.changeRing.advertisement.Category.*;

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
public class AdvertisementTest extends FunctionalTest {
    private static final User user1 = createTestMember();
    private static final User user2 = createTestMember();
    private static final List<Advertisement> ADVERTISEMENTS = newArrayList();
    private static final Advertisement offer1 = createAdvertisement(office, user1, offer);
    private static final Advertisement offer2 = createAdvertisement(computer, user2, offer);
    private static final Advertisement offer3 = createAdvertisement(internet, user1, offer);
    private static final Advertisement offer4 = createAdvertisement(bike, user1, offer);
    private static final Advertisement request1 = createAdvertisement(esoteric, user1, request);
    private List<Advertisement> resultList;

    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(DataPump.class);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void offersInRootCategorie() {
        findAdvertisement(offer, root).expectAdvertisements(offer4, offer3, offer2, offer1);
    }


    @Test
    public void offersInSuperCategory() {
        findAdvertisement(offer, engineering).expectAdvertisements(offer3, offer2, offer1);
    }

    @Test
    public void offersInSpecialCategory() {
        findAdvertisement(offer, computer).expectAdvertisements(offer2);
    }

    @Test
    public void offersInEmptySpecialCategory() {
        findAdvertisement(offer, beauty).expectAdvertisements();
    }

    @Test
    public void requestsInRootCategory() {
        findAdvertisement(request, root).expectAdvertisements(request1);
    }

    @Test
    public void requestsInSpecialCategory() {
        findAdvertisement(request, esoteric).expectAdvertisements(request1);
    }

    @Test
    public void requestsInEmptySpecialCategory() {
        findAdvertisement(request, home).expectAdvertisements();
    }

    @Test
    public void newest3Offers() {
        getNewestAdvertisements(3, offer).expectAdvertisements(offer4, offer3, offer2);

    }

    private AdvertisementTest getNewestAdvertisements(int count, Advertisement.AdvertisementType type) {
        resultList = Advertisement.getNewestAdvertisements(count, type, entityManager);
        return this;
    }


    private AdvertisementTest findAdvertisement(Advertisement.AdvertisementType type, Category category) {
        resultList = Advertisement.findAdvertisement(type, category, entityManager);
        return this;
    }

    private void expectAdvertisements(Advertisement... advertisements) {
        expectResultList(resultList, advertisements);
    }

    private static Advertisement createAdvertisement(Category category, User user1, Advertisement.AdvertisementType offer) {
        Advertisement result = new Advertisement();
        result.setCategory(category);
        result.setType(offer);
        result.setOwner(user1);
        ADVERTISEMENTS.add(result);
        return result;
    }


    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void createUser() {
            for (Advertisement advertisement : ADVERTISEMENTS) {
                entityManager.persist(advertisement);
            }
            entityManager.persist(user1);
            entityManager.persist(user2);
        }
    }
}
