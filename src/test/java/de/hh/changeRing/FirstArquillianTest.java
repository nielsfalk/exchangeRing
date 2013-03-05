package de.hh.changeRing;

import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.calendar.Event;
import de.hh.changeRing.testPersistence.TestBlubb;
import de.hh.changeRing.testPersistence.TestPersistence;
import de.hh.changeRing.transaction.Transaction;
import de.hh.changeRing.user.DepotItem;
import de.hh.changeRing.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(Arquillian.class)
public class FirstArquillianTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(TestPersistence.class, User.class, Advertisement.class, BaseEntity.class, DepotItem.class, Event.class, TestBlubb.class, Transaction.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
    }

    @EJB
    TestPersistence testPersistence;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void bla() {

        System.out.println("asdgdag");
    }
}
