package de.hh.changeRing.infrastructure.jsfExtension;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import java.util.Random;


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
public class RingResourceHandler extends javax.faces.application.ResourceHandlerWrapper {
    private static final String REVISION = "" + new Random().nextInt();
    private final ResourceHandler wrapped;

    public RingResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        Resource resource = super.createResource(resourceName, libraryName);
        return resource == null ? null : new RingResource(resource);
    }

    public static class RingResource extends javax.faces.application.ResourceWrapper {
        private final Resource resource;

        public RingResource(Resource resource) {
            this.resource = resource;
        }

        @Override
        public Resource getWrapped() {
            return this.resource;
        }

        @Override
        public String getRequestPath() {
            String requestPath = resource.getRequestPath();
            return requestPath + (requestPath.contains("?") ? "&rev=" : "?rev=") + REVISION;
        }

        @Override
        public String getContentType() {
            return getWrapped().getContentType();
        }
    }
}
