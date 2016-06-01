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

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * <p>Handles the tasks of saving and loading the settings file for the GUI Ents Explorer.</p>
 * <p>May be more descriptive to rename FileHandler?</p>
 * <p>If no settings file is found, it will try to create one.</p>
 * <p>Many methods will be context-free methods.</p>
 * <h3><b><center>Approach to creating settings file</center></b></h3>
 * <p>This program needs to create a file in order to save references to arbitrary locations on the file
 * system. It will make files in predetermined locations with the user's permission.</p>
 * <p>On loading, it will search the predetermined locations for a file named ents_explorer_settings
 * and if found, load it. If not found, we assume this is the first loading of the program and
 * we will ask the user to create the file and give the directory.</p>
 * <p>First, get the location of the running file, likely IO.class. Next, check if the path
 * includes .jar somewhere, which means the program is running as an executable JAR package.
 * We can not save a file within the JAR, so instead save it in the same directory. If it is not
 * part of a JAR, instead save it along with the .class file.</p>
 * <p>Confirm with the user that the action of creating the file in the specified location is
 * permissible.</p>
 * <p>For the case that people debugging and contributing to the code, it shouldn't be too much
 * of a problem to have the file saved in the same location as the class files.</p>
 */
public class IO {


    /** The name the settings file will have. */
    private static final String SETTINGS_FILE_NAME = "ents_explorer_gui_settings";
    /** The location of this class helps determine where to put the settings file. */
    private static final String CLASS_LOCATION = "net/openpatterns/ents/gui/IO.class";
    /**
     * The file extension for an Ents Database. Needs to be in array form for use with FileNameExtensionFilter.
     */
    private static final String[] ENTS_DATABASE_FILE_EXTENSION = {"ents"};
    /**
     * A FileFilter for Ents Database files with a .ents extension.
     */
    public static final FileNameExtensionFilter entsFilter = new FileNameExtensionFilter("*.ents (Ents Database)", ENTS_DATABASE_FILE_EXTENSION);
    private final int LOAD_SETTINGS_FAILED = 0;
    /**
     * <p>The directory where settings should be saved.</p>
     * <p>Can't be "final", but use caps anyways as it is generated
     * via methods.</p>
     */
    private String SETTINGS_DIRECTORY = null;

    /**
     * Initializes an IO instance. Does nothing at the moment however.
     */
    public IO() {
    }


    /**
     * <p>Allows you to load a text file from a file given by a String.
     * Returns a List of Strings for each line.</p>
     */
    private List<String> textFileToStringList(String path) {
        return textFileToStringList(Paths.get(path));
    }

