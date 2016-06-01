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

package net.openpatterns.ents.gui.launcher;

import net.openpatterns.ents.gui.ui_templates.CustomListPanel;
import net.openpatterns.ents.gui.ui_templates.ListElement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * <p>This JFrame shows the user the basic options for their loading of databases.</p>
 */
class DatabaseSelectorMainFrame extends JFrame {

    /**
     * The possible modes for the selector to be in.
     */
    final static int GENERAL = 0;
    /**
     * An array of the options that will be given in each mode. These options are stored in arrayLists of ListActionElements.
     */
    ArrayList<ListElement>[] actionElements;
    /**
     * <p>The list holding basic options</p>
     */
    CustomListPanel listPanel;
    /**
     * The mode that the Selector should be in.
     */
    private int mode;
    /**
     * The parent DatabaseSelector instance.
     */
    private DatabaseSelector databaseSelector;

    /**
     * Initialize the frame, setting the parent DatabaseSelector in order to inform it of
     * decisions by the user.
     * @param databaseSelector The parent DatabaseSelector which will handle the logic of actions performed.
     */
    DatabaseSelectorMainFrame(DatabaseSelector databaseSelector) {
        super();
        setTitle("Ents Database Selector");
        this.databaseSelector = databaseSelector;
        //setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /* Initialize the JPanel with a list. */
        listPanel = new CustomListPanel();
        /* Add the list to the frame. */
        add(listPanel);
        pack();
        /* Center the frame on the screen */
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        /* Now that everything is set, make it visible */
        setVisible(true);
    }

    /**
     * Sets the current mode of the selector.
     * @param mode The mode to be set: 0 is GENERAL.
     */
    void setMode(int mode) {
        this.mode = mode;
        /* If a mode is just a set of actions, we can just put it in here. */
        if (mode == GENERAL)
            setListMode(mode);
    }

    /**
     * <p>Sets the JList to display the Actions from a given mode.</p>
     */
    private void setListMode(int mode) {
        /* Clear the list. */
        listPanel.clearList();
        /* Get the list of actions for this mode. */
        ArrayList<ListElement> elements = actionElements[mode];
        /* Add them to the list. */
        listPanel.addElements(elements);
    }

}
