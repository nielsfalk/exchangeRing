package de.hh.changeRing.testPersistence;

import de.hh.changeRing.InitTestData;
import de.hh.changeRing.user.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@SuppressWarnings("UnusedDeclaration")
@Singleton
@Stateless
@Startup
public class TestPersistence {
    @PersistenceContext
    EntityManager entityManager;

    @PostConstruct
    public void testPersistence() {

        // List resultList = entityManager.createQuery("SELECT e FROM TestBlubb e").getResultList();
        // for (Object o : resultList) {
        //     System.out.println(o);
        // }
        if (entityManager.createQuery("select user from User user").getResultList().isEmpty()) {
            for (User user : InitTestData.getUsers()) {
                entityManager.persist(user);
            }
            System.out.println(entityManager.createQuery("select user from User user").getResultList().size());
        }
        System.out.println("bla persistence");
        entityManager.persist(new TestBlubb("bla"));
        System.out.println("find:" + entityManager.find(TestBlubb.class, 1L));
    }
}
