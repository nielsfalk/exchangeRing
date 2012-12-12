package de.hh.changeRing.jsfExtension;

import de.hh.changeRing.InitTestData;
import de.hh.changeRing.domain.Advertisement;
import de.hh.changeRing.domain.BaseEntity;
import de.hh.changeRing.user.User;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
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
@SuppressWarnings("UnusedDeclaration")
public abstract class EntityConverter<TYPE extends BaseEntity> implements Converter {
    @FacesConverter("advertisementConverter")
    public static class AdvertisementConverter extends EntityConverter<Advertisement> {
        @Override
        protected List<Advertisement> getAllEntities() {
            return InitTestData.getAdvertisements();
        }
    }

    @FacesConverter("userConverter")
    public static class UserConverterNew extends EntityConverter<User> {
        @Override
        protected List<User> getAllEntities() {
            return InitTestData.getUsers();
        }
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        for (TYPE entity : getAllEntities()) {
            if (entity.getId().toString().equals(s)) {
                return entity;
            }
        }
        return null;
    }


    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof BaseEntity) {
            BaseEntity user = (BaseEntity) o;
            return user.getId().toString();
        }
        return "Bitter wählen";
    }

    protected abstract List<TYPE> getAllEntities();
}
