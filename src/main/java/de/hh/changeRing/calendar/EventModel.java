package de.hh.changeRing.calendar;

import static de.hh.changeRing.calendar.EventModel.TimeFilter.future;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

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
public class EventModel implements Serializable {
	private TimeFilter timeFilter = future;

	public String selectedStyleClass(TimeFilter timeFilter) {
		return this.timeFilter.equals(timeFilter) ? "ui-state-active" : "";
	}

	public void activateTimeFilter(TimeFilter timeFilter) {
		this.timeFilter = timeFilter;
	}

	public static enum TimeFilter {
		future("Kommende"), past("Vergangene"), all("Alle");
		private String translation;

		TimeFilter(String translation) {
			this.translation = translation;
		}

		public String getTranslation() {
			return translation;
		}
	}
}
