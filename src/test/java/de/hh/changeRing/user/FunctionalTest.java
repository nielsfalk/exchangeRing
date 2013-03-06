package de.hh.changeRing.user;

import de.hh.changeRing.BaseEntity;
import de.hh.changeRing.Context;
import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.calendar.Event;
import de.hh.changeRing.transaction.Transaction;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;

public class FunctionalTest {
    public static final User USER = createTestUser();
    public static final String PASSWORD = "321";


    private static User createTestUser() {
        User result = new User();
        result.setNickName("nick");
        result.setEmail("hans@meiser.de");
        result.setPassword(PASSWORD);
        return result;
    }

    static JavaArchive functionalJarWithEntities() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addClasses(User.class, Advertisement.class, BaseEntity.class, DepotItem.class, Event.class, Transaction.class);
    }

    @Before
    public void setContext() {
        TestContext.set();
    }

    @After
    public void resetContext() {
        TestContext.reset();
    }

    public static class TestContext extends Context {
        public static void set() {
            Context.dummyContext = new TestContext();
        }

        public static void reset() {
            Context.dummyContext = null;
        }

        @Override
        public void addMessage(String summary) {}

        @Override
        public void leavePublicEvents() {}
    }
}