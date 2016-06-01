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

import net.openpatterns.ents.gui.Settings;
import net.openpatterns.ents.gui.ui_templates.CustomListPanel;
import net.openpatterns.ents.gui.ui_templates.ListActionElement;
import net.openpatterns.ents.gui.ui_templates.ListElement;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * <p>Lets the user select a database to explore.</p>
 * <p>The user can choose between remembered favorite databases, recent databases,
 * a database from a chose location, an example database, or an empty database.</p>
 */
public class DatabaseSelector implements WindowListener {

    /**
     * The main frame used to select a database.
     */
    private DatabaseSelectorMainFrame mainFrame;
    /**
     * The user's settings.
     */
    private Settings settings;
    /**
     * The launcher of this program instance.
     */
    private Launcher launcher;

    /**
     * <p>Initialize a DatabaseSelector instance. If the database location given
     * is blank, bring up the normal view, otherwise try to load it up.</p>
     */
    DatabaseSelector(Settings settings, Launcher launcher) {
        this.settings = settings;
        this.launcher = launcher;
        /* Initialize the DatabaseSelectorMainFrame as we'll use it eventually. */
        mainFrame = new DatabaseSelectorMainFrame(this);
        /* Initialize the List elements of the mainframe */
        initializeListElements();
        /* Listen to the Frame so we know when it closes. */
        mainFrame.addWindowListener(this);
        /* Get the current preferred working directory. We won't hold its value locally in case
         * It later changes. */
        String pwd = settings.getPreferredWorkingDirectory();
        /* Make sure it's not blank. */
        if (pwd == null || pwd.equals("")) {
            /* It's blank so just open the default selector frame. */
        }
        /* Set the mode to start in GENERAL, for now. */
        mainFrame.setMode(DatabaseSelectorMainFrame.GENERAL);
    }

    /**
     * <p>Initializes the different ListActionElements for the different modes of the DatabaseSelectorMainFrame.</p>
     */
    private void initializeListElements() {
        /* Get the list instance for reference. */
        CustomListPanel listPanel = mainFrame.listPanel;
        /* Initialize the Array of different mode sets. Only 1 mode for now. */
        mainFrame.actionElements = new ArrayList[1];
        /* Create the Action List elements and their corresponding methods to call. */
        try {
            /* Initialize the general mode set of actions. */
            ArrayList<ListElement> generalModeElements = new ArrayList<>();
            /* Add an element for letting the user select an Ents Database from file. */
            ListActionElement openDatabaseFile = new ListActionElement(listPanel, "Open Database On File", DatabaseSelector.class.getMethod("chooseDatabaseFileToLoad"), this);
            generalModeElements.add(openDatabaseFile);
            /* Add an element to load up the example hard-coded database. */
            ListActionElement loadExampleDatabase = new ListActionElement(listPanel, "Load an example database", DatabaseSelector.class.getMethod("loadExampleDatabase"), this);
            generalModeElements.add(loadExampleDatabase);
            /* OK, now set the ArrayList to the right mode in the MainFrame. */
            mainFrame.actionElements[DatabaseSelectorMainFrame.GENERAL] = generalModeElements;

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    /**
     * <p>Called when the user wants to load a database from file.</p>
     * <p>Tells the Launcher that the user wants to choose a new database to open from file. Let it handle
     * the problem of multiple Explorers editing the same database.</p>
     */
    public void chooseDatabaseFileToLoad() {
        launcher.chooseDatabaseFileToLoad(mainFrame);
    }

    /**
     * Loads the example database that was hard-coded in.
     */
    public void loadExampleDatabase() {
        launcher.loadExampleDatabase();
    }

    /**
     * Called when the MainFrame is exited so we can tell the launcher or
     * do other special things.
     */
    private void onMainFrameClosed() {
        /* For now, when it exits, inform the Launcher because that's the only frame
         * in the DatabaseSelector suite. */
        launcher.databaseSelectorExited();
    }

    /**
     * Hide the MainFrame because it's no longer needed.
     */
    void setMainFrameVisible(boolean visible) {
        mainFrame.setVisible(visible);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        Object source = e.getSource();
        if (source == mainFrame) {
            onMainFrameClosed();
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
