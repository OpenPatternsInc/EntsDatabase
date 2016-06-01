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

package net.openpatterns.ents.gui;

import net.openpatterns.ents.database_library.Database;
import net.openpatterns.ents.database_library.Ent;

import javax.swing.*;
import java.awt.*;

/**
 * Things related to UI to keep everything else cleaner.
 */
public class UI {

    /**
     * Brings up a y/n dialog to confirm something given by the String text.
     * 1 means confirm, 0 means cancel
     */
    public static int confirmDialog(Container container, String message, String confirmText) {

        String[] options = {confirmText, "Cancel"};
        int n = JOptionPane.showOptionDialog(container, message, "Please Confirm...", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

        //1 means yes, 0 means no!
        if (n == 0)
            return 1;
        return 0;
    }

    /**
     * <p>Warns the user that this software is in early development and comes without a warranty.
     * It could mess up their computer.</p>
     * <p>If they click exit, it should quit the program. The number of warnings given
     * will be incremented and the settings will be saved to record this.</p>
     * @return 1 if confirm, 0 if exit program.
     */
    public static int showDevelopmentSoftwareWarningDialog(Settings settings) {
        //increment times warned now. even if they click Exit now, that still counts as a warning.
        settings.incrementTimesWarned();
        String[] options = {"I understand", "Exit"};
        String message = "This software is in early development.\nIt could potentially damage your computer.";
        //what is their response?
        int choiceIndex = JOptionPane.showOptionDialog(new JFrame(), message, "WARNING", JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        if (choiceIndex == 0) {
            //they say they understand
            System.out.println("You have been warned.");
            return 1;
        } else
            return 0;
    }


    /**
     * <p>Asks the user to input a name for the new Ent. If the name is already taken it will inform the user and ask
     * again until it gets a good response or the user cancels.</p>
     * @param database       The database the new Ent would be added to.
     * @param parent         The UI component the Dialog should be displayed over.
     * @param specialMessage Custom message, in case a name was already taken or invalid.
     * @return
     */
    public static Ent queryNewEntName(Database database, Component parent, String specialMessage) {

        String message;

        if (specialMessage == null || specialMessage.equals("")) {
            message = "Enter name of new Ent.";
        } else {
            message = specialMessage;
        }

        String name = (String) JOptionPane.showInputDialog(parent, message, "", JOptionPane.PLAIN_MESSAGE, null, null, "");

        /* If the JOptionPane was canceled, then name is null */
        if (name == null)
            return null;

        /* Check if the name already exists. */
        if (database.isNameValidAndFree(name)) {
            Ent newEnt = new Ent(database);
            newEnt.setName(name);
            database.addEnt(newEnt);
            return newEnt;
        } else {
            //TODO add specific responses to specific issues.
            return queryNewEntName(database, parent, "Invalid name");
        }
    }

    public static void ok(String message) {
        //JOptionPane.showMessageDialog();
    }

}
