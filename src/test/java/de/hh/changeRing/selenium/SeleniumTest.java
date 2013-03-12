package de.hh.changeRing.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import de.hh.changeRing.Context;
import de.hh.changeRing.FunctionalTest;
import de.hh.changeRing.ThemeSwitcher;
import de.hh.changeRing.jsfExtension.SecurityFilter;
import de.hh.changeRing.user.UserSession;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.net.URL;

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
public abstract class SeleniumTest {
    @Drone
    protected DefaultSelenium browser;
    @ArquillianResource
    protected URL deploymentUrl;

    protected static WebArchive warWithBasics() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addClasses(FunctionalTest.ENTITY_CLASSES)
                .addClasses(Context.class, UserSession.class, ThemeSwitcher.class)
                .addPackage(SecurityFilter.class.getPackage());
        webArchive.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("src/main/webapp").as(GenericArchive.class),
                "/", Filters.includeAll());
        return webArchive;
    }
}
