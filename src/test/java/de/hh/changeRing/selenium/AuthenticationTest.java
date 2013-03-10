package de.hh.changeRing.selenium;

import de.hh.changeRing.FunctionalTest;
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
import static de.hh.changeRing.FunctionalTest.PASSWORD;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
@RunAsClient
public class AuthenticationTest extends SeleniumTest {
    private static final User USER = FunctionalTest.createTestUser();
    public static final String MEMBERS_XHTML = "internal/members/members.xhtml";

    @Deployment
    public static WebArchive createDeployment() {
        return warWithBasics().addClass(DataPump.class);
    }

    @Test
    public void authentication() {
        internalAreaNotAccessible();
        login("hans@meiser.de", PASSWORD);
        internalAreaIsAccessible();
        logout();
        internalAreaNotAccessible();
    }

    private void logout() {
        browser.open(deploymentUrl.toString() + "logout/logout.xhtml");
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
        }
    }
}
