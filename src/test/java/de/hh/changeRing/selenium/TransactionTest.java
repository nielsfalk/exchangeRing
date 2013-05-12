package de.hh.changeRing.selenium;

import de.hh.changeRing.transaction.TransactionCreator;
import de.hh.changeRing.user.Administrator;
import de.hh.changeRing.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static de.hh.changeRing.Context.WELCOME_PAGE;
import static de.hh.changeRing.TestUtils.*;
import static org.hamcrest.CoreMatchers.is;
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
@RunWith(Arquillian.class)
@RunAsClient
public class TransactionTest extends SeleniumTest {
    private static final User USER = createTestMember();
    private static final User RECEIVER = createTestMember();


    @Deployment
    public static WebArchive createDeployment() {
        return warWithBasics().addClasses(DataPump.class, TransactionCreator.class);
    }

    @Test
    public void userAuthentication() {
        login(USER.getEmail(), PASSWORD);
        transaction();
        logout();
    }

    private void transaction() {
        browser.open(deploymentUrl.toString() + "/internal/transaction.xhtml?clear=clear&receiverId=" + RECEIVER.getId());
        browser.type("id=transactionForm-amount", "15");
        browser.type("id=transactionForm-subject", "Vielen Dank für die gute Arbeit");
        browser.click("id=transactionForm-showDialogButton");
        browser.click("id=transactionForm-confirm");
        browser.waitForPageToLoad("15000");
    }

    private void logout() {
        browser.open(deploymentUrl.toString() + "logout/logout.xhtml");
    }

    public void login(String idOrEmail, String password) {
        browser.open(deploymentUrl.toString());
        assertThat(browser.isElementPresent("id=loggedInHeaderForm"), is(false));
        browser.type("id=loginForm-idOrEmail", idOrEmail);
        browser.type("id=loginForm-password", password);
        browser.click("id=loginForm-loginButton_button");
        browser.waitForPageToLoad("15000");
        assertThat(browser.isElementPresent("id=loggedInHeaderForm"), is(true));
    }

    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void createUser() {
            entityManager.persist(USER);
            entityManager.persist(RECEIVER);
        }
    }
}
