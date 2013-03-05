package de.hh.changeRing.user;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import de.hh.changeRing.Context;
import de.hh.changeRing.InitTestData;
import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.calendar.Event;

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
@Named
@SessionScoped
public class UserSession implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(UserSession.class.getName());
	public static final String ACTIVE_CSS_CLASS = "ui-state-active";

	private String id, password;
	protected User user;

	public void login() {
		User user = InitTestData.findUser(id);
		if (user != null && user.getPassword().equals(password)) {
			this.user = user;
			new Context().leavePublicEvents();
		}
		LOGGER.info(isLoggedIn() ? id + " logged in" : "tried to login " + id);
		id = null;
		password = null;
		//noinspection ConstantConditions
		message(isLoggedIn() ? ("Wilkommen " + user.getDisplayName() + "!") : "Id oder Passwort falsch");
		//return isLoggedIn()?"/internal/events/browse.xhtml":"";
	}

	protected void message(String message) {
		new Context().addMessage(message);
	}

	public List<User> getNewestMembers(int count) {
		return InitTestData.getNewestMembers(count);
	}

	public List<Event> getNextEventsInternal(int count) {
		return InitTestData.getNextEventsInternal(count);
	}

	public List<Event> getNextEventsPublic(int count) {
		return InitTestData.getNextEventsPublic(count);
	}

	public void updateUser() {}

	@Named
	@Produces
	public boolean isNotLoggedIn() {
		return user == null;
	}

	@Named
	@Produces
	public boolean isLoggedIn() {
		return user != null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	void setUser(User user) {
		this.user = user;
	}

	public List<Event> getNext3EventsInternal() {
		return getNextEventsInternal(3);
	}

	public List<Event> getNext3EventsPublic() {
		return getNextEventsPublic(3);
	}

	public List<User> getNewestMembers8() {
		return getNewestMembers(8);
	}

	public List<Advertisement> getNewestRequests() {
		return InitTestData.getNewestAdvertisements(3, Advertisement.AdvertisementType.request);
	}

	public List<Advertisement> getNewestOffers() {
		return InitTestData.getNewestAdvertisements(3, Advertisement.AdvertisementType.offer);
	}
}
