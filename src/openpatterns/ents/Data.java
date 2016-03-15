/*
The Ents database library provides access to a Taxonomical Hierarchy.
Copyright (C) 2016  Jason Stockwell
www.openpatterns.net/#ents

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package openpatterns.ents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**<p>This class holds the various data like lists of Ents.</p>
 * <p>It is sent around so that all classes that need to access
 * it can</p>
 * */
public class Data {

    /** An arrayList of every Ent in the system. No reason to have it be a Map now. */
    private ArrayList<Ent> ents;
    /** A map of unique Ent names to the Ent objects themselves.
     * Used to find the Ent quickly with a given name.*/
    private Map<String, Ent> nameMap;
    /** A map of the unique ID's to the Ent object it represents.
     * Useful to have unique names and UIDs if names are ever changed. */
    private Map<Integer, Ent> UIDMap;
    /** Random number generator for stuff like UIDs */
    private Random rand;
    /** The root ent which represents the category of "everything" and so contains all other Ents are its descendants. */
    private Ent root;

    //TODO Check for integrity of tree when idle in new thread that can be paused when user is active



    public Data() {
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
     * Adds a new Ent with the given name to the system.
     * @param entName The name of the new Ent to add.
     * @return Returns 1 if successful, 0 if name taken, -1 if other.
     */
    protected int addNewEnt(String entName) {
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

    /** Removes an Ent with the given name. Does not check for tree consistency after removal.
     * @return 1 if successful, 0 if error.
     * */
    protected int removeEnt(String name) {
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
     * @return The map of unique names to Ent objects.
     */
    protected Map<String, Ent> getNameMap() {
        return nameMap;
    }

    /**
     * @return The map of unique UIDs to Ent objects.
     */
    protected Map<Integer, Ent> getUIDMap() {
        return UIDMap;
    }

    /**
     * Just generates random integers... could be positive, negative, whatever.
     * @return A random integer.
     */
    protected int getRandomInt() {
        return rand.nextInt();
    }

    /**Returns the root Ent. */
    protected Ent getRoot() {
        return root;
    }

}
