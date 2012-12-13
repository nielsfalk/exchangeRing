package de.hh.changeRing.testPersistence;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created with IntelliJ IDEA.
 * User: niles
 * Date: 13.12.12
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
@Singleton
@Startup
public class TestPersistence {
    //@PersistenceContext
    EntityManager entityManager;
    //
    //@PostConstruct
    public void testPersistence() {

        // List resultList = entityManager.createQuery("SELECT e FROM TestBlubb e").getResultList();
        // for (Object o : resultList) {
        //     System.out.println(o);
        // }
        System.out.println("bla persistence");
        entityManager.persist(new TestBlubb("bla"));
        System.out.println("find:"+entityManager.find(TestBlubb.class, 1L));
    }
}