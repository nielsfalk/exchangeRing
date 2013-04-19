package de.hh.changeRing.transaction;

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
import java.math.BigDecimal;
import java.util.List;

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
public class TransactionCreatorTest extends MoneyTest {
    private static User owner = createTestMember();
    private static User receiver = createTestMember();

    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(UserSession.class, TransactionCreator.class, DataPump.class);
    }

    @Inject
    UserSession userSession;

    @EJB
    TransactionCreator transactionCreator;


    @Before
    public void login() {
        userSession.setIdOrEmail(owner.getId().toString());
        userSession.setPassword(PASSWORD);
        userSession.login();
        refresheEventWasFired=false;
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

    protected void expectTransactionProcessed() {
        assertThat(userSession.getUser().getBalance(), is(new BigDecimal("-30.00")));
        assertThat(MoneyTest.refresheEventWasFired, is(true));
        super.expectTransactionProcessed(owner, receiver);
    }
}
