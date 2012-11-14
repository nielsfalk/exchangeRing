package de.hh.changeRing;

import static javax.faces.event.PhaseId.RESTORE_VIEW;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * ----------------GNU General Public License--------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ----------------in addition-----------------------------------------------
 *
 * In addition, each military use, and the use for interest profit will be
 * excluded.
 * Environmental damage caused by the use must be kept as small as possible.
 */
public class SecurityFilter implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent phaseEvent) {
		new ServletHelper(phaseEvent).secureInternalArea();
	}

	@Override
	public void beforePhase(PhaseEvent phaseEvent) {
		new ServletHelper(phaseEvent).onlyHttps();
	}

	@Override
	public PhaseId getPhaseId() {
		return RESTORE_VIEW;
	}

}
