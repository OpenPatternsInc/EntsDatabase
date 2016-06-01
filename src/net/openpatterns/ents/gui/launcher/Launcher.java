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

package net.openpatterns.ents.gui.launcher;

import net.openpatterns.ents.database_library.Database;
import net.openpatterns.ents.database_library.DatabaseIO;
import net.openpatterns.ents.database_library.DatabaseLoadReturnElement;
import net.openpatterns.ents.gui.IO;
import net.openpatterns.ents.gui.Settings;
import net.openpatterns.ents.gui.explorer.Explorer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * <p>Handles the launching of the GUI (Maybe rename this to Manager?).</p>
 * <p>Takes care of things like launching settings, creating a
 * settings file if needed, warning the user that is is development
 * software, etc.</p>
 * <p>No need to initialize this class which will be accessed statically.
 * The reasoning behind this is so that only one launcher can be active at once.
 * We want this single Launcher class to handle multiple ExplorerFrames.
 * If this is unnecessary, certainly say why, as it feels a bit contrived.</p>
 * <p>This class is public to allow for Explorers to be created by other programs.
 * This seems to make logical sense moving forward. If it creates any security issues,
 * then a different approach will be taken.</p>
 */
public class Launcher {

    //TODO Allow for multiple ExplorerFrames to be open, exploring multiple databases and talking to each other.

    /**
     * Singleton instance.
     */
    public static Launcher launcher;
    /**
     * A dummy JFrame to use to show some JOptionDialogs as they need
     * a parent frame to show off of.
     */
    private final JFrame dummyFrame = new JFrame();
    /**
     * <p>The Explorer instances that are open at the moment.</p>
     * <p>We need to make sure two Explorers aren't editing the same
     * database at once. We also should know what databases are open so
     * we can make certain decisions like if to open a Database automatically
     * when creating a new Explorer.</p>
     */
    private ArrayList<Explorer> explorers;
    /**
     * The Settings instance which will handle loading, querying and saving of
     * preferences and current states of the application.
     */
    private Settings settings;
    /**
     * The DatabaseSelector which will help the user select a new or existing database to load.
     */
    private DatabaseSelector databaseSelector;
    /**
     * An IO instance for things like loading files.
     */
    private IO io;

    //TODO Allow the user to disable automatic loading of last explored database

    /**
     * <p>Creates a new instance of Launcher.</p>
     * <p>Tries to start an instance of Explorer if that seems appropriate.</p>
     */
    private Launcher() {
        /* First of course we warn the user that this is untested software */
        /* Initiate the explorers list */
        explorers = new ArrayList<>();
        /* Initiate an IO instance in case the user wants to choose a file to load. */
        io = new IO();
        /* Initiate the Settings instance which will try to load the settings file
         * or try to create a new one. It will also warn the user that this is untested
         * software if they haven't already been warned 3 times. */
        settings = new Settings(this);
        /* Now we can either load the database from the previous session or bring the
         * user to a frame to choose their own. This may be a favorite, one from a chose
         * location, an example database, or an empty database.
         * Lets initialize the DatabaseSelector and give it the Settings so it knows the
         * user's preferences. */
        databaseSelector = new DatabaseSelector(settings, this);
    }

    /**
     * Returns the current Launcher or creates it.
     */
    public static Launcher getInstance() {
        if (launcher == null)
            launcher = new Launcher();
        return launcher;
    }

    /**
     * <p>Create a new ExplorerFrame.</p>
     * <p>The optional Database argument is what the new Explorer will display.
     * If that database is already open however, we should refrain from doing that
     * and instead display a message saying why.</p>
     */
    public void openNewExplorer(Database database) {
        /** What to do if it's not null? For now nothing. */
        if (database != null) {

        } else {
            /** OK, no Database was specified so... */
        }

    }

    //TODO Remember the last directory and/or the Preferred Working Directory. Done via Settings.

    /**
     * <p>Called when the DatabaseSelector has been exited.</p>
     * <p>If there are no open UI elements we should exit the program.</p>
     */
    void databaseSelectorExited() {
        if (explorers.size() == 0) {
            System.exit(0);
        }
    }

    /**
     * Called when the user wants to open a database by selecting the file manually.
     */
    void chooseDatabaseFileToLoad(Component parent) {
        /* Open a dialog for the user to select a database. Set custom approve text and file extension of databases. */
        File file = io.letUserLocateFile(parent, "Open Database", "Please Select an Ents Database", false, IO.entsFilter);
        /* Was anything selected? If not, there's nothing to do really. */
        if (file != null) {
            /* Try to load the database into a Database instance. */
            DatabaseLoadReturnElement returnElement = DatabaseIO.generateDatabaseFromFile(file.toPath());
            //TODO make sure the database chosen isn't already being edited.
            /* OK, a file was chosen, switch to an Explorer. */
            /* Close the DatabaseSelector */
            databaseSelector.setMainFrameVisible(false);
            System.out.println("Not supported yet...");
        }
    }

    /**
     * <p>Loads the example database that was hard-coded.</p>
     * <p>We don't need to check if this database is already being edited because it doesn't have a save file
     * associated with it. It can be saved later if the user wants to, but it will still not conflict with
     * other loadings of the hard-coded example database because that is never changed directly.</p>
     */
    void loadExampleDatabase() {
        System.out.println("Loading example database...");
        /* Create a new Explorer. */
        Explorer newExplorer = new Explorer(this);
        /* Get an instance of Database with the example hierarchy. */
        Database exampleDatabase = Database.generateExampleDatabase(newExplorer);
        /* Now hook them up. The ordering of this is important to initiate the Explorer with the loaded database.
         * Worth it to rethink the reasoning behind the linking up algorithm. */
        newExplorer.setDatabase(exampleDatabase);
        /* Set the MainFrame of the Selector invisible. */
        databaseSelector.setMainFrameVisible(false);
        /* Set the MainFrame of the Explorer visible. */
        newExplorer.setMainFrameVisible(true);
    }

    /**
     * Called when an Explorer is being closed.
     */
    public void onExplorerClose(Explorer closingExplorer) {
        /* Remove the explorer from the list of active ones. */
        explorers.remove(closingExplorer);
        /* If there are no more open explorers, show the Database selector. */
        if (explorers.size() == 0) {
            databaseSelector.setMainFrameVisible(true);
        }
    }

}
