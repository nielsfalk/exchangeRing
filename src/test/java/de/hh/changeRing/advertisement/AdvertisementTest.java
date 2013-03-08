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

    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(DataPump.class);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void offersInRootCategorie() {
        Advertisement.AdvertisementType type = offer;
        Category category = root;
        List resultList = findAdvertisement(type, category);
        expectFoundOffers(resultList, offer1, offer2, offer3, offer4);
    }


    @Test
    public void offersInSuperCategory() {
        List resultList = findAdvertisement(offer, engineering);
        expectFoundOffers(resultList, offer1, offer2, offer3);
    }

    @Test
    public void offersInSpecialCategory() {
        List resultList = findAdvertisement(offer, computer);
        expectFoundOffers(resultList, offer2);
    }

    @Test
    public void offersInEmptySpecialCategory() {
        List resultList = findAdvertisement(offer, beauty);
        expectFoundOffers(resultList);
    }

    @Test
    public void requestsInRootCategory() {
        List resultList = findAdvertisement(request, root);
        expectFoundOffers(resultList, request1);
    }

    @Test
    public void requestsInSpecialCategory() {
        List resultList = findAdvertisement(request, esoteric);
        expectFoundOffers(resultList, request1);
    }

    @Test
    public void requestsInEmptySpecialCategory() {
        List resultList = findAdvertisement(request, home);
        expectFoundOffers(resultList);
    }


    private List findAdvertisement(Advertisement.AdvertisementType type, Category category) {
        return Advertisement.findAdvertisement(type, category, entityManager);
    }

    private void expectFoundOffers(List resultList, Advertisement... offers) {
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
