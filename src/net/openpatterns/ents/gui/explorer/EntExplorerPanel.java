/*******************************************************************************
 * The GUI Ents Explorer helps you visually alter and view an Ents Database Hierarchy.
 * Copyright (c) 2016. Jason Stockwell and OpenPatterns Inc.
 * www.openpatterns.net/#ents
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package net.openpatterns.ents.gui.explorer;

import net.openpatterns.ents.database_library.Database;
import net.openpatterns.ents.database_library.Ent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * <p>A panel with 3 elements for exploring an Ents Hierarchy.</p>
 * <p>The center panel displays information about an Ent, like its name and
 * description. The left panel is an EntList displaying its parents, and the
 * right panel is an EntList displaying its children.</p>
 */
abstract class EntExplorerPanel extends JPanel implements ComponentListener {

    /**
     * The types of EntExplorerPanels, either it is displaying parents or children.
     */
    protected static int PARENT_LIST = 0, CHILD_LIST = 1, GENERAL_LIST = 2;
    /**
     * The center panel displaying info on the Ent of focus.
     */
    protected EntInfoPanel infoPanel;
    /**
     * The two EntLists which display the parents and children and allow for navigation.
     */
    protected RelationListPanel pList, cList;
    /**
     * The Database which is being explored.
     */
    protected Database data;
    /**
     * The Ent of focus.
     */
    protected Ent entFocus;
    //TODO Make an EntExplorerFrame? Any benefit to that?
    /**
     * Keep this reference in order to change the title later.
     */
    private JFrame parentFrame;

    EntExplorerPanel(Database data, JFrame parentFrame) {
        this.data = data;
        this.parentFrame = parentFrame;
        /* Set the layout to display the 3 panels side by side. */
        GridLayout gridLayout = new GridLayout(1, 3);
        setLayout(gridLayout);
        /* Initiate the 3 panels. */
        pList = new RelationListPanel(this, EntListPanel.ListType.PARENTS);
        infoPanel = new EntInfoPanel();
        cList = new RelationListPanel(this, EntListPanel.ListType.CHILDREN);
        add(pList);
        add(infoPanel);
        add(cList);
        addComponentListener(this);
    }

    /**
     * Sets the title of the Frame
     */
    void setTitle(String text) {
        parentFrame.setTitle(text);
    }

    protected RelationListPanel getParentList() {
        return pList;
    }

    protected RelationListPanel getChildList() {
        return cList;
    }

    protected Ent getEntFocus() {
        return entFocus;
    }

    /**
     * Sets the Ent of focus for this EntPanel
     */
    public void setEntFocus(Ent entFocus) {
        this.entFocus = entFocus;
        pList.setEnts(entFocus.getParents());
        infoPanel.setEnt(entFocus);
        cList.setEnts(entFocus.getChildren());
        setTitle(entFocus.getName());
    }

    /**
     * Called when an Ent has been left-clicked.
     */


    /*******************************************************************************
     * Abstract Methods
     *******************************************************************************/


    /*******************************************************************************
     * ComponentListener Event Stuff
     *******************************************************************************/


    @Override
    public void componentResized(ComponentEvent e) {
        /* Make all 3 panels of close-to-equal-size. */
        int height = getHeight();
        int width = getWidth();
        Dimension dim = new Dimension(width / 3, height);
        cList.setPreferredSize(dim);
        cList.setMinimumSize(dim);
        cList.setMaximumSize(dim);
        pList.setPreferredSize(dim);
        pList.setMinimumSize(dim);
        pList.setMaximumSize(dim);
        infoPanel.setPreferredSize(dim);
        infoPanel.setMinimumSize(dim);
        infoPanel.setMaximumSize(dim);
        revalidate();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
