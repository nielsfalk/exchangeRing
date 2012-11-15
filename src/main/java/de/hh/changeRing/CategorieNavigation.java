package de.hh.changeRing;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: niles
 * Date: 15.11.12
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
public class CategorieNavigation {
    private DefaultMenuModel model;

    public CategorieNavigation() {
        //TODO use the stuff
        model = new DefaultMenuModel();

        //First submenu
        Submenu submenu = new Submenu();
        submenu.setLabel("Dynamic Submenu 1");


        MenuItem item = new MenuItem();
        item.setValue("Dynamic Menuitem 1.1");
        item.setUrl("#");
        submenu.getChildren().add(item);

        model.addSubmenu(submenu);

        //Second submenu
        submenu = new Submenu();
        submenu.setLabel("Dynamic Submenu 2");

        item = new MenuItem();
        item.setValue("Dynamic Menuitem 2.1");
        item.setUrl("#");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue("Dynamic Menuitem 2.2");
        item.setUrl("#");
        item.addActionListener(new ActionListener() {
            @Override
            public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        submenu.getChildren().add(item);

        model.addSubmenu(submenu);
    }
}
