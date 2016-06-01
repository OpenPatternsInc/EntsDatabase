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

import javax.swing.*;
import java.awt.*;

/**
 * <p>Used when selecting new children, parents or exclusions.</p>
 * <p>A separate frame in order to keep the main ExplorerFrame unchanged.
 * One will be able to explore the hierarchy like the normal Explorer, but
 * it will be for a certain purpose, like "looking for parents to add".
 * If an ent would be displayed and it was a descendant of the Ent in the focus
 * of the main ExplorerFrame, it may be colored red to indicate that adding
 * it as a parent would result in an inconsistency of the hierarchy. This way
 * the user knows what is likely a potential addition to the list at hand, but
 * still is able to navigate the hierarchy how they choose.</p>
 */
class ConnectionFrame extends JFrame {

    private ConnectionSelectorPanel selectorPanel;
    private int type;
    private Ent ent;

    ConnectionFrame(Database data, int type, Ent ent) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(910, 410);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        //set the title
        if (type == ConnectionSelectorPanel.CHILDREN)
            setTitle("Select children to add under \"" + ent.getName() + "\"");
        if (type == ConnectionSelectorPanel.PARENTS)
            setTitle("Select parents to add to \"" + ent.getName() + "\"");
        if (type == ConnectionSelectorPanel.EXCLUSIONS)
            setTitle("Select ents which are exclusive to \"" + ent.getName() + "\"");

        this.type = type;
        this.ent = ent;

        selectorPanel = new ConnectionSelectorPanel(data, type, ent, this);
        //selectorPanel.setEnt(ent);

        add(selectorPanel);


    }


}
