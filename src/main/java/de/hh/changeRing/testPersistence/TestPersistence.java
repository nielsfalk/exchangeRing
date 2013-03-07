package de.hh.changeRing.testPersistence;

import de.hh.changeRing.InitTestData;
import de.hh.changeRing.user.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@SuppressWarnings("UnusedDeclaration")
@Singleton
@Startup
public class TestPersistence {
    @PersistenceContext
    EntityManager entityManager;

    @PostConstruct
    public void testPersistence() {
        if (entityManager.createNamedQuery("allUsers").setMaxResults(1).getResultList().isEmpty()) {
            for (User user : InitTestData.getUsers()) {
                user.initialStuffAfterParsing();
                entityManager.persist(user);
            }
            System.out.println(entityManager.createQuery("select user from tr_user user").getResultList().size());
        }
        System.out.println("bla persistence");
    }
}
