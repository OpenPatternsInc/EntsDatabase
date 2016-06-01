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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>This class holds the various data like lists of Ents.</p>
 * <p>It is sent around so that all classes that need to access
 * it can</p>
 */
public class Database {

    /**
     * An arrayList of every Ent in the system. No reason to have it be a Map now.
     */
    ArrayList<Ent> ents;
    /**
     * The user interface object which is handling the management of this Database instance.
     */
    EntsUserInterface userInterface;
    /**
     * A map of unique Ent names to the Ent objects themselves.
     * Used to find the Ent quickly with a given name.
     */
    private Map<String, Ent> nameMap;
    /**
     * A map of the unique ID's to the Ent object it represents.
     * Useful to have unique names and UIDs if names are ever changed.
     */
    private Map<Integer, Ent> UIDMap;
    /**
     * Random number generator for stuff like UIDs
     */
    private Random rand;
    /**
     * The root ent which represents the category of "everything" and so contains all other Ents are its descendants.
     */
    private Ent root;

    /**
     * The name this database goes by.
     */
    private String name;
    /**
     * The path to where this database is being saved.
     */
    private Path savePath;


    //TODO Check for integrity of tree occasionally when idle in a new thread. Seems reasonable.

    /**
     * <p>Initialize an empty database with only root.</p>
     * <p>Sets the EntsUserInterface which will handle interaction between the database and the user.</p>
     */
    public Database(EntsUserInterface userInterface) {
        this.userInterface = userInterface;
        /* Must initialize the ents arrayList and Maps nameMap...*/
        ents = new ArrayList<>();
        nameMap = new HashMap<>();
        UIDMap = new HashMap<>();
        // initialize random number generator
        rand = new Random();
        //initialize root
        Ent r = new Ent(this);
        r.makeRoot();
        root = r;
        //add root to lists
        ents.add(root);
        nameMap.put(root.getName(), root);
        UIDMap.put(root.getUID(), root);
    }

