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

package net.openpatterns.ents.gui;

import net.openpatterns.ents.gui.launcher.Launcher;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * <p>This class holds the settings for the Ents GUI Explorer.
 * Makes it easy to organize.</p>
 * <p>Settings are shared between both the Launcher and Explorer packages
 * and so the class is located above both of them.</p>
 */
public class Settings {

    /**
     * <p>The directory the user chose to be the default one to open when looking
     * for ent database files to explore and edit.</p>
     */
    private String preferredWorkingDirectory;
    /**
     * The number of times the user has been warned that this is untested software.
     */
    private int timesWarned = 0;
    /**
     * Have the settings been loaded yet?
     */
    private boolean settingsLoaded;
    /**
     * <p>Does the user not want to use a settings file?</p>
     * <p>This could occur if the settings file is unable to be created or the user
     * simply doesn't want to save any settings. They will be warned every time they
     * open the application though...</p>
     */
    private boolean noSettingsMode;
    /**
     * The Launcher which is managing the Explorers.
     */
    private Launcher launcher;
    /**
     * The Instance of IO which handles loading and saving of files.
     */
    private IO io;

    /**
     * <p>Initializes a Settings instance with a reference to the Launcher that will
     * be using it.</p>
     * <p>We'll try to load the settings file or if none is found, create a new one.</p>
     */
    public Settings(Launcher launcher) {
        this.launcher = launcher;
        /* Initiate the IO instance to handle loading and saving files. */
        io = new IO();
        /* Try to load settings from one of the default locations file. */
        int loadResult = tryToLoadSettings();
        /* If the result is 0 we need to exit the program at the user's wishes.
        * No need to clean up anything this early on, so we can just exit.*/
        if (loadResult == 0)
            System.exit(0);
    }

    //TODO Show warning sign 3 times and then hide it... gotta sufficiently warn people...

