package de.hh.changeRing;

import de.hh.changeRing.controller.UserSession;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
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
public class Context {
    private static final Logger LOGGER = Logger.getLogger(Context.class.getName());

    public static final String WELCOME_PAGE = "/dashboard.xhtml";
    private static final String INTERNAL_PREFIX = "/internal";
    private static final String LOGOUT_PREFIX = "/logout";
    private static final String GERMAN_DATE = "dd.MM.yyyy";
    private String url;
    private String requestURI;
    private HttpServletRequest request;
    private final FacesContext context;
    private ExternalContext externalContext;
    private UIViewRoot viewRoot;
    private HttpSession session;
    private ELContext elContext;
    private ExpressionFactory expressionFactory;
    private Application app;
    private String viewId;
    private javax.el.ELResolver elResolver;

    private Context(FacesContext context) {
        this.context = context;
    }

    public Context(PhaseEvent phaseEvent) {
        this(phaseEvent.getFacesContext());
    }

    public Context() {
        this(FacesContext.getCurrentInstance());
    }

    public static String formatGermanDate(Date date) {
        return date == null ? "" : new SimpleDateFormat(GERMAN_DATE).format(date);
    }

    public void secureInternalArea() {
        if (internalRequest()) {
            if (notLoggedIn()) {
                redirect(WELCOME_PAGE);
            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
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
            getExternalContext().redirect(urlOrUri);
            context.responseComplete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean notLoggedIn() {
        UserSession userSession = getNamedBean(UserSession.class);
        return userSession == null || userSession.isNotLoggedIn();
    }

    <T> T getNamedBean(Class<T> type) {
        String typeSimpleName = type.getSimpleName();
        String beanName = Character.toLowerCase(typeSimpleName.charAt(0)) + typeSimpleName.substring(1);
        return (T) getElResolver().getValue(getElContext(), null, beanName);
    }

    private boolean internalRequest() {
        return getRequestedURI().startsWith(INTERNAL_PREFIX);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void logUrl() {
        LOGGER.info(getUrl());
    }

    public MethodExpressionActionListener createElActionListener(String elExpression,
                                                                 Class<?>... paramTypes) {
        List<Class<?>> paramTypesList = new ArrayList<Class<?>>();
        paramTypesList.add(ActionEvent.class);
        Collections.addAll(paramTypesList, paramTypes);
        return new MethodExpressionActionListener(
                getExpressionFactory().createMethodExpression(
                        getElContext(),
                        elExpression,
                        Void.class,
                        paramTypesList.toArray(new Class<?>[paramTypesList.size()])));
    }

    public void handleLogout() {
        if (getRequestedURI().startsWith(LOGOUT_PREFIX)) {
            transientSessionInvalidation();
        }
    }

    private void transientSessionInvalidation() {
        if (getSession() != null) {
            LOGGER.info("transient logout " + getSession().getId());
            getSession().invalidate();
        }
        getViewRoot().setTransient(true);
    }

    private ExpressionFactory getExpressionFactory() {
        if (expressionFactory == null) {
            expressionFactory = getApplication().getExpressionFactory();
        }
        return expressionFactory;
    }

    private Application getApplication() {
        if (app == null) {
            app = context.getApplication();
        }
        return app;
    }

    private ELContext getElContext() {
        if (elContext == null) {
            elContext = context.getELContext();
        }
        return elContext;
    }


    private UIViewRoot getViewRoot() {
        if (viewRoot == null) {
            viewRoot = context.getViewRoot();
        }
        return viewRoot;
    }

    public String getViewId() {
        if (viewId == null) {
            viewId = getViewRoot().getViewId();
        }
        return viewId;
    }

    private HttpSession getSession() {
        if (session == null) {
            session = (HttpSession) getExternalContext().getSession(false);
        }
        return session;
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

    public HttpServletRequest getRequest() {
        if (request == null) {
            request = (HttpServletRequest) getExternalContext().getRequest();
        }
        return request;
    }

    ExternalContext getExternalContext() {
        if (externalContext == null) {
            externalContext = context.getExternalContext();
        }
        return externalContext;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        context.addMessage(null, message);
    }

    public ELResolver getElResolver() {
        if (elResolver == null) {
            elResolver = getElContext().getELResolver();
        }
        return elResolver;
    }
}