    /**
     * Generates an example database with a number of Ents.
     */
    public static Database generateExampleDatabase(EntsUserInterface ui) {
        Database database = new Database(ui);
        database.setName("Example Database");

        /************************************************
         *  Create a bunch of Ents and connect them up. *
         ************************************************/
        database.addNewEnt("Living Organisms");
        /** Some animals */
        database.addNewEnt("Animals");
        database.getEntByName("Living Organisms").addChild(database.getEntByName("Animals"));
        database.addNewEnt("Fungus");
        database.getEntByName("Living Organisms").addChild(database.getEntByName("Fungus"));
        database.addNewEnt("Plants");
        database.getEntByName("Living Organisms").addChild(database.getEntByName("Plants"));

        database.addNewEnt("Mammals");
        database.getEntByName("Animals").addChild(database.getEntByName("Mammals"));
        database.addNewEnt("Lizards");
        database.getEntByName("Animals").addChild(database.getEntByName("Lizards"));
        database.addNewEnt("Marsupials");
        database.getEntByName("Animals").addChild(database.getEntByName("Marsupials"));

        database.addNewEnt("Land Dwellers");
        database.getEntByName("Animals").addChild(database.getEntByName("Land Dwellers"));
        database.addNewEnt("Aquatic Dwellers");
        database.getEntByName("Animals").addChild(database.getEntByName("Aquatic Dwellers"));

        database.addNewEnt("Humans");
        database.getEntByName("Mammals").addChild(database.getEntByName("Humans"));
        database.getEntByName("Land Dwellers").addChild(database.getEntByName("Humans"));

        database.addNewEnt("Iguanas");
        database.getEntByName("Lizards").addChild(database.getEntByName("Iguanas"));
        database.getEntByName("Land Dwellers").addChild(database.getEntByName("Iguanas"));

        database.addNewEnt("Whales");
        database.getEntByName("Mammals").addChild(database.getEntByName("Whales"));
        database.getEntByName("Aquatic Dwellers").addChild(database.getEntByName("Whales"));

        /** Some Molecules */
        database.addNewEnt("Molecules");

        database.addNewEnt("Fatty Acids");
        database.getEntByName("Molecules").addChild(database.getEntByName("Fatty Acids"));

        database.addNewEnt("Unsaturated Fatty Acids");
        database.getEntByName("Fatty Acids").addChild(database.getEntByName("Unsaturated Fatty Acids"));

        database.addNewEnt("Polyunsaturated Fatty Acids");
        database.getEntByName("Unsaturated Fatty Acids").addChild(database.getEntByName("Polyunsaturated Fatty Acids"));

        database.addNewEnt("Omega 3s");
        database.getEntByName("Polyunsaturated Fatty Acids").addChild(database.getEntByName("Omega 3s"));

        database.addNewEnt("Omega 6s");
        database.getEntByName("Polyunsaturated Fatty Acids").addChild(database.getEntByName("Omega 6s"));

        return database;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    //TODO Add in illegal characters

    /**
     * Adds a custom Root ent which is loaded from file and replaces the old root
     */
    void replaceRoot(Ent ent) {
        /* reinitialize the ents arrayList and Maps nameMap...*/
        ents = new ArrayList<>();
        nameMap = new HashMap<>();
        UIDMap = new HashMap<>();
        //set it up
        ent.makeRoot();
        root = ent;
        //add root to lists
        ents.add(root);
        nameMap.put(root.getName(), root);
        UIDMap.put(root.getUID(), root);
    }

    /**
     * Is the given name valid and not taken?
     */
    public boolean isNameValidAndFree(String name) {
        return name != null && name.length() > 3 && !nameMap.containsKey(name);
    }

    /**
     * Adds an Ent to the various lists, but does not check its uniqueness.
     * @param ent
     */
    public void addEnt(Ent ent) {
        ents.add(ent);
        nameMap.put(ent.getName(), ent);
        UIDMap.put(ent.getUID(), ent);
        ent.addParent(root);
    }

    //TODO optimize the crap outa this. Just fine for now though.

    /**
     * Adds a new Ent with the given name to the system.
     * @param entName The name of the new Ent to add.
     * @return Returns 1 if successful, 0 if name taken, -1 if other.
     */
    public int addNewEnt(String entName) {
        Ent newEnt = new Ent(this);
        int result = newEnt.setName(entName);
        if (result == 1) {
            //not taken, add it to the lists and maps
            ents.add(newEnt);
            nameMap.put(newEnt.getName(), newEnt);
            UIDMap.put(newEnt.getUID(), newEnt);
            //set root as its only parent for now
            newEnt.addParent(root);
            return 1;
        } else if (result == 0) {
            return 0;
        }
        return -1;
    }

    /**
     * Adds a new Ent object that has probably been loaded via a file.
     */
    void loadEnt(Ent ent) {
        ents.add(ent);
        nameMap.put(ent.getName(), ent);
        UIDMap.put(ent.getUID(), ent);
    }

    /**
     * Removes an Ent with the given name. Does not check for tree consistency after removal.
     * @return 1 if successful, 0 if error.
     */
    public int removeEnt(String name) {
        if (nameMap.containsKey(name)) {
            Ent ent = nameMap.get(name);
            ents.remove(ent);
            nameMap.remove(name);
            UIDMap.remove(ent.getUID());
            return 1;
        }
        return 0;
    }

    /**
     * Removes the given Ent object from all lists.
     * @return 1 if successful, 0 if not.
     */
    public int removeEnt(Ent ent) {
        if (ents.contains(ent)) {
            //ok, lets remove it from the lists
            ents.remove(ent);
            nameMap.remove(ent.getName());
            UIDMap.remove(ent.getUID());
            //and now remove references to it from its parents and children
            ent.delink();
            return 1;
        } else
            return 0;
    }

    /**
     * @return The map of unique names to Ent objects.
     */
    Map<String, Ent> getNameMap() {
        return nameMap;
    }

    /**
     * @return The map of unique UIDs to Ent objects.
     */
    Map<Integer, Ent> getUIDMap() {
        return UIDMap;
    }

    /**
     * Just generates random integers. We want them positive because having a
     * minus sign in the number that identifies an Ent is awkward.
     * @return A random integer.
     */
    int getRandomInt() {
        return rand.nextInt(Integer.MAX_VALUE);
    }

    //TODO Allow argument to specify where to save.

    /**
     * Returns the root Ent.
     */
    public Ent getRoot() {
        return root;
    }

    /**
     * <p>Saves all relevant information of the Ent hierarchy to file.</p>
     * <p>Can not yet choose where to save to.</p>
     * <p>Should the object calling this should handle successful saves and errors as well.</p>
     */
    public String save() {

        StringBuilder builder = new StringBuilder();

        for (Ent ent : ents)
            ent.saveToString(builder);

        DatabaseIO.save(builder.toString(), null);

        return "Should have saved to disk. No double check done however...";
    }

    /**
     * Returns an ArrayList of the Ents in the data.
     */
    ArrayList<Ent> getEnts() {
        return ents;
    }

    /**
     * Loads a database from file. As of now it's just a static location.
     */
    public void loadFile() {
        DatabaseIO.loadData(this);
    }

    /**
     * Returns an Ent of the given name.
     */
    public Ent getEntByName(String name) {
        return nameMap.get(name);
    }

    /**
     * Returns whether an Ent with the given name exists.
     */
    public boolean doesEntExist(String name) {
        return nameMap.containsKey(name);
    }


}
