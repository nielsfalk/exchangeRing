package de.hh.changeRing;

import de.hh.changeRing.domain.Advertisement;
import de.hh.changeRing.domain.Category;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.util.List;


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
@ApplicationScoped
public class AdNavigation {

    private DefaultMenuModel addNavigation;


    public DefaultMenuModel getAddNavigation() {
        if (addNavigation == null) {
            addNavigation = new DefaultMenuModel();
            MenuItem back = new MenuItem();
            back.setOutcome(Context.WELCOME_PAGE);
            back.setValue("Zurück");
            back.setIcon("ui-icon-home");

            addNavigation.addMenuItem(back);
            addNavigation.addSeparator(new Separator());

            recusiveCreateCategory(Category.rootItems(), null);
        }
        return addNavigation;
    }

    private void recusiveCreateCategory(List<Category> categories, Submenu parent) {
        for (Category category : categories) {
            Submenu sub = new Submenu();
            sub.setLabel(category.getName());
            if (parent == null) {
                addNavigation.addSubmenu(sub);
            } else {
                parent.getChildren().add(sub);
            }

            if (category.getChildren().isEmpty()) {
                addItems(category, sub);
            } else {
                recusiveCreateCategory(category.getChildren(), sub);
            }
            //add2(category.getChildren(), sub.getChildren());
        }
    }

    private void addItems(Category category, Submenu sub) {
        for (Advertisement advertisement : InitTestData.getSortedAds().get(category)) {
            MenuItem item = new MenuItem();
            item.addActionListener(new Context().createActionListener(
                    "#{userSession.selectOffer(" + advertisement.getId() + ")}",
                    ActionEvent.class, Long.class));
            item.setValue(advertisement.getTitle());
            sub.getChildren().add(item);
        }
    }

    public static class adActionListener implements ActionListener {

        private Advertisement advertisement;

        public adActionListener(Advertisement advertisement) {
            this.advertisement = advertisement;
        }

        @Override
        public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
            System.out.println("yeah" + advertisement.getContent());
        }
    }

}
