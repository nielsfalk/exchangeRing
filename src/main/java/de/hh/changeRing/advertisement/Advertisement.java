package de.hh.changeRing.advertisement;

import de.hh.changeRing.BaseEntity;
import de.hh.changeRing.user.User;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;

import javax.persistence.*;
import java.util.Date;

import static de.hh.changeRing.Context.formatGermanDate;
import static de.hh.changeRing.advertisement.AdvertisementModel.ADVERTISEMENTS_BROWSE_URL;
import static de.hh.changeRing.advertisement.AdvertisementModel.ADVERTISEMENTS_EDIT_URL;
import static de.hh.changeRing.advertisement.Category.root;
import static javax.persistence.TemporalType.DATE;

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
@Entity
public class Advertisement extends BaseEntity {

    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    private AdvertisementType type;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Temporal(DATE)
    private Date validUntil;

    private String title;

    @Column(length = 512)
    private String content;
    private String location;
    private String name;
    private boolean linkLocation;

    @Temporal(DATE)
    private Date creationDate = new Date();

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

    public String getEditUrl() {
        return ADVERTISEMENTS_EDIT_URL + "?type=" + getType().name() + "&advertisement=" + getId();
    }

    public String getBrowseCategoryUrl() {
        return getCategory().getBrowseUrl(getType());
    }

    public String getBrowseTypeUrl() {
        return root.getBrowseUrl(getType());
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
