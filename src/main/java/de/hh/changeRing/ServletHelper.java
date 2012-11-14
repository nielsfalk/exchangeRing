package de.hh.changeRing;

import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;


/**
 * ----------------GNU General Public License--------------------------------
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * ----------------in addition-----------------------------------------------
 * <p/>
 * In addition, each military use, and the use for interest profit will be
 * excluded.
 * Environmental damage caused by the use must be kept as small as possible.
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

    public <T> T getSessionBean(Class<T> type) {
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
