package de.hh.changeRing.domain;

import java.util.ArrayList;
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
public enum Category {
    // <p:slideMenu style="width:180px">
    service("Service für Daheim"),
    home(service, "Haus und Wohnung"),
    move(service, "Umzug"),
    garden(service, "Garten"),
    procuration(service, "Urlaubsvertretungen"),
    handcraft(service, "Handwerk"),
    cleaning(service, "Putzen"),
    washing(service, "Waschen & Bügeln"),

    engineering("Technik"),
    office(engineering, "Büro"),
    layout(engineering, "Layout"),
    computer(engineering, "Computer"),
    internet(engineering, "Internet"),
    phone(engineering, "DSL & Telefon"),
    tv(engineering, "Foto / Hifi / TV"),

    creative("Kreative Ecken"),
    artCraft(creative, "Kunsthandwerk"),
    tailor(creative, "Kleidung & Schneidern"),
    design(creative, "Design"),
    advertising(creative, "Werbung"),
    otherCreative(creative, "Sonstiges"),

    food("Essen und Trinken"),
    cooking(food, "Kochen"),
    baking(food, "Backen"),
    sweet(food, "Marmelade, Süßes, Nachspeisen"),
    party(food, "Partyservice"),

    bodySoul("Körper & Seele"),
    esoteric(bodySoul, "esoterisch / spirituell"),
    astrology(bodySoul, "Astrologie"),
    therapy(bodySoul, "Therapien"),
    massage(bodySoul, "Massagen"),
    health(bodySoul, "Gesundheit"),
    beauty(bodySoul, "Schönheit & Kosmetik"),

	misc("Sonstiges");

    private String name;
    private Category parent;
    private List<Category> children = new ArrayList<Category>();

    Category(Category parent, String name) {
        this(name);
        this.parent = parent;
        parent.children.add(this);
    }

    Category(String name) {
        this.name = name;
    }


    public static List<Category> rootItems() {
        List<Category> result = new ArrayList<Category>();
        for (Category potentialRoot : values()) {
            if (potentialRoot.parent == null) {
                result.add(potentialRoot);
            }
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public List<Category> getChildren() {
        return children;
    }
}
