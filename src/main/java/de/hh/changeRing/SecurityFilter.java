package de.hh.changeRing;

import static javax.faces.event.PhaseId.RESTORE_VIEW;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;


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
