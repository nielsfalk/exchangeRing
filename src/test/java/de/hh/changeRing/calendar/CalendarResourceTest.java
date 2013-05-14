package de.hh.changeRing.calendar;

import de.hh.changeRing.Context;
import de.hh.changeRing.FunctionalTest;
import de.hh.changeRing.selenium.SeleniumTest;
import de.hh.changeRing.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URL;

import static de.hh.changeRing.TestUtils.createTestMember;
import static de.hh.changeRing.calendar.EventType.*;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

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

@RunAsClient
@RunWith(Arquillian.class)
public class CalendarResourceTest extends FunctionalTest {
    private static final User USER = createTestMember();
    private static final Event EVENT1 = createEvent(1, fleaMarket, USER);
    private static final Event EVENT2 = createEvent(2, individual, USER);

    @Deployment
    public static Archive<?> createDeployment() {
        return SeleniumTest.warWithBasics().addClass(DataPump.class);
    }

    @ArquillianResource
    private URL deploymentUrl;


    @Test
    public void allEvents() {
        ClientResponse<String> responseObj = request("/calendar/tauschring.ics", String.class, "text/Calendar");
        String calendarString = responseObj.getEntity();

        assertThat(calendarString, containsString("fleaMarket"));
        assertThat(calendarString, containsString("individual"));
    }

    @Test
    public void excludeFleaMarket() {
        ClientResponse<String> responseObj = request("/calendar/tauschring.ics?exclude=" + fleaMarket.getDatabaseValue()
                + "&exclude=" + info.getDatabaseValue(), String.class, "text/Calendar");
        String calendarString = responseObj.getEntity();

        assertThat(calendarString, not(containsString("fleaMarket")));
        assertThat(calendarString, containsString("individual"));
    }

    private <T> ClientResponse<T> request(String path, Class<T> returnType, String mediaType) {
        ClientRequest request = new ClientRequest(deploymentUrl.toString() + Context.RESOURCE_PREFIX + path);
        request.header("Accept", mediaType);
        try {
            ClientResponse<T> response = request.get(returnType);
            Assert.assertThat(response.getStatus(), is(OK.getStatusCode()));
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void createUser() {
            entityManager.persist(USER);
            for (Event event : new Event[]{EVENT1, EVENT2}) {
                entityManager.persist(event);
            }
        }
    }
}
