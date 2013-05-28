package de.hh.changeRing.selenium;

import de.hh.changeRing.user.Administrator;
import de.hh.changeRing.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static de.hh.changeRing.Context.WELCOME_PAGE;
import static de.hh.changeRing.TestUtils.*;
import static org.hamcrest.Matchers.endsWith;
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
@WarpTest
public class AuthenticationTest extends SeleniumTest {
    private static final User USER = createTestMember();
    private static final Administrator ADMINISTRATOR = createAdministrator();
    public static final String MEMBERS_XHTML = "internal/members/members.xhtml";
    public static final String CONFIGURATION_XHTML = "internal/admin/configuration.xhtml";

    @Deployment
    public static WebArchive createDeployment() {
        return warWithBasics().addClass(DataPump.class);
    }

    @Test
    public void userAuthentication() {
        internalAreaNotAccessible();
        login(USER.getEmail(), PASSWORD);
        internalAreaIsAccessible();
        adminAreaNotAccessible();
        logout();
        internalAreaNotAccessible();
    }

    @Test
    public void adminAuthentication() {
        adminAreaNotAccessible();
        login(ADMINISTRATOR.getEmail(), PASSWORD);
        adminAreaIsAccessible();

        logout();
        adminAreaNotAccessible();
    }

    private void adminAreaIsAccessible() {
        adminAreaAccessible(true);
    }

    private void adminAreaNotAccessible() {
        adminAreaAccessible(false);
    }


    private void internalAreaNotAccessible() {
        internalAreaAccessible(false);
    }

    private void internalAreaIsAccessible() {
        internalAreaAccessible(true);
    }

    private void internalAreaAccessible(boolean accessible) {
        browser.open(deploymentUrl.toString() + MEMBERS_XHTML);
        assertThat(browser.getLocation(), endsWith(accessible ? MEMBERS_XHTML : WELCOME_PAGE));
    }

    private void adminAreaAccessible(boolean accessible) {
        browser.open(deploymentUrl.toString() + CONFIGURATION_XHTML);
        assertThat(browser.getLocation(), endsWith(accessible ? CONFIGURATION_XHTML : WELCOME_PAGE));
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
        }
    }
}
