package de.hh.changeRing.domain;

import de.hh.changeRing.Context;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;

import java.util.ArrayList;
import java.util.List;

import static de.hh.changeRing.controller.Advertisements.ADVERTISEMENTS_BROWSE_URL;
import static de.hh.changeRing.domain.Advertisement.AdvertisementType;

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
public enum Category {
    root(null, null),

    service(root, "Service für Daheim"), home(service, "Haus und Wohnung"), move(service, "Umzug"), garden(service,
            "Garten"), procuration(service, "Urlaubsvertretungen"), handcraft(service, "Handwerk"), cleaning(service,
            "Putzen"), washing(service, "Waschen & Bügeln"),

    engineering(root, "Technik"), office(engineering, "Büro"), layout(engineering, "Layout"), computer(engineering,
            "Computer"), internet(engineering, "Internet"), phone(engineering, "DSL & Telefon"), tv(engineering,
            "Foto / Hifi / TV"),

    creative(root, "Kreative Ecken"), artCraft(creative, "Kunsthandwerk"), tailor(creative, "Kleidung & Schneidern"), design(
            creative, "Design"), advertising(creative, "Werbung"), otherCreative(creative, "Sonstiges"),

    food(root, "Essen und Trinken"), cooking(food, "Kochen"), baking(food, "Backen"), sweet(food,
            "Marmelade, Süßes, Nachspeisen"), party(food, "Partyservice"),

    bodySoul(root, "Körper & Seele"), esoteric(bodySoul, "esoterisch / spirituell"), astrology(bodySoul, "Astrologie"), therapy(
            bodySoul, "Therapien"), massage(bodySoul, "Massagen"), health(bodySoul, "Gesundheit"), beauty(bodySoul,
            "Schönheit & Kosmetik"),

    fitForever(root, "Fit Forever"), sports(fitForever, "Spiel & Sport"), dance(fitForever, "Tanzen"),

    cultureEducation(root, "Kultur & Bildung"), music(cultureEducation, "Musik"), language(cultureEducation, "Sprachen"), culture(
            cultureEducation, "Kultur"),

    smallOnes(root, "Die lieben Kleinen"), babysitting(smallOnes, "Babysitting"), coaching(smallOnes,
            "Förderung & Nachhilfe"),

    animal(root, "Tierisches"), dog(animal, "Hunde"), cat(animal, "Katzen"), animalMisc(animal, "Andere"),

    mobile(root, "Mobiles"), bike(mobile, "Fahrrad"), car(mobile, "Auto"),

    sozial(root, "Soziales & Lebendiges"), help(sozial, "Rat & Hilfe"), enterprise(sozial, "Unternehmungen"), mottenFree(
            sozial, "Mottenfreie Zone"),

    substantive(root, "Materielles"), fleaMarket(substantive, "Flohmarkt"), rental(substantive, "Verleih"), free(
            substantive, "für lau"),

    misc(root, "Sonstiges");

    private final String name;
    public final Category parent;
    private final List<Category> children = new ArrayList<Category>();
    private static List<Category> endPoints;
    public final ArrayList<Category> thisWithParents;

    Category(Category parent, String name) {
        this.parent = parent;
        this.name = name;
        if (parent != null) {
            parent.children.add(this);
        }
        thisWithParents = createThisWithParents();
    }

    private ArrayList<Category> createThisWithParents() {
        ArrayList<Category> result = new ArrayList<Category>();
        Category thisOrParent = this;
        do {
            result.add(thisOrParent);
            thisOrParent = thisOrParent.parent;
        } while (thisOrParent != null);
        return result;
    }

    public static List<Category> rootItems() {
        return root.getChildren();
    }

    public static List<Category> endPointItems() {
        if (endPoints == null) {
            endPoints = new ArrayList<Category>();
            for (Category category : values()) {
                if (category.getChildren().isEmpty()) {
                    endPoints.add(category);
                }
            }
        }
        return endPoints;
    }

    public String getName() {
        return name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public String getDesc() {
        String desc = name;
        if (parent != root) {
            desc = parent.getDesc() + '-' + desc;
        }
        return desc;
    }

    public DefaultMenuModel createBreadCrumb(AdvertisementType type) {
        DefaultMenuModel result = new DefaultMenuModel();
        MenuItem root = new MenuItem();
        root.setValue("home");
        root.setUrl(Context.WELCOME_PAGE);
        result.addMenuItem(root);
        MenuItem typeItem = new MenuItem();
        typeItem.setValue(type.plural);
        typeItem.setUrl(this.root.getBrowseUrl(type));
        result.addMenuItem(typeItem);

        Category[] thisWithParents = this.thisWithParents.toArray(new Category[this.thisWithParents.size()]);
        for (int i = thisWithParents.length - 2; i >= 0; i--) {
            Category category = thisWithParents[i];
            MenuItem menuItem = new MenuItem();
            menuItem.setValue(category.getName());
            menuItem.setUrl(category.getBrowseUrl(type));
            result.addMenuItem(menuItem);
        }
        return result;
    }


    public String getBrowseUrl(AdvertisementType type) {
        return ADVERTISEMENTS_BROWSE_URL + "?type=" + type.name() + "&category=" + name();
    }
}
