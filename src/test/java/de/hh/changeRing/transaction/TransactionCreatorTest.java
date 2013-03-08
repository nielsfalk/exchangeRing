package de.hh.changeRing.transaction;

import de.hh.changeRing.FunctionalTest;
import de.hh.changeRing.user.DepotItem;
import de.hh.changeRing.user.DepotItemType;
import de.hh.changeRing.user.User;
import de.hh.changeRing.user.UserSession;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static de.hh.changeRing.user.DepotItemType.in;
import static de.hh.changeRing.user.DepotItemType.out;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
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

@RunWith(Arquillian.class)
public class TransactionCreatorTest extends FunctionalTest {
    private static final String SUBJECT = TransactionCreatorTest.class.getName();
    private static User owner = createTestUser();
    private static User receiver = createTestUser();

    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(UserSession.class, TransactionCreator.class, DataPump.class);
    }

    @Inject
    UserSession userSession;

    @EJB
    TransactionCreator transactionCreator;

    @PersistenceContext
    private
    EntityManager entityManager;

    @Before
    public void login() {
        userSession.setId(owner.getId().toString());
        userSession.setPassword(PASSWORD);
        userSession.login();
    }

    @Test
    public void transaction() {
        transactionCreator.setReceiver(receiver);
        transactionCreator.setAmount(30l);
        transactionCreator.setSubject(SUBJECT);
        transactionCreator.submit();
        expectTransactionProcessed();
    }

    @Test
    public void findOthers() {
        List<User> otherUsers = transactionCreator.getOtherUsers();
        assertThat(otherUsers.size(), is(1));
        assertThat(otherUsers.get(0), is(receiver));

    }

    private void expectTransactionProcessed() {
        assertThat(userSession.getUser().getBalance(), is(-30l));
        owner = refresh(owner);
        receiver = refresh(receiver);
        assertThat(owner.getBalance(), is(-30l));

        assertThat(receiver.getBalance(), is(30l));
        expectBalance(owner, -30l);
        expectDepotItem(owner, -30l, receiver, out);
        expectBalance(receiver, 30l);
        expectDepotItem(receiver, 30l, owner, in);
        expectSameTransAction(owner, receiver);
    }

    private void expectSameTransAction(User owner, User receiver) {
        assertThat(owner.getDepotItems().get(0).getTransaction(),
                is(receiver.getDepotItems().get(0).getTransaction()));
    }

    private void expectBalance(User user, long amount) {
        assertThat(user.getBalance(), is(amount));
    }

    private void expectDepotItem(User user, long amount, User other, DepotItemType type) {
        List<DepotItem> depotItems = user.getDepotItems();
        assertThat(depotItems.size(), is(1));

        DepotItem depotItem = depotItems.get(0);
        assertThat(depotItem.getAmount(), is(amount));
        assertThat(depotItem.getFormattedDate(), is(not(nullValue())));
        assertThat(depotItem.getOldBalance(), is(0l));
        assertThat(depotItem.getNewBalance(), is(amount));
        assertThat(depotItem.getOther(), is(other));
        assertThat(depotItem.getTransaction().getSubject(), is(SUBJECT));
        assertThat(depotItem.getType(), is(type));
    }

    private User refresh(User user) {
        return entityManager.find(User.class, user.getId());
    }

    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void createUser() {
            entityManager.persist(owner);
            entityManager.persist(receiver);
        }
    }

}
