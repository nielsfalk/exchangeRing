package de.hh.changeRing.jsfExtension;

import de.hh.changeRing.user.User;
import org.apache.commons.lang.StringEscapeUtils;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

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
@FacesComponent(value = "GravatarTag")
public class GravatarTag extends UIComponentBase {
    public GravatarTag() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return null;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (Boolean.FALSE.equals(this.getAttributes().get("rendered"))) {
            return;
        }
        String sizeString = (String) this.getAttributes().get("size");
        int size = sizeString==null?80:Integer.parseInt(sizeString);
        User user = (User) this.getAttributes().get("user");

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("a", this);
        for (String key : new String[]{"style", "styleClass", "imgId"}) {
            Object value = this.getAttributes().get(key);
            if (value != null) {
                if (key.equals("styleClass")) {
                    key = "class";
                }
                if (key.equals("imgId")) {
                    // with getAttributes().get("id") you always get a generated id
                    key = "id";
                }
                writer.writeAttribute(key, value, null);
            }
        }
        boolean noName = getBooleanAttribute("noName", false);
        if (getBooleanAttribute("doLink", true) && ! noName){
            writer.writeAttribute("href",user.getLink(),null);
        }

        writer.startElement("img", this);
        writer.writeAttribute("src", user.getGravatarUrl(size), null);
        if (!noName){
            writer.writeAttribute("alt", "avatar: "+ user.getDisplayName(), null);
        }
        boolean showUserId = getBooleanAttribute("showUserId", false);
        boolean showDisplayName = getBooleanAttribute("showDisplayName", false);
        if (showUserId) {
            writer.write(" "+user.getId());
            if (showDisplayName){
                writer.write(" - ");
            }
        }
        if (showDisplayName){
            writer.write(escapeHtml(user.getDisplayName()));
        }
        writer.endElement("img");
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("a");
    }

    private boolean getBooleanAttribute(String key, boolean defaultValue) {
        String valueStr = (String) this.getAttributes().get(key);
        return valueStr == null ? defaultValue : Boolean.valueOf(valueStr);
    }
}
