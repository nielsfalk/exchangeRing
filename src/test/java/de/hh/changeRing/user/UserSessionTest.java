package de.hh.changeRing.user;

import de.hh.changeRing.FunctionalTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

@RunWith(Arquillian.class)
public class UserSessionTest extends FunctionalTest {
    private static final User USER = createTestUser();

    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(UserSession.class, DataPump.class);
    }

    @Inject
    UserSession userSession;

    @Test
    public void loginWithId() {
        userSession.setIdOrEmail(USER.getId().toString());
        userSession.setPassword(PASSWORD);
        userSession.login();
        assertThat(userSession.isLoggedIn(), is(true));
    }

    @Test
    public void loginWithIdWrong() {
        userSession.setIdOrEmail(USER.getId().toString());
        userSession.setPassword("wrong");
        userSession.login();
        assertThat(userSession.isNotLoggedIn(), is(true));
    }

    @Test
    public void loginWithEmail() {
        userSession.setIdOrEmail(USER.getEmail());
        userSession.setPassword(PASSWORD);
        userSession.login();
        assertThat(userSession.isLoggedIn(), is(true));
    }

    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void createUser() {
            entityManager.persist(USER);
        }
    }

}
