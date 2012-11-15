package de.hh.changeRing.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------GNU General Public License--------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ----------------in addition-----------------------------------------------
 *
 * In addition, each military use, and the use for interest profit will be
 * excluded.
 * Environmental damage caused by the use must be kept as small as possible.
 */
public enum Categorie {
	// <p:slideMenu style="width:180px">
	service("Service für Daheim"),
	home(service, "Haus und Wohnung"),
	move(service, "Umzug"),
	garden(service, "Garten"),
	procuration (service, "Urlaubsvertretungen"),
	handcraft(service, "Handwerk"),
	cleaning(service, "Putzen"),
	washing(service, "Waschen & Bügeln"),

	engineering("Technik"),
	office(engineering,"Büro"),
	layout(engineering,"Layout"),
	computer(engineering,"Computer"),
	internet(engineering,"Internet"),
	phone(engineering,"DSL & Telefon"),
	tv(engineering,"Foto / Hifi / TV"),

	creative("Kreative Ecken"),
	artCraft(creative,"Kunsthandwerk"),
	tailor(creative,"Kleidung & Schneidern"),
	design(creative,"Design"),
	advertising(creative,"Werbung"),
	otherCreative(creative,"Sonstiges"),

	food("Essen und Trinken"),
	cooking(food,"Kochen"),
	baking(food,"Backen"),
	sweet(food,"Marmelade, Süßes, Nachspeisen"),
	party(food,"Partyservice"),

	bodySoul("Körper & Seele"),
	esoteric(bodySoul,"esoterisch / spirituell"),
	astrology(bodySoul,"Astrologie"),
	therapy(bodySoul,"Therapien"),
	massage(bodySoul,"Massagen"),
	health(bodySoul,"Gesundheit"),
	beauty(bodySoul,"Schönheit & Kosmetik");

	private String name;
	private Categorie parent;
	private List<Categorie> children = new ArrayList<Categorie>();

	Categorie(Categorie parent, String name) {
		this(name);
		this.parent = parent;
		parent.children.add(this);
	}

	Categorie(String name) {
		this.name = name;
	}
}
