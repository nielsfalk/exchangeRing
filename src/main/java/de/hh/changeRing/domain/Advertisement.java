package de.hh.changeRing.domain;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;

import java.util.Date;

import static de.hh.changeRing.Context.formatGermanDate;
import static de.hh.changeRing.controller.Advertisement.ADVERTISEMENTS_BROWSE_URL;

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
public class Advertisement extends BaseEntity {

    private static long idCounter = 0;
    private User owner;
    private AdvertisementType type;
    private Category category;
    private Date validUntil;

    private String title;
    private String content;
    private String location;
    private String name;
    private boolean linkLocation;
    private Date creationDate = new Date();

    public Advertisement() {
        id = idCounter++;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public DefaultMenuModel getBreadCrumb() {
        DefaultMenuModel breadCrumb = category.createBreadCrumb(type);
        MenuItem menuItem = new MenuItem();
        menuItem.setValue(title);
        menuItem.setUrl(getBrowseUrl());
        breadCrumb.addMenuItem(menuItem);
        return breadCrumb;
    }

    public String getFormattedValidUntil() {
        return formatGermanDate(getValidUntil());
    }

    public String getFormattedCreationDate() {
        return formatGermanDate(getCreationDate());
    }

    public String getBrowseUrl() {
        return ADVERTISEMENTS_BROWSE_URL + "?type=" + getType().name() + "&advertisement=" + getId();
    }

    public static enum AdvertisementType {
        offer("Angebot", "Angobote"), request("Gesuch", "Gesuche");
        private String translation;
        public final String plural;

        AdvertisementType(String translation, String plural) {
            this.translation = translation;
            this.plural = plural;
        }

        public String getTranslation() {
            return translation;
        }
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
        if (name == null) {
            name = owner.getDisplayName();
        }
    }

    public AdvertisementType getType() {
        return type;
    }

    public void setType(AdvertisementType type) {
        this.type = type;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLinkLocation(boolean linkLocation) {
        this.linkLocation = linkLocation;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
