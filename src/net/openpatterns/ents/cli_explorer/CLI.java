/*******************************************************************************
 * The CLI Ents Explorer helps you alter and view an
 * Ents Database Hierarchy from the command line.
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

package net.openpatterns.ents.cli_explorer;

import net.openpatterns.ents.database_library.Database;
import net.openpatterns.ents.database_library.Ent;

import java.util.ArrayList;
import java.util.Scanner;

/**<p>This is a class which uses a Scanner to make a command line interface
 * in order to edit an Ents database.</p>
 * <p>You can use it to add new Ents, change their names, set children and
 * parents, or analyze the tree, etc.</p>
 * <h1>Usage</h1>
 * <p>Type command and hit enter key.</p>
 * */
public class CLI {

    /** The shared Data class holding info for the current Ents database.*/
    private Database data;

    private Scanner scanner;

    public CLI(Database data) {
        this.data = data;
        scanner = new Scanner(System.in);
        init();
    }

    /**Initializes the command functions and waits for input.*/
    private void init() {
        generalInput();
    }

    private void generalInput() {
        System.out.print("\n\n> ");
        String text = scanner.nextLine();
        //should we exit?
        if (text.equals("exit"))
            return;
        if (text.equals("add")) {
            addNewEnt();
        } else if (text.equals("help") || text.equals("h")) {
            displayHelp();
        }
        //after checks for commands, see if the command is an ent's name
        else if (data.getNameMap().containsKey(text)) {
            //name found, lets open the entEditor
            entEditor(text);
        }
        generalInput();
    }

    /**
     * Lists commands and how to use them.
     */
    private void displayHelp() {
        System.out.println("Commands:\n\t>add\n\t>remove\n\t>exit");
    }

    /**
     * <p>Edit a certain Ent.</p>
     * <p>You will see the command prompt as "name>"</p>
     */
    private void entEditor(String name) {
        Ent ent = data.getNameMap().get(name);
        System.out.print("\n" + name + "> ");
        String command = scanner.nextLine();
        if (command.equals("exit")) {
            return;
        } else if (command.equals("remove")) {
            //lets try and see what happens
            int result = handleEntRemoval(name);
            if (result == 1) {
                //ent was removed, we can exit the editor
                return;
            } else if (result == 0) {
                //ent not removed, stay in editor for now
                entEditor(name);
            }
        } else if (command.equals("children") || command.equals("kids") || command.equals("k") || command.equals("c")) {

            //display all direct children
            ArrayList<Ent> children = ent.getChildren();
            if (children != null && children.size() > 0) {
                System.out.println("Direct children:");
                for (Ent e: children) {
                    System.out.println('\t' + e.getName());
                }
            } else {
                System.out.println("No children.");
            }
            entEditor(name);

        } else if (command.equals("parents") || command.equals("p")) {

            //display all direct parents
            ArrayList<Ent> parents = ent.getParents();
            if (parents != null && parents.size() > 0) {
                System.out.println("Direct parents:");
                for (Ent e: parents) {
                    System.out.println('\t' + e.getName());
                }
            } else {
                System.out.println("No parents... something went wrong...");
            }
            entEditor(name);

        } else if (command.equals("add child") || command.equals("add c")) {

            //ok so we will ask for the name of the child to add.
            System.out.print("Child's name: ");
            String child = scanner.nextLine();
            String result = ent.addChild(data.getNameMap().get(child));
            if (result.equals("success")) {
                System.out.println("Added.");
            } else {
                System.out.println("Could not be added: " + result);
            }
            entEditor(name);

        } else if (command.equals("add parent") || command.equals("add p")) {

            //ok so we will ask for the name of the parent to add.
            System.out.print("Parent's name: ");
            String parent = scanner.nextLine();
            String result = ent.addParent(data.getNameMap().get(parent));
            if (result.equals("success")) {
                System.out.println("Added.");
            } else {
                System.out.println("Could not be added: " + result);
            }
            entEditor(name);
        } else if (command.equals("remove child") || command.equals("remove c")) {

            //removing a child isn't that problematic. If it becomes orphaned then we just put it under root.
            System.out.print("Child's name: ");
            String child = scanner.nextLine();
            //is this a valid ent?
            Ent c = data.getNameMap().get(child);
            if (c == null) {
                System.out.println("No Ent with that name exists.");
                entEditor(name);
            } else if (ent.getChildren().contains(c)) {
                //confirm removal just in case
                if (confirm()) {
                    String result = ent.removeChild(data.getNameMap().get(child));
                    if (result.equals("success"))
                        System.out.println("Removed.");
                    else
                        System.out.println("Error: " + result);
                } else {
                    System.out.println("Not removed.");
                }
            } else {
                System.out.println("\'" + c.getName() + "\" is not a child of \"" + name + "\".");
            }
            entEditor(name);

        } else if (command.equals("remove parent") || command.equals("remove p")) {

            //if we remove a parent and the Ent then becomes parentless,
            // put it as root's child because of the logic of the taxonomical hierarchy.
            System.out.print("Parent's name: ");
            String parent = scanner.nextLine();
            //is this a valid ent?
            Ent p = data.getNameMap().get(parent);
            if (p == null) {
                System.out.println("No Ent with that name exists.");
                entEditor(name);
            } else if (ent.getParents().contains(p)) {
                //confirm removal just in case
                if (confirm()) {
                    String result = ent.removeParent(data.getNameMap().get(parent));
                    if (result.equals("success"))
                        System.out.println("Removed.");
                    else
                        System.out.println("Error: " + result);
                } else {
                    System.out.println("Not removed.");
                }
            } else {
                System.out.println("\'" + p.getName() + "\" is not a parent of \"" + name + "\".");
            }
            entEditor(name);

        } else
            entEditor(name);

    }

    /** Dialog to do with removing an Ent from the database. Should main tree consistency and stuff, but not yet. */
    private int handleEntRemoval(String name) {
        if (confirm()) {
            //well, they're pretty sure about it...
            int result = data.removeEnt(name);
            if (result == 1) {
                System.out.println("\"" + name + "\" was removed successfully");
                return 1;
            } else if (result == 0) {
                System.out.println("\"" + name + "\" could not be removed...");
                return 0;
            }
        } else {
            System.out.println("OK, \"" + name + "\" was not removed.");
            return 0;
        }
        System.out.println("Strange error... \"" + name + "\" was not removed...");
        return 0;




    }


    private void addNewEnt() {
        //looks like we're making a new Ent, but we need a name first
        System.out.println("Creating new Ent.");
        String name = null;
        //can we add it to the data?
        int result = 0;
        //TODO check for exit inside loop
        while (result == 0) {
            System.out.print("Name: ");
            name = scanner.nextLine();
            if (name.equals("exit")) {
                System.out.println("Ent not added.");
                return;
            }
            result = data.addNewEnt(name);
            if (result == 0) {
                System.out.println("Name is either invalid or already used. Please try again.");
            }
        }
        if (result == 1) {
            //valid name, it was added
            System.out.println("New Ent added.");
            entEditor(name);
        } else {
            System.out.println("Error adding new Ent...");
        }

    }


    /** Asks "Comfirm? y/n" and only the response of "y" returns true. */
    private boolean confirm() {
        System.out.print("Confirm? y/n: ");
        if (scanner.nextLine().equals("y"))
            return true;
        return false;
    }

}
