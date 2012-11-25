package de.hh.changeRing.controller;

import de.hh.changeRing.Context;
import de.hh.changeRing.InitTestData;
import de.hh.changeRing.domain.Advertisement;
import de.hh.changeRing.domain.Category;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hh.changeRing.controller.Ad.TopAdMenuItemType.offers;

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
public class Ad {

    private DefaultMenuModel topAdMenu;
    private TopAdMenuItemType topAdMenuItemType = offers;
    private static Map<TopAdMenuItemType, DefaultMenuModel> navigationCache = new HashMap<TopAdMenuItemType, DefaultMenuModel>();
    private Advertisement selectedAdvertisement;

    public void selectTop(TopAdMenuItemType name) {
        this.topAdMenuItemType = name;
        topAdMenu = null;
    }

    public DefaultMenuModel getTopAdMenu() {
        if (topAdMenu == null) {
            topAdMenu = new DefaultMenuModel();
            MenuItem back = new MenuItem();
            back.setOutcome(Context.WELCOME_PAGE);
            back.setValue("Zurück");
            back.setIcon("ui-icon-home");
            topAdMenu.addMenuItem(back);

            topAdMenu.addSeparator(new Separator());
            for (TopAdMenuItemType itemType : TopAdMenuItemType.values()) {
                MenuItem item = new MenuItem();
                item.addActionListener(new Context().createElActionListener(
                        "#{ad.selectTop('" + itemType.name() + "')}", TopAdMenuItemType.class));
                item.setValue(itemType.translation);
                item.setUpdate("adForm");
                if (itemType.equals(topAdMenuItemType)) {
                    item.setStyleClass("ui-state-active");
                }
                topAdMenu.addMenuItem(item);
            }
        }
        return topAdMenu;
    }

    public DefaultMenuModel getNavigation() {
        if (!navigationCache.containsKey(topAdMenuItemType)) {

            DefaultMenuModel navi = topAdMenuItemType.hasSubMenu ? AdNavigation.createAdNavigation(topAdMenuItemType) : null;
            navigationCache.put(topAdMenuItemType, navi);
        }
        return navigationCache.get(topAdMenuItemType);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void selectOffer(Long id) {
        selectedAdvertisement = InitTestData.findAd(id);
    }

    public void setSelectedAdvertisement(Advertisement selectedAdvertisement) {
        this.selectedAdvertisement = selectedAdvertisement;
    }

    public Advertisement getSelectedAdvertisement() {
        return selectedAdvertisement;
    }


    public static enum TopAdMenuItemType {
        create("Erstellen"),
        offers("Angebote", Advertisement.AdvertisementType.offer, true),
        requests("Gesuche", Advertisement.AdvertisementType.request, true);

        private final boolean hasSubMenu;
        private String translation;
        final Advertisement.AdvertisementType adType;

        TopAdMenuItemType(String translation) {
            this(translation, null, false);
        }

        TopAdMenuItemType(String translation, Advertisement.AdvertisementType adType, boolean hasSubMenu) {
            this.translation = translation;
            this.hasSubMenu = hasSubMenu;
            this.adType = adType;
        }

        public boolean isHasSubMenu() {
            return hasSubMenu;
        }
    }

    public TopAdMenuItemType getTopAdMenuItemType() {
        return topAdMenuItemType;
    }

    public static class AdNavigation {
        static DefaultMenuModel createAdNavigation(Advertisement.AdvertisementType type) {
            return recursiveCreateCategory(Category.rootItems(), null, new DefaultMenuModel(), type);
        }

        private static DefaultMenuModel recursiveCreateCategory(List<Category> categories, Submenu parent, DefaultMenuModel rootNavigation, Advertisement.AdvertisementType offer) {
            for (Category category : categories) {
                Submenu sub = new Submenu();
                sub.setLabel(category.getName());
                if (parent == null) {
                    rootNavigation.addSubmenu(sub);
                } else {
                    parent.getChildren().add(sub);
                }

                if (category.getChildren().isEmpty()) {
                    addItems(category, sub, offer);
                } else {
                    recursiveCreateCategory(category.getChildren(), sub, rootNavigation, offer);
                }
            }
            return rootNavigation;
        }

        private static void addItems(Category category, Submenu sub, Advertisement.AdvertisementType type) {
            for (Advertisement advertisement : InitTestData.getSortedAds().get(category)) {
                if (advertisement.getType().equals(type)) {
                    MenuItem item = new MenuItem();
                    item.addActionListener(new Context().createElActionListener(
                            "#{ad.selectOffer(" + advertisement.getId() + ")}", Long.class));
                    item.setValue(advertisement.getTitle());
                    item.setUpdate("toUpdate");
                    sub.getChildren().add(item);
                }
            }
        }

        public static DefaultMenuModel createAdNavigation(TopAdMenuItemType topAdMenuItemType) {
            return createAdNavigation(topAdMenuItemType.adType);
        }
    }
}
