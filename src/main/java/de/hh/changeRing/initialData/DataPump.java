package de.hh.changeRing.initialData;

import de.hh.changeRing.user.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@SuppressWarnings("UnusedDeclaration")
@Singleton
@Startup
public class DataPump {
    @PersistenceContext
    EntityManager entityManager;

    @PostConstruct
    public void testPersistence() {
        if (entityManager.createNamedQuery("allUsers", User.class).setMaxResults(1).getResultList().isEmpty()) {
            for (User user : InitTestData.getUsers()) {
                user.initialStuffAfterParsing();
                entityManager.persist(user);
            }
        }
    }
}
