package de.hh.changeRing;

import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.calendar.Event;
import de.hh.changeRing.calendar.EventType;
import de.hh.changeRing.transaction.Transaction;
import de.hh.changeRing.user.*;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
public abstract class FunctionalTest {
    public static final Class<?>[] ENTITY_CLASSES = new Class<?>[]{Member.class, Administrator.class, SystemAccount.class, User.class, Advertisement.class, BaseEntity.class, DepotItem.class, Event.class, Transaction.class};


    protected static JavaArchive functionalJarWithEntities() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addClasses(ENTITY_CLASSES);
    }

    private static DateTime date(int daysToAdd) {
        return new DateTime().hourOfDay().withMinimumValue().plusHours(19).plusDays(daysToAdd);
    }

    protected static Event createEvent(int dayToAdd, EventType eventType, User user) {
        Event event = new Event();
        event.setWhen(date(dayToAdd));
        event.setEventType(eventType);
        event.setDuration(90);
        event.setTitle(eventType.name() + "bla" + System.currentTimeMillis());
        event.setUser(user);
        return event;
    }

    @Before
    public void setContext() {
        TestContext.set();
    }

    @After
    public void resetContext() {
        TestContext.reset();
    }

    protected void expectResultList(List<? extends BaseEntity> resultList, BaseEntity... expectedEvents) {
        assertThat(resultList.size(), is(expectedEvents.length));
        for (int i = 0; i < expectedEvents.length; i++) {
            assertThat(resultList.get(i), is(expectedEvents[i]));
        }
    }

    public static class TestContext extends Context {
        public static void set() {
            Context.dummyContext = new TestContext();
        }

        public static void reset() {
            Context.dummyContext = null;
        }

        @Override
        public void addMessage(String summary) {
        }

        @Override
        public void leavePublicEvents() {
        }
    }
}
