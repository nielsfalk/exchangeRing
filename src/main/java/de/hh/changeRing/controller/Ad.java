package de.hh.changeRing.controller;

import de.hh.changeRing.Context;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.model.DefaultMenuModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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

    public static enum TopAdMenuItemType {
        create("Erstellen"),
        offers("Angebote"),
        requests("Gesuche");
        private String translation;

        TopAdMenuItemType(String translation) {
            this.translation = translation;
        }
    }
}