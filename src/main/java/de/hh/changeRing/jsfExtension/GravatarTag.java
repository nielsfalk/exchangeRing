package de.hh.changeRing.jsfExtension;

import de.hh.changeRing.Context;
import de.hh.changeRing.user.User;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

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
    public void encodeEnd(FacesContext context) throws IOException {
        if (Boolean.FALSE.equals(this.getAttributes().get("rendered"))) {
            return;
        }
        String doLinkStr = (String) this.getAttributes().get("doLink");
        String noNameStr = (String) this.getAttributes().get("noName");
        String sizeString = (String) this.getAttributes().get("size");
        int size = sizeString==null?80:Integer.parseInt(sizeString);
        User user = (User) this.getAttributes().get("user");
        boolean doLink = doLinkStr == null || Boolean.valueOf(doLinkStr);
        boolean noName = noNameStr != null && Boolean.valueOf(noNameStr);

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
        if (doLink && ! noName){
            writer.writeAttribute("href",user.getLink(),null);
        }

        writer.startElement("img", this);
        writer.writeAttribute("src", user.getGravatarUrl(size), null);
        if (!noName){
            writer.writeAttribute("alt", user.getDisplayName(), null);
        }
        writer.endElement("img");
    }
}
