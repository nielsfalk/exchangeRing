package de.hh.changeRing;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpServletRequest;


/**
 * User: nielsfalk Date: 14.11.12 14:10
 */
public class ServletHelper {
	private static final Logger LOGGER = Logger.getLogger(ServletHelper.class.getName());

	static final String WELCOME_PAGE = "/dashboard.xhtml";
	public static final String INTERNAL_PREFIXE = "/internal";
	private ExternalContext externalContext;
	private String url;
	private String requestURI;
	private HttpServletRequest request;

	public ServletHelper(ExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	public ServletHelper(PhaseEvent phaseEvent) {
		this(phaseEvent.getFacesContext().getExternalContext());
	}

	void secureInternalArea() {
		if (internalRequest()) {
			if (notLoggedIn()) {
				redirect(WELCOME_PAGE);
			}
		}
	}

	void onlyHttps() {
		if (getUrl().startsWith("http://")) {
			if (!getUrl().startsWith("http://localhost")) {
				redirectHttpsRoot(getUrl());
			} else {
				LOGGER.info("was localhost");
			}
		}
	}

	private void redirectHttpsRoot(String url) {
		LOGGER.info("redirect to https");
		String httpsUrl = url.substring(7);
		int firstSlash = httpsUrl.indexOf('/');
		if (firstSlash != -1) {
			httpsUrl = httpsUrl.substring(0, firstSlash);
		}
		httpsUrl = "https://" + httpsUrl;
		redirect(httpsUrl);
	}

	private void redirect(String urlOrUri) {
		try {
			externalContext.redirect(urlOrUri);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean notLoggedIn() {
		UserSession userSession = getSessionBean(UserSession.class);
		return userSession == null || !userSession.isLoggedIn();
	}

	private <T>T getSessionBean(Class<T> type) {
		String typeSimpleName = type.getSimpleName();
		String beanName = Character.toLowerCase(typeSimpleName.charAt(0)) + typeSimpleName.substring(1);
		return (T) externalContext.getSessionMap().get(beanName);
	}

	private boolean internalRequest() {
		return getRequestedURI().startsWith(INTERNAL_PREFIXE);
	}

	private String getUrl() {
		if (url == null) {
			url = getRequest().getRequestURL().toString();
		}
		return url;
	}

	private String getRequestedURI() {
		if (requestURI == null) {
			requestURI = getRequest().getRequestURI();
		}
		return requestURI;
	}

	private HttpServletRequest getRequest() {
		if (request == null) {
			request = (HttpServletRequest) externalContext.getRequest();
		}
		return request;
	}
}
