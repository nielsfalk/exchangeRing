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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
@RunAsClient
public class AuthenticationTest extends SeleniumTest {
    private static final User USER = FunctionalTest.createTestUser();

    @Deployment
    public static WebArchive createDeployment() {
        return warWithBasics().addClass(DataPump.class);
    }


    @Test
    public void login() {
        browser.open(deploymentUrl.toString());
        assertThat(browser.isElementPresent("id=loggedInHeaderForm"), is(false));
        browser.type("id=loginForm-idOrEmail", "hans@meiser.de");
        browser.type("id=loginForm-password", FunctionalTest.PASSWORD);
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
