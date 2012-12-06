package de.hh.changeRing.controller;


import de.hh.changeRing.Context;
import de.hh.changeRing.domain.Category;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

import static de.hh.changeRing.controller.UserSession.ACTIVE_CSS_CLASS;
import static de.hh.changeRing.domain.Advertisement.AdvertisementType;

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
@ManagedBean
@SessionScoped
public class Advertisement {
    public static final String ADVERTISEMENT_NAVIGATION_APP_KEY = "advertisementNavigation";
    AdvertisementType type;
    Category category;

    public String activeMenuBrowse(AdvertisementType type) {
        Context context = new Context();
        return typeActive(type, context)
                && context.getViewId().startsWith("/internal/advertisements/browse")
                ? ACTIVE_CSS_CLASS : "";
    }

    private boolean typeActive(AdvertisementType type, Context context) {
        return type != null && type.name().equals(context.getRequest().getParameter("type"));
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
        this.category = category;
    }

    public DefaultMenuModel getCategoryBrowser() {
        DefaultMenuModel menuModel = new DefaultMenuModel();
        //todo zurück
        List<Category> children = category == null ? Category.rootItems() : category.getChildren();
        for (Category child : children) {
            MenuItem menuItem = new MenuItem();
            menuItem.setValue(child.getName());
            menuItem.setUrl("/internal/advertisements/browse.xhtml?type=" + type.name() + "&category=" + child.name());
            menuModel.addMenuItem(menuItem);
        }
        if (children.isEmpty()) {
            //TODO ads
        }

        return menuModel;
    }

}
