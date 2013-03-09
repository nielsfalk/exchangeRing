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
