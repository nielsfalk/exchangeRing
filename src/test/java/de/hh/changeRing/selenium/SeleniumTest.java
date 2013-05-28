package de.hh.changeRing.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import de.hh.changeRing.Context;
import de.hh.changeRing.FunctionalTest;
import de.hh.changeRing.RestConfig;
import de.hh.changeRing.ThemeSwitcher;
import de.hh.changeRing.calendar.CalendarResource;
import de.hh.changeRing.infrastructure.jsfExtension.SecurityFilter;
import de.hh.changeRing.user.UserSession;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.net.URL;

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
@RunAsClient
public abstract class SeleniumTest {
    @Drone
    protected DefaultSelenium browser;
    @ArquillianResource
    protected URL deploymentUrl;

    public static WebArchive warWithBasics() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addClasses(FunctionalTest.ENTITY_CLASSES)
                .addClasses(Context.class, UserSession.class, ThemeSwitcher.class, CalendarResource.class, RestConfig.class)
                .addPackage(SecurityFilter.class.getPackage());
        webArchive.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("src/main/webapp").as(GenericArchive.class),
                "/", Filters.includeAll());
        return webArchive;
    }

    protected void logout() {
        browser.open(deploymentUrl.toString() + "logout/logout.xhtml");
    }

    public void login(String idOrEmail, String password) {
        browser.open(deploymentUrl.toString());
        assertThat(browser.isElementPresent("id=loggedInHeaderForm"), is(false));
        browser.type("id=loginForm-idOrEmail", idOrEmail);
        browser.type("id=loginForm-password", password);
        waitForAllResourceThreads();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                browser.click("id=loginForm-loginButton_button");
            }
        }).inspect(new UserSessionLoggedIn());
        browser.waitForPageToLoad("15000");
        assertThat(browser.isElementPresent("id=loggedInHeaderForm"), is(true));
    }

    protected void waitForAllResourceThreads() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class UserSessionLoggedIn extends Inspection {
        private static final long serialVersionUID = 1L;

        @Inject
        private UserSession userSession;

        @AfterPhase(Phase.INVOKE_APPLICATION)
        public void verifySessionLoggedIn() {
            assertThat(userSession.isLoggedIn(), is(true));
        }
    }
}
