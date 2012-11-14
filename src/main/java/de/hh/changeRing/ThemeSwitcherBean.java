package de.hh.changeRing;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;


@ManagedBean(name = "themeSwitcherBean")
@SessionScoped
public class ThemeSwitcherBean {

	private Map<String, String> themes;

	private String theme = "le-frog";

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

}