    /**
     * <p>Tries to load settings from file, and consults the user along the way.</p>
     * @return 1 to continue using <br/>
     * 0 to exit program completely
     */
    private int tryToLoadSettings() {
        //First try and determine a reasonable place where settings should be.
        int settingsDirectoryResult = io.determineSettingsDirectory();
        if (settingsDirectoryResult == 0) {

            /** Haven't gone over this section yet */

            return 0;

            /*
            //suitable directory could not be found... can still use Explorer, but no settings can be saved or loaded.
            //take this time to warn the user about using development software
            int warningResult = UI.showDevelopmentSoftwareWarningDialog();
            if (warningResult == 1) {
                //they wimped out. close da program
                return 0;
            }
            //make a dialog saying as much
            String message = "A suitable place to save settings could not be found.\nEntsExplorer will not be able to" +
                    " automatically load and save preferences or configurations.\nIf you want, please inform the" +
                    " developers of this important problem.\nThey can be found at openpatterns.net";
            String[] options = {"Continue without saving", "Close"};
            int result = JOptionPane.showOptionDialog(new JFrame(), message, "Error",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            //if they want to close the program, close it.
            if (result == 1)
                return 0;
            */
        } else if (settingsDirectoryResult == 1) {
            //suitable directory found! lets see if a settings file exists and has actual contents!
            List<String> rawSettings = io.tryToLoadRawSettings();
            //if rawSettings is null, the file wasn't found, otherwise it might be empty, but the file was found.
            if (rawSettings == null) {
                //no settings file found, so we must first warn the user
                if (0 == UI.showDevelopmentSoftwareWarningDialog(this)) {
                    //User is sufficiently wary, just end the program.
                    return 0;
                }
                // so ask the user if we can create one
                String message = "Create a settings file in the following directory:\n\n" + io.getSettingsDirectory();
                String[] options = {"Create file", "Continue without saving"};
                int result = JOptionPane.showOptionDialog(new JFrame(), message, "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (result == 1) {
                    //they want to continue without creating the settings file, ok
                    System.out.println("Settings file not created. We can't count how many times you have been warned D:");
                    noSettingsMode = true;
                    return 1;
                } else if (result == 0) {
                    //ok, they want to create the file, so lets do it
                    if (save() == 1) {
                        //file was successfully created
                        JOptionPane.showMessageDialog(new JFrame(), "Settings successfully saved!", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (result == JOptionPane.CLOSED_OPTION) {
                    /* The user just exited out of the Pane, so return 0. */
                    return 0;
                }
            } else {
                /* Ok, we have some settings to load */
                int settingsLoadResult = parseSettingsFromStrings(rawSettings);
                /* Does the user need to be warned? */
                if (timesWarned < 3) {
                    //Show the warning and exit if they don't want to continue.
                    if (0 == UI.showDevelopmentSoftwareWarningDialog(this)) return 0;
                }
            }
        } else {
            System.out.println("Error in Settings.java");
            return 1;
        }
        return 1;
    }

    public String getPreferredWorkingDirectory() {
        return preferredWorkingDirectory;
    }

    private void setPreferredWorkingDirectory(String directory) {
        preferredWorkingDirectory = directory;
    }

    /**
     * Records the number of times the user has been warned.
     */
    private void setTimesWarned(int times) {
        timesWarned = times;
    }

    /**
     * <p>Increments the number of times warned by the UI method which displays the warning dialog.</p>
     * <p>If the user has not consented to the settings file being created yet, this increment
     * will be saved when and if they do.</p>
     */
    void incrementTimesWarned() {
        timesWarned++;
        /* Save the fact that we warned the user. If the settings file doesn't exist, we don't
         * need to warn the user because that should not be a problem. */
        if (!noSettingsMode && io.doesSettingsFileExist())
            save();
    }

    /**
     * <p>Loads settings from the contents of the settings file.</p>
     * <p>The first line should be the number of times the user has been warned
     * about using this software, because it might be unstable.The second line
     * should be the preferred directory to save Ents data files, or blank. The third line
     * should be the last loaded data file, or blank.</p>
     * @param strings A List of Strings
     * @return int 1 if settings loaded successfully
     * 2 if settings weren't the correct format
     */
    private int parseSettingsFromStrings(List<String> strings) {
        //make sure there are 3 lines
        if (strings.size() != 2) {
            System.out.println("Settings file wasn't the correct format.");
            return 2;
        }
        //1: how many times has the user been warned?
        //make sure there's no funny business
        try {
            setTimesWarned(Integer.parseInt(strings.get(0)));
        } catch (NumberFormatException e) {
            System.out.println("Error: Settings file seems to be corrupt...");
            //TODO Do something specific about it.
            setTimesWarned(0);
            return 2;
        }
        //2: Make sure the directory exists.
        //TODO If it doesn't exist... ask to select a new one.
        Path pwd = Paths.get(strings.get(1));
        if (Files.exists(pwd)) {
            setPreferredWorkingDirectory(pwd.toFile().toString());
        } else {
            //Preferred directory doesn't exist. Better ask the user to choose another one.
        }
        //3: Load last database file
        /* Not implemented yet.
        Path database = Paths.get(strings.get(2));
        if (Files.exists(database)) {
            //ooh, file exists, lets load it!
            //TODO actually load the database
        }
        */


        return 1;
    }

    //TODO Finish saving relevant info!

    /**
     * <p>Saves the settings to the designated SETTINGS_DIRECTORY.</p>
     * <p>First line is the number of times the user has been warned that this
     * is early development software.</p>
     * @return 1 if success
     * 0 if failure
     */
    private int save() {
        //first we must generate the content of the save file. Use StringBuilder for efficiency.
        StringBuilder builder = new StringBuilder();
        //first add the warningTimes.
        builder.append(timesWarned).append('\n');
        //second line is preferred working directory. if it's null, change it to empty
        if (preferredWorkingDirectory == null || preferredWorkingDirectory.equals("")) preferredWorkingDirectory = "";
        builder.append(Paths.get(preferredWorkingDirectory).toFile().toString()).append('\n');
        //third file is the last open database file... but we aren't there yet!!!!
        return io.saveStringToSettings(builder.toString());
    }


}
