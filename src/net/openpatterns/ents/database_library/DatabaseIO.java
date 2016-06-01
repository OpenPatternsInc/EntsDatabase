/*******************************************************************************
 * The Ents Database Library provides access to functions,
 * analysis and implementations of a Taxonomical Hierarchy.
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

package net.openpatterns.ents.database_library;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <h3>Handles IO tasks like reading and writing files.</h3>
 * <p>Basic saving of a database via writing a string to file</p>
 * <p>Needs to have the capabilities expected of it, being able to easily
 * save and load Ents databases and enable API access.</p>
 * <p>Should of course mitigate potential to mess shit up,
 * potentially with checks.</p>
 */
public class DatabaseIO {

    //TODO Change the Ent UIDs to unsigned numbers to make it more uniform.
    //TODO Load everything at once, don't go line by line. Unnecessary risk.
    //TODO Let the user change the location to save the databases, and name them, etc.


    static String loadData(Database data) {


        //TODO Implement better way of loading stuff.

        String line = null;

        try {
            FileReader fileReader = new FileReader(line);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //discard first line
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                //ok, line now has the text with UID, name and info
                //find the locations of the pound sign and at sign
                int pound = line.indexOf('#');
                int at = line.indexOf('@');
                int UID = Integer.parseInt(line.substring(0, pound));
                String name = line.substring(pound + 1, at);
                String info = line.substring(at + 1);
                //OK now deal with parents
                line = bufferedReader.readLine();
                int index = line.indexOf('$');
                ArrayList<Integer> parents = new ArrayList<>();
                if (index != -1) {
                    parents.add(Integer.parseInt(line.substring(1, index)));
                    int next = line.indexOf('$', index + 1);
                    while (next > index) {
                        parents.add(Integer.parseInt(line.substring(index + 1, next)));
                        index = next;
                        next = line.indexOf('$', index + 1);
                    }
                }
                //And now children
                line = bufferedReader.readLine();
                index = line.indexOf('$');
                ArrayList<Integer> children = new ArrayList<>();
                if (index != -1) {
                    children.add(Integer.parseInt(line.substring(1, index)));
                    int next = line.indexOf('$', index + 1);
                    while (next > index) {
                        children.add(Integer.parseInt(line.substring(index + 1, next)));
                        index = next;
                        next = line.indexOf('$', index + 1);
                    }
                }
                //OK, now lets create that ent!
                Ent ent = new Ent(data, name, UID, info, parents, children, null);
                //if it's root, then it's special
                if (UID == 0)
                    data.replaceRoot(ent);
                else
                    data.loadEnt(ent);
            }
            //ok now that we've loaded the Ents, we can set the connections between them
            for (Ent current : data.getEnts())
                current.setConnections();

        } catch (FileNotFoundException e) {
            //dangit
        } catch (IOException e) {
            //double-drat
        }
        return null;
    }

    /**
     * <p>Used to generate a Database object with the information held within an
     * Ents Database file.</p>
     * <p>Takes a java.nio.Path as the argument because that would seem to be platform
     * independent. Instead of using a String, it may force the user to make sure it's a
     * valid path and not just an incompatible String. Not 100% sure of this reasoning
     * however.</p>
     * <p><i>For an overview of the Database protocol, please visit </i></p>
     * @param path A java.nio.Path that points to the database file to be loaded.
     * @return The Database object generated from the file.
     */
    public static DatabaseLoadReturnElement generateDatabaseFromFile(Path path) {
        //Generate a List of Strings from the text file. File uses newlines extensively.
        List<String> rawLines = textFileToStringList(path);
        //Make sure it's not null, meaning there weren't any errors loading it;
        if (rawLines == null) {
            //loading error
            return new DatabaseLoadReturnElement(DatabaseLoadReturnElement.ERROR_LOADING, null, "File could not be loaded.");
        } else if (rawLines.size() == 0) {
            //Blank files aren't very interesting!
            return new DatabaseLoadReturnElement(DatabaseLoadReturnElement.BLANK_FILE, null, "File was empty.");
        } else {
            //So, we have some lines to iterate through...
            Iterator<String> iterator = rawLines.iterator();
            //Set the current line being analyzed to a String, and use that to continue the loop.
            String line;
            //The entry for each Ent has the same structure, so we can loop through it.
            while (iterator.hasNext()) {
                //Get the first line holding UID, name and info
                line = iterator.next();


            }
            /*
            for (String line : rawLines) {

                //ok, line now has the text with UID, name and info
                //find the locations of the pound sign and at sign
                int pound = line.indexOf('#');
                int at = line.indexOf('@');
                int UID = Integer.parseInt(line.substring(0, pound));
                String name = line.substring(pound + 1, at);
                String info = line.substring(at + 1);
                //OK now deal with parents
                line = bufferedReader.readLine();
                int index = line.indexOf('$');
                ArrayList<Integer> parents = new ArrayList<>();
                if (index != -1) {
                    parents.add(Integer.parseInt(line.substring(1, index)));
                    int next = line.indexOf('$', index + 1);
                    while (next > index) {
                        parents.add(Integer.parseInt(line.substring(index + 1, next)));
                        index = next;
                        next = line.indexOf('$', index + 1);
                    }
                }
                //And now children
                line = bufferedReader.readLine();
                index = line.indexOf('$');
                ArrayList<Integer> children = new ArrayList<>();
                if (index != -1) {
                    children.add(Integer.parseInt(line.substring(1, index)));
                    int next = line.indexOf('$', index + 1);
                    while (next > index) {
                        children.add(Integer.parseInt(line.substring(index + 1, next)));
                        index = next;
                        next = line.indexOf('$', index + 1);
                    }
                }
                //OK, now lets create that ent!
                Ent ent = new Ent(data, name, UID, info, parents, children);
                //if it's root, then it's special
                if (UID == 0)
                    data.replaceRoot(ent);
                else
                    data.loadEnt(ent);
            }
            */


        }


        return null;
    }

    /**
     * <p>Allows you to load a text file from a file given by a String.
     * Returns a List of Strings for each line. Here just for
     * if it is determined that Strings are the better approach over Paths.</p>
     */
    private static List<String> textFileToStringList(String path) {
        return textFileToStringList(Paths.get(path));
    }

    /**
     * <p>Loads a text file at the given location and returns a list
     * of Strings, separated by the newlines of the original file.</p>
     * <p><i>Does not check that the file exists.</i></p>
     */
    private static List<String> textFileToStringList(Path path) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e + "\nDatabase could not be loaded.");
        }
        return lines;
    }

    /**
     * <p>Saves a string to file. If you want to call a save of the ents data, call the Data.save()</p>
     * <p>Returns a String informing the user of if the save was successful.</p>
     */
    static String save(String fileContents, Path filePath) {
        //Create a string to catch any output to the user.
        String saveResults = "";

        boolean fileExists = Files.exists(filePath);

        try {
            PrintWriter writer = new PrintWriter(filePath.toFile(), "UTF-8");
            writer.print(fileContents);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            if (fileExists)
                saveResults += "";
        }
        return saveResults;
    }


}