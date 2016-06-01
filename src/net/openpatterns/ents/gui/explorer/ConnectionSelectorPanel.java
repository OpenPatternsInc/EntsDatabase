/*******************************************************************************
 * The GUI Ents Explorer helps you visually alter and view an Ents Database Hierarchy.
 * Copyright (c) 2016. Jason Stockwell and OpenPatterns Inc.
 * www.openpatterns.net/#ents
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package net.openpatterns.ents.gui.explorer;

import net.openpatterns.ents.database_library.Database;
import net.openpatterns.ents.database_library.Ent;
import net.openpatterns.ents.gui.ui_templates.InfoWindow;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Handles the adding of new connections to an Ent (parents, children or exclusions)
 */
class ConnectionSelectorPanel extends EntExplorerPanel implements MouseListener {

    final static int PARENTS = 0, CHILDREN = 1, EXCLUSIONS = 3;
    private int type;
    private JButton addButton;
    /**
     * The Ent which this panel is based off of, looking for connections.
     */
    private Ent ent;
    private InfoWindow infoWindow;

    ConnectionSelectorPanel(Database data, int type, Ent ent, JFrame parentFrame) {
        super(data, parentFrame);
        this.type = type;
        this.ent = ent;
        addButton = new JButton();
        addButton.addMouseListener(this);

        //need to initialize pList and cList as SelectorLists
        //pList = new SelectorList(data, this, PARENT_LIST, ent);
        //cList = new SelectorList(data, this, CHILD_LIST, ent);
        //initialize info panel
        infoPanel = new EntInfoPanel();
        //now add them to the panel
        add(pList);
        add(infoPanel);
        add(cList);

        if (type == PARENTS)
            addButton.setText("Add Parent");
        else if (type == CHILDREN)
            addButton.setText("Add Child");
        else if (type == EXCLUSIONS)
            addButton.setText("Add Exclusion");


    }
    /*
    @Override
    protected void parentLeftClicked(Ent ent) {
        setEnt(ent);
    }

    @Override
    protected void parentDoubleLeftClicked(Ent ent) {

    }

    @Override
    protected void childLeftClicked(Ent ent) {
        setEnt(ent);
    }

    @Override
    protected void childDoubleLeftClicked(Ent ent) {

    }

    @Override
    protected void childRightClicked(Ent ent) {

    }

    @Override
    protected void parentRightClicked(Ent ent) {

    }
    */

    /**
     * Returns what list this panel is trying to select.
     */
    /*
    @Override
    protected int getListType() {
        return type;
    }
    */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(addButton)) {
            System.out.println("User wants to add this Ent to a list!");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
