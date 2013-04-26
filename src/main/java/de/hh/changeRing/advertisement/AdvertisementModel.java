package de.hh.changeRing.advertisement;


import de.hh.changeRing.Context;
import de.hh.changeRing.user.UserSession;
import org.joda.time.DateTime;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.model.DefaultMenuModel;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import static de.hh.changeRing.Context.ACTIVE_CSS_CLASS;
import static de.hh.changeRing.advertisement.Advertisement.AdvertisementType;
import static java.util.Calendar.YEAR;

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
@Named
@SessionScoped
@Stateful
public class AdvertisementModel implements Serializable {
    public static final String ADVERTISEMENTS_BROWSE_URL = "/internal/advertisements/browse.xhtml";
    public static final String ADVERTISEMENTS_EDIT_URL = "/internal/advertisements/edit.xhtml";
    @Inject
    private UserSession session;

    @PersistenceContext
    private EntityManager entityManager;
    private AdvertisementType type;
    private Category category = Category.root;

    private Advertisement advertisement;
    private Advertisement newAdvertisement;

    public String activeMenuBrowse(AdvertisementType type) {
        Context context = new Context();
        return typeActive(type, context)
                && context.getViewId().startsWith("/internal/advertisements/browse")
                ? ACTIVE_CSS_CLASS : "";
    }

    private boolean typeActive(AdvertisementType type, Context context) {
        return type != null && type.name().equals(context.getRequest().getParameter("type"));
    }

    public DefaultMenuModel getCategoryBrowser() {
        DefaultMenuModel menuModel = new DefaultMenuModel();
        if (category != Category.root) {
            MenuItem menuItem = new MenuItem();
            menuItem.setValue("zurück");
            menuItem.setUrl(category.parent.getBrowseUrl(type));
            menuItem.setIcon("ui-icon-carat-1-n");
            menuModel.addMenuItem(menuItem);
            menuModel.addSeparator(new Separator());
        }
        List<Category> children = category.getChildren();
        for (Category child : children) {
            MenuItem menuItem = new MenuItem();
            menuItem.setValue(child.getName());
            menuItem.setUrl(child.getBrowseUrl(type));
            menuModel.addMenuItem(menuItem);
        }
        if (children.isEmpty()) {
            for (Advertisement advertisement : Advertisement.findAdvertisement(type, category, entityManager)) {
                MenuItem menuItem = new MenuItem();
                menuItem.setValue(advertisement.getTitle());
                menuItem.setUrl(advertisement.getBrowseUrl());
                if (advertisement.equals(getAdvertisement())) {
                    menuItem.setStyleClass(ACTIVE_CSS_CLASS);
                }
                menuModel.addMenuItem(menuItem);
            }
        }
        return menuModel;
    }

    public String save() {
        if (newAdvertisement != null) {
            entityManager.persist(newAdvertisement);
            new Context().addMessage("Anzeige erstellt");
            String browseUrl = newAdvertisement.getBrowseUrl();
            newAdvertisement = null;
            return browseUrl;
        }
        if (advertisement != null) {
            return advertisement.getBrowseUrl();
        }
        return null;
    }

    public List<Advertisement> getAdvertisements() {
        if (getAdvertisement() != null) {
            return Arrays.asList(getAdvertisement());
        }
        return Advertisement.findAdvertisement(type, category, entityManager);
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public Advertisement getEditAdvertisement() {
        if (advertisement != null) {
            if (advertisement.getOwner().equals(session.getUser())) {
                return advertisement;
            }
        }
        return null;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
        newAdvertisement = null;
    }

    public AdvertisementType getType() {
        return type;
    }

    public void setType(AdvertisementType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        setAdvertisement(null);
        this.category = category;
    }

    public Advertisement getNewAdvertisement() {
        if (newAdvertisement == null) {
            newAdvertisement = new Advertisement();
            newAdvertisement.setOwner(session.getUser());
            newAdvertisement.setValidUntil(new DateTime().plusYears(1));
        }
        return newAdvertisement;
    }

    public void setNewAdvertisement(Advertisement newAdvertisement) {
        this.newAdvertisement = newAdvertisement;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSession(UserSession session) {
        this.session = session;
    }

    public Advertisement.AdvertisementType[] getAdType() {
        return Advertisement.AdvertisementType.values();
    }

    public List<Category> getCategories() {
        return Category.endPointItems();
    }
}