    /**
     * <p>Loads a text file at the given location and returns a list of Strings,
     * separated by the newlines of the original file.</p>
     * <p><i>Does not check that the file exists.</i></p>
     */
    private List<String> textFileToStringList(Path path) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e);
        }
        return lines;
    }

    /**
     * <p>Tries to find a suitable directory in the same location as the IO.class file.
     * If the class file is actually in a JAR executable, choose the directory that the
     * JAR is located in.</p>
     * <p>Returns 1 if suitable directory found and SETTINGS_DIRECTORY set.
     * Returns 0 if the settings file couldn't be found, and 2 if
     * the file found could not be loaded. Returns 3 if the program couldn't find its
     * own location.</p>
     * @return 0 if there was a problem maneuvering the file system. <br/>
     * 1 if a suitable directory was found and SETTINGS_DIRECTORY was set.
     * 2 if the system doesn't use forward or backslashes for some reason.
     */
    int determineSettingsDirectory() {

        ClassLoader loader = IO.class.getClassLoader();
        if (loader == null) {
            /* Dunno if this can really return null... */
            System.out.println("Error: ClassLoader returned null.");
            return 0;
        }
        /* Finds the location of this class file. Seems like a reasonable place
        to start looking for a save location for the settings file. Maybe this is dumb though*/
        URL classLocationURL = loader.getResource(Paths.get(CLASS_LOCATION).toFile().toString());

        if (classLocationURL == null) {
            System.out.println("Error: Could not find adequate place to save settings file.");
            return 0;
        }
        String classLocation = classLocationURL.toString();
        //OK, now determine where the settings should go/be
        //remove the leading "file:" part from the URL if need be
        classLocation = classLocation.replaceFirst("file:", "");
        /*
         * If it's been compiled into a .jar file, save the properties on the same level as that executable.
         * We probably can't and would rather not write into the JAR file... Or... do we? That would make it
         * portable.
         */
        int jarIndex = classLocation.indexOf(".jar");
        if (jarIndex != -1) {
            //OK remove the leading "jar:" from the URL part if it's there
            classLocation = classLocation.replaceFirst("jar:", "");
            //OK, we're dealing with being inside a .jar archive. Find the slash before .jar.
            int slashIndex = classLocation.lastIndexOf('\\', jarIndex);
            //if this returns -1 the OS must use forward slashes instead?
            if (slashIndex == -1) slashIndex = classLocation.lastIndexOf('/', jarIndex);
            //if still no slash is found, we're not in kansas or something
            if (slashIndex == -1) {
                System.out.print("Error: Does this system really not use forward or backslashes?");
                return 2;
            }
            //truncate to just before the slash
            SETTINGS_DIRECTORY = classLocation.substring(0, slashIndex);
        } else {
            //Find the last slash as that's the directory the class file is in.
            int slashIndex = classLocation.lastIndexOf('\\');
            if (slashIndex == -1) slashIndex = classLocation.lastIndexOf('/');
            if (slashIndex == -1) {
                System.out.print("Error: Does this system really not use forward or backslashes?");
                return 2;
            }
            //TODO Do we want the ending slash or not?
            SETTINGS_DIRECTORY = classLocation.substring(0, slashIndex);
        }
        return 1;
    }


    /**
     * <p>Tries to load the settings from the settings file.</p>
     * @return List of Strings
     * null       if settings not found
     * non-null   if it loaded some strings of indeterminant value
     */
    List<String> tryToLoadRawSettings() {

        //lets build the full path to the potential settings file
        Path path = Paths.get(SETTINGS_DIRECTORY, SETTINGS_FILE_NAME);
        //lets see if the settings file exists
        if (!Files.exists(path)) {
            //OK, no settings found
            return null;
        }
        //ok, file exists, so lets try to load it
        List<String> rawSettings = textFileToStringList(path.toFile().toString());
        //now process the contents of the file
        return rawSettings;
    }

    /**
     * <p>Loads an Ents database file into the current Explorer.</p>
     * <p>Already assumed that the file exists.</p>
     */
    private int loadEntsDatabaseFromFile(String path) {
        //load up them lines
        List<String> rawLines = textFileToStringList(path);
        //TODO Finish this
        return 0;
    }

    /**
     * Returns a String with the value of the SettingsDirectory. Can't be edited this way.
     */
    String getSettingsDirectory() {
        return String.valueOf(SETTINGS_DIRECTORY);
    }

    //TODO Put some failsafes in to make sure we aren't doing something stupid or leaving a vulnerability open

    /**
     * <p>Saves a string to the given location.</p>
     * <p>Haven't double checked that we aren't writing over stuff, so only write
     * to the directory approved by the user.</p>
     * @return 1 if success
     * 0 if failure
     */
    private int save(String output, String location) {
        try {
            PrintWriter writer = new PrintWriter(location, "UTF-8");
            writer.print(output);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            return 0;
        }
        //seems to have worked?
        return 1;
    }

    //TODO add double check to read from file and verify write? Eh... maybe..

    /**
     * <p>Saves the given String to the settings file</p>
     */
    int saveStringToSettings(String raw) {
        return save(raw, Paths.get(SETTINGS_DIRECTORY, SETTINGS_FILE_NAME).toFile().toString());
    }

    /**
     * Does the settings file exist?
     */
    boolean doesSettingsFileExist() {
        return Files.exists(Paths.get(SETTINGS_DIRECTORY, SETTINGS_FILE_NAME));
    }

    /**
     * <p>Asks the user to find a file.</p>
     * <p>We can specify the Swing Component that called this action, a custom text to display on the file select
     * approve button, and an array of the allowable file extensions, if any.</p>
     * @param parent               The parent Swing Component which the file chooser will appear above.
     * @param approveButtonText    The text to be displayed on the button confirming the file selection.
     * @param enableAllFileTypes   Should the user be given the option to display all file types?
     * @param fileExtensionFilters Any number of FileNameExtensionFilter instances that will be allowed.
     */
    public File letUserLocateFile(Component parent, String approveButtonText, String titleText, boolean enableAllFileTypes, FileNameExtensionFilter... fileExtensionFilters) {
        /* Use the Swing JFileChooser class to help us help the user select a file to load. */
        JFileChooser chooser = new JFileChooser();
        /* We only want to select files. */
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        /* Do we want to accept all File types, or just the specified filters? */
        chooser.setAcceptAllFileFilterUsed(enableAllFileTypes);
        /* Now set up filters. If there are more than 1 we need to add them individually. */
        if (fileExtensionFilters == null || fileExtensionFilters.length == 0) {
            /* Don't add a filter. */
        } else if (fileExtensionFilters.length == 1) {
            /* Only one filter */
            chooser.addChoosableFileFilter(fileExtensionFilters[0]);
        } else if (fileExtensionFilters.length > 1) {
            /* Multiple filters, lets add them all. */
            for (FileFilter filter : fileExtensionFilters)
                chooser.addChoosableFileFilter(filter);
        }
        /* TODO allow the user to load multiple databases at once.
         * This would be handled by the DatabaseSelector to make sure the same database wasn't being edited multiple times. */
        /* Only one file at a time for now */
        chooser.setMultiSelectionEnabled(false);
        /* Set the approve button text. */
        chooser.setApproveButtonText(approveButtonText);
        /* Sets the title text of the dialog. */
        chooser.setDialogTitle(titleText);
        /* Finally show the dialog. Here we set the parent Component, but don't give the button text because we set it earlier. */
        int returnVal = chooser.showDialog(parent, null);
        /* If they actually selected something, then return the file! */
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //TODO Save the directory to Settings and save settings so that we can open to that directory again later.
            File file = chooser.getSelectedFile();
            System.out.println("Chosen file: \"" + file + "\"");
            return file;
        } else {
            /* user didn't select anything */
            //TODO finish this. Maybe there is nothing to do though...
        }
        /* No file selected? Return null to keep you on your feet. */
        return null;
    }


}


