package de.hh.changeRing;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

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
public class ThemeSwitcher {

    private String theme = "start";
	private Map<String, String> themes;


	public Map<String, String> getThemes() {
		return themes;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@PostConstruct
	public void init() {

		themes = new TreeMap<String, String>();

		themes.put("afterdark", "afterdark");
		themes.put("afternoon", "afternoon");
		themes.put("afterwork", "afterwork");
		themes.put("aristo", "aristo");
		themes.put("black-tie", "black-tie");
		themes.put("blitzer", "blitzer");
		themes.put("bluesky", "bluesky");
		themes.put("casablanca", "casablanca");
		themes.put("cruze", "cruze");
		themes.put("cupertino", "cupertino");
		themes.put("dark-hive", "dark-hive");
		themes.put("dot-luv", "dot-luv");
		themes.put("eggplant", "eggplant");
		themes.put("excite-bike", "excite-bike");
		themes.put("flick", "flick");
		themes.put("glass-x", "glass-x");
		themes.put("home", "home");
		themes.put("hot-sneaks", "hot-sneaks");
		themes.put("humanity", "humanity");
		themes.put("le-frog", "le-frog");
		themes.put("midnight", "midnight");
		themes.put("mint-choc", "mint-choc");
		themes.put("overcast", "overcast");
		themes.put("pepper-grinder", "pepper-grinder");
		themes.put("redmond", "redmond");
		themes.put("rocket", "rocket");
		themes.put("sam", "sam");
		themes.put("smoothness", "smoothness");
		themes.put("south-street", "south-street");
		themes.put("start", "start");
		themes.put("sunny", "sunny");
		themes.put("swanky-purse", "swanky-purse");
		themes.put("trontastic", "trontastic");
		themes.put("ui-darkness", "ui-darkness");
		themes.put("ui-lightness", "ui-lightness");
		themes.put("vader", "vader");
	}

	public void save(){
	}

}
