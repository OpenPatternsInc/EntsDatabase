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
import net.openpatterns.ents.gui.UI;

import javax.swing.*;

/**
 * <p>A JPanel which allows the user to explore an Ents hierarchy. Gives parents on the left, children on the right, and
 * info along with options in the center for the current Ent at hand.</p>
 * <p>Sits in an ExplorerPanel. They are separate to make adding additional UI elements easier.</p>
 */
class ExplorerMainPanel extends EntExplorerPanel {


    ExplorerMainPanel(Database data, JFrame parentFrame) {
        super(data, parentFrame);
    }

    //TODO don't give the user the option to delete root in the first place. Though the error message may be educational?

    /** Called when the user wants to delete the current Ent of focus. */
    void handleDeleteEnt() {
        /* User isn't trying to delete root are they? */
        if (entFocus.isRoot()) {
            JOptionPane.showMessageDialog(this, "You can't delete root!!!", "", JOptionPane.INFORMATION_MESSAGE);
        } else if (1 == UI.confirmDialog(this, "Are you sure you want to delete \"" + entFocus.getName() + "\"?", "Delete")) {
            /* Deletion Confirmed. */
            //TODO Go into detail about what is left when this Ent is deleted. What happens to its children?
            entFocus.delete();
            /* Set focus as root. */
            //TODO Not the best plan. Better to stay near the Ent, but it could have multiple parents... so root for now
            setEntFocus(entFocus.data.getRoot());
        }
    }


    void handleNewEnt() {
        /* Use UI tool to ask user for the name of the new Ent, make sure it's valid, and return a new Ent with that name or null. */
        Ent newEnt = UI.queryNewEntName(data, this, null);
        /* Was it null? */
        if (newEnt != null) {
            setEntFocus(newEnt);
        }
    }
}
