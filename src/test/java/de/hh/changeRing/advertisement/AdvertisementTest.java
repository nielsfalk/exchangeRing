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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class AdvertisementTest extends FunctionalTest {
    private static final User user1 = createTestUser();
    private static final User user2 = createTestUser();
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
        findAdvertisement(offer, root).expectFoundOffers(offer1, offer2, offer3, offer4);
    }


    @Test
    public void offersInSuperCategory() {
        findAdvertisement(offer, engineering).expectFoundOffers(offer1, offer2, offer3);
    }

    @Test
    public void offersInSpecialCategory() {
        findAdvertisement(offer, computer).expectFoundOffers(offer2);
    }

    @Test
    public void offersInEmptySpecialCategory() {
        findAdvertisement(offer, beauty).expectFoundOffers();
    }

    @Test
    public void requestsInRootCategory() {
        findAdvertisement(request, root).expectFoundOffers(request1);
    }

    @Test
    public void requestsInSpecialCategory() {
        findAdvertisement(request, esoteric).expectFoundOffers(request1);
    }

    @Test
    public void requestsInEmptySpecialCategory() {
        findAdvertisement(request, home).expectFoundOffers();
    }

    @Test
    public void newest3Offers() {
        getNewestAdvertisements(3, offer).expectFoundOffers(offer1, offer2, offer3);

    }

    private AdvertisementTest getNewestAdvertisements(int count, Advertisement.AdvertisementType type) {
        resultList = Advertisement.getNewestAdvertisements(count, type, entityManager);
        return this;
    }


    private AdvertisementTest findAdvertisement(Advertisement.AdvertisementType type, Category category) {
        resultList = Advertisement.findAdvertisement(type, category, entityManager);
        return this;
    }

    private void expectFoundOffers(Advertisement... offers) {
        assertThat(resultList.size(), is(offers.length));
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
            entityManager.persist(user1);
            entityManager.persist(user2);
            for (Advertisement advertisement : ADVERTISEMENTS) {
                entityManager.persist(advertisement);
            }
        }
    }
}
