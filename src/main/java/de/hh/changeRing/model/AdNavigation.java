package de.hh.changeRing.model;

import de.hh.changeRing.Context;
import de.hh.changeRing.InitTestData;
import de.hh.changeRing.domain.Advertisement;
import de.hh.changeRing.domain.Category;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.util.List;

import static de.hh.changeRing.domain.Advertisement.AdvertisementType.offer;
import static de.hh.changeRing.domain.Advertisement.AdvertisementType.request;

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
@ManagedBean
@ApplicationScoped
public class AdNavigation {

    private DefaultMenuModel offerNavigation;
    private DefaultMenuModel requestNavigation;

    public DefaultMenuModel getOfferNavigation() {
        if (offerNavigation == null) {
            offerNavigation = createAdNavigation(offer);
        }
        return offerNavigation;
    }

    public DefaultMenuModel getRequestNavigation() {
        if (requestNavigation == null) {
            requestNavigation = createAdNavigation(request);
        }
        return requestNavigation;
    }

    private DefaultMenuModel createAdNavigation(Advertisement.AdvertisementType type) {
        DefaultMenuModel navigation = new DefaultMenuModel();
        MenuItem back = new MenuItem();
        back.setOutcome(Context.WELCOME_PAGE);
        back.setValue("Zurück");
        back.setIcon("ui-icon-home");

        navigation.addMenuItem(back);
        navigation.addSeparator(new Separator());

        recursiveCreateCategory(Category.rootItems(), null, navigation, type);
        return navigation;
    }

    private void recursiveCreateCategory(List<Category> categories, Submenu parent, DefaultMenuModel rootNavigation, Advertisement.AdvertisementType offer) {
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
    }

    private void addItems(Category category, Submenu sub, Advertisement.AdvertisementType type) {
        for (Advertisement advertisement : InitTestData.getSortedAds().get(category)) {
            if (advertisement.getType().equals(type)) {
                MenuItem item = new MenuItem();
                item.addActionListener(new Context().createActionListener(
                        "#{userSession.selectOffer(" + advertisement.getId() + ")}", ActionEvent.class, Long.class));
                item.setValue(advertisement.getTitle());
                item.setUpdate("toUpdate");
                sub.getChildren().add(item);
            }
        }
    }
}
