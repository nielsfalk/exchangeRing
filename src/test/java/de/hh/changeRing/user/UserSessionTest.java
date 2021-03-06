package de.hh.changeRing.user;

import de.hh.changeRing.FunctionalTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.joda.time.DateMidnight;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static de.hh.changeRing.TestUtils.PASSWORD;
import static de.hh.changeRing.TestUtils.createAdministrator;
import static de.hh.changeRing.TestUtils.createSystemAccount;
import static de.hh.changeRing.TestUtils.createTestMember;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
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
public class UserSessionTest extends FunctionalTest {
    private static final User USER = createTestMember();
	private static final Administrator ADMINISTRATOR = createAdministrator();
	private static final SystemAccount SYSTEM_ACCOUNT= createSystemAccount();

    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(UserSession.class, DataPump.class);
    }

    @Inject
    UserSession userSession;

    @Test
    public void loginWithId() {
	    testSuccessfulLogin(USER.getId().toString(), PASSWORD);
    }

	@Test
    public void loginWithIdWrongPassword() {
		testUnsuccessfulLogin(USER, "wrong");
    }

	@Test
    public void loginWithEmail() {
		testSuccessfulLogin(USER.getEmail(), PASSWORD);
    }

	@Test
	public void loginAdmin() {
		testSuccessfulLogin(ADMINISTRATOR.getId().toString(), PASSWORD);
	}

	@Test
	public void loginSystemShouldFail() {
		testUnsuccessfulLogin(SYSTEM_ACCOUNT, PASSWORD);
	}

	private void testUnsuccessfulLogin(User user, String password) {
		userSession.setIdOrEmail(user.getId().toString());
		userSession.setPassword(password);
		userSession.login();
		assertThat(userSession.isNotLoggedIn(), is(true));
	}

	private void testSuccessfulLogin(String idOrEmail, String password) {
		userSession.setIdOrEmail(idOrEmail);
		userSession.setPassword(password);
		userSession.login();
		assertThat(userSession.isLoggedIn(), is(true));
		assertThat(userSession.getUser().getLastLogin(), is(greaterThan(new DateMidnight().toDateTime())));
		assertThat(userSession.getUser().getLastLogin(), is(lessThan(new DateMidnight().plusDays(1).toDateTime())));
	}

	@Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void createUser() {
	        entityManager.persist(USER);
	        entityManager.persist(ADMINISTRATOR);
	        entityManager.persist(SYSTEM_ACCOUNT);
        }
    }

}
