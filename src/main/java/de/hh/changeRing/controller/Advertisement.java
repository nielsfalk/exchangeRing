package de.hh.changeRing.controller;


import de.hh.changeRing.Context;
import de.hh.changeRing.InitTestData;
import de.hh.changeRing.domain.Category;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.model.DefaultMenuModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Arrays;
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
    public static final String ADVERTISEMENTS_BROWSE_URL = "/internal/advertisements/browse.xhtml";
    private AdvertisementType type;
    private Category category = Category.root;

    private de.hh.changeRing.domain.Advertisement advertisement;

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
            menuItem.setUrl(browseUrl(category.parent, type));
            menuItem.setIcon("ui-icon-carat-1-n");
            menuModel.addMenuItem(menuItem);
            menuModel.addSeparator(new Separator());
        }
        List<Category> children = category.getChildren();
        for (Category child : children) {
            MenuItem menuItem = new MenuItem();
            menuItem.setValue(child.getName());
            menuItem.setUrl(browseUrl(child, type));
            menuModel.addMenuItem(menuItem);
        }
        if (children.isEmpty()) {
            for (de.hh.changeRing.domain.Advertisement advertisement : InitTestData.getSortedAds().get(category)) {
                MenuItem menuItem = new MenuItem();
                menuItem.setValue(advertisement.getTitle());
                menuItem.setUrl(browseUrl(advertisement));
                if (advertisement.equals(getAdvertisement())) {
                    menuItem.setStyleClass(ACTIVE_CSS_CLASS);
                }
                menuModel.addMenuItem(menuItem);
            }
        }

        return menuModel;
    }

    public static String browseUrl(de.hh.changeRing.domain.Advertisement advertisement) {
        return ADVERTISEMENTS_BROWSE_URL + "?type=" + advertisement.getType().name() + "&advertisement=" + advertisement
                .getId();
    }

    public static String browseUrl(Category category, AdvertisementType type) {
        return ADVERTISEMENTS_BROWSE_URL + "?type=" + type.name() + "&category=" + category.name();
    }


    public DefaultMenuModel getBreadCrumb() {
        return advertisement == null ? category.createBreadCrumb(type) : advertisement.createBreadCrumb();
    }

    public List<de.hh.changeRing.domain.Advertisement> getAdvertisements() {
        if (getAdvertisement() != null) {
            return Arrays.asList(getAdvertisement());
        }
        return InitTestData.getSortedAds().get(category);
    }

    public de.hh.changeRing.domain.Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(de.hh.changeRing.domain.Advertisement advertisement) {
        this.advertisement = advertisement;
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
}
