/*******************************************************************************
 * The Ents Database Library provides access to functions,
 * analysis and implementations of a Taxonomical Hierarchy.
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

package net.openpatterns.ents.database_library;

import java.util.ArrayList;

/**
 * <p>This is the basic class which represents an Entity.</p>
 * <p>An Ent represents a category of things, and may have subcategories of
 * additional Ent classes.</p>
 * <p>This class holds all the the methods used by Ents to manipulate themselves
 * like editing variables or adding parents or children.</p>
 * <p>This is intended to be well documented.</p>
 */
//TODO Plan out and determine what functions should be exposed publicly. Only allowing logically sound operations is a start.

public class Ent {

    //TODO Add support for the Exclusion groups. Because exclusions propagate down the hierarchy, this involves non-trivial logic.

    /**
     * Data object which holds all information about the current system. Does making this static create a security risk?
     */
    public Database data;
    /**
     * Unique identifier for this particular entity.
     */
    private int UID;
    /**
     * The name of this ent to be displayed in the viewer.
     * Maybe should be unique to avoid confusion?
     */
    private String name;
    /**
     * The description of this Ent.
     */
    private String info;
    /**
     * ArrayLists of the ent's direct parents and children.
     */
    private ArrayList<Ent> parents, children, exclusions;
    /**
     * ArrayLists of the ent's direct parents and children as their UIDs... for loading from file...
     */
    private ArrayList<Integer> ps, cs, es;
    //TODO Only allow programs to get root's properties, not the object itself. Prevent them from messing things up.
    /**
     * Is this Ent root? Maybe inefficient to store this as boolean in each Ent, but OK for now
     */
    private boolean isRoot;


    /**
     * Generates a new basic Ent object.
     * Does not add it to the Data class.
     */
    public Ent(Database data) {
        /* Must initialize arrayLists before use.*/
        parents = new ArrayList<>();
        children = new ArrayList<>();
        /* Set the Data object */
        this.data = data;
        /* Generate a new UID */
        int id = data.getRandomInt();
        //id 0 is reserved for root. might make things easier?
        while (data.getUIDMap().containsKey(id) || id == 0)
            id = data.getRandomInt();
        UID = id;
        isRoot = false;
        info = "No info yet...";
    }

    /**
     * Creates a new ent with the given details, these will be loaded from files, so do it exactly.
     */
    public Ent(Database data, String name, int UID, String info, ArrayList<Integer> ps, ArrayList<Integer> cs, ArrayList<Integer> es) {
        /* Must initialize arrayLists before use.*/
        parents = new ArrayList<>();
        children = new ArrayList<>();
        /* Set the Data object */
        this.data = data;
        //now set the values
        this.name = name;
        this.UID = UID;
        this.info = info;
        //set parents and children to be loaded later once everything has been added
        this.ps = ps;
        this.cs = cs;
    }

    /**
     * Delinks a parent and child pair.
     */
    public static void delink(Ent parent, Ent child) {
        parent.removeChild(child);
        child.removeParent(parent);
    }

    //TODO Make sure CLI input does not get confused with Ent names
    //TODO Make special method for adding names without checking if they are valid (for fast loading from file)

    /**
     * Links up a new parent and child. Doesn't check for hierarchical consistency.
     */
    public static void link(Ent parent, Ent child) {
        parent.addChild(child);
        child.addParent(parent);
    }

    /**
     * Sets the connections this ent has to other ents once they have all been loaded from file
     */
    void setConnections() {
        //parents
        for (int n : ps)
            parents.add(data.getUIDMap().get(n));
        //children
        for (int n : cs)
            children.add(data.getUIDMap().get(n));
        //exclusions
        for (int n : es)
            exclusions.add(data.getUIDMap().get(n));

    }

    /**
     * <p>Makes this Ent the root ent which represents the category of "everything" and so contains
     * all other categories. Makes sure there isn't already another root, which could screw stuff up.</p>
     * <p><i>Don't call this unless you want to mess up everything lol</i></p>
     */
    void makeRoot() {
        //Not checking this right now because it messes up loading from file
        /*
        if (data.getRoot() != null)
            return;
            */
        isRoot = true;
        UID = 0;
        name = "root";
    }

    /**
     * <p>Sets the name of this specific Ent if it's not already used by another
     * Ent in the system.</p>
     * <p>Name is not set if already taken.</p>
     * @param newName The desired new name.
     * @return 1 if set successfully, 0 if already taken.
     */
    public int setName(String newName) {
        if (newName == null || newName.length() < 4)
            return 0;
        if (!data.getNameMap().containsKey(newName)) {
            name = newName;
            return 1;
        } else
            return 0;
    }

    /**
     * <i>Do not use this to change the Ent's name, use XXX instead.</i>
     * @return The Ent's name as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * <i>Do not use this to change the Ent's UID,
     * in fact don't try to change it at all there's no reason to.</i>
     * @return The Ent's unique ID number as an int.
     */
    public int getUID() {
        return UID;
    }

    /**
     * <p></p>Adds an Ent to this Ent's list of children unless it's there already. Also automatically
     * adds itself to the parent's list of children.</p>
     * <p><i>Checks logical consistency, may not be fast.</i></p>
     * @param newParent The parent to be added
     * @return Returns either "success" or the error when adding.
     */
    public String addParent(Ent newParent) {
        /* If newParent isn't already a parent, add it, or else don't.
        * Not sure if we actually need to check this.*/
        if (parents.contains(newParent)) {
            return "\"" + newParent.getName() + "\" is already a parent.";
        } else if (getAncestors().contains(newParent)) {
            return "\"" + newParent.getName() + "\" is already an ancestor.";
        } else if (getChildren().contains(newParent)) {
            return "\"" + newParent.getName() + "\" is already a child.";
        } else if (getDescendants().contains(newParent)) {
            return "\"" + newParent.getName() + "\" is already a descendant.";
        }
        //ok seems valid to add it
        //remove all parents who are ancestors of the new parent. If this were not done, the logic of the hierarchy may become redundant.
        //TODO Notify the user when the tree is pruned like this, just so they know.
        ArrayList<Ent> ancestors = newParent.getAncestors();
        for (Ent a : ancestors)
            removeParent(a);
        //ok now add it
        parents.add(newParent);
        newParent.forceAddChild(this);
        return "success";
    }

    /**
     * <p>Adds an Ent to this Ent's list of children unless it's there already. Also automatically
     * adds itself to the child's list of parents.</p>
     * <p><i>Checks logical consistency, may not be fast.</i></p>
     * @param newChild The parent to be added
     * @return Returns either "success" or the error when adding.
     */
    public String addChild(Ent newChild) {
        //calculate ancestors for use later
        ArrayList<Ent> ancestors = getAncestors();
        /* If newParent isn't already a parent, add it, or else don't.
        * Not sure if we actually need to check this.*/
        if (parents.contains(newChild)) {
            return "\"" + newChild.getName() + "\" is already a parent.";
        } else if (ancestors.contains(newChild)) {
            return "\"" + newChild.getName() + "\" is already an ancestor.";
        } else if (getChildren().contains(newChild)) {
            return "\"" + newChild.getName() + "\" is already a child.";
        } else if (getDescendants().contains(newChild)) {
            return "\"" + newChild.getName() + "\" is already a descendant.";
        }
        //we got here... so it must be valid
        //if this Ent's children include any of the new child's descendants, we can remove them to maintain consistency of the hierarchy
        ArrayList<Ent> descs = newChild.getDescendants();
        for (Ent c : children)
            if (descs.contains(c))
                forceRemoveChild(c);
        //TODO Check if there are any inconsistencies with exclusivity

        //If this Ent shares any parents with the new child, remove the connection between the child and it's parent, as that is now redundant.
        ArrayList<Ent> cAncestors = newChild.getAncestors();
        for (Ent a : ancestors) {
            if (cAncestors.contains(a)) {
                newChild.forceRemoveParent(a);
                a.forceRemoveChild(newChild);
            }
        }

        //ok now add the new child
        children.add(newChild);
        newChild.forceAddParent(this);
        return "success";

    }

    /**
     * Adds an ent to the children list without checking anything.
     */
    private void forceAddChild(Ent child) {
        children.add(child);
    }

    /**
     * Adds an ent to the parents list without checking anything.
     */
    private void forceAddParent(Ent parent) {
        parents.add(parent);
    }

    //TODO Sanitize this method so the user doesn't screw stuff up

    /**
     * Adds an ent to the exclusion list without checking anything.
     */
    private void forceAddExclusion(Ent exclusion) {
        exclusions.add(exclusion);
    }

    //TODO Sanitize this as well. Don't want people saying monkeys have feathers. Not natural man, it's just not.

    /**
     * Removes an ent from the children list without checking anything.
     */
    private void forceRemoveChild(Ent child) {
        children.remove(child);
    }

    /**
     * Removes an ent from the parents list without checking anything.
     */
    private void forceRemoveParent(Ent parent) {
        parents.remove(parent);
    }

    /**
     * <p>Tries to remove an Ent from the Children List.</p>
     * <p>If the Ent to be removed would become an orphan Ent we must make it a child of root.
     * That is handled when the to-be-removed Ent's removeParent() method is called.</p>
     */
    public String removeChild(Ent child) {
        if (children.contains(child)) {
            children.remove(child);
            return "Successfully removed \"" + child.getName() + "\" from the children of \"" + name + "\".";
        } else {
            return "Could not remove because \"" + child.getName() + "\" was not a child.";
        }
    }

    /**
     * <p>Tries to remove an Ent from the parent list.</p>
     * <p>If the Ent isn't actually a parent then it won't be done.</p>
     * <p>If this Ent would then become parentless, make root its parent.</p>
     */
    public String removeParent(Ent parent) {
        if (parents.contains(parent)) {
            parents.remove(parent);
            if (parents.size() == 0)
                addParent(data.getRoot());
            return "success";
        } else {
            return "Could not remove because \"" + parent.getName() + "\" was not a parent.";
        }
    }

    /**
     * <p>Returns the parents of this Ent as an arrayList. Try not to change
     * them unless you mean to because this is the original list.</p>
     * @return The arrayList of parents.
     */
    public ArrayList<Ent> getParents() {
        return parents;
    }

    /**
     * <p>Returns the children of this Ent as an arrayList. Try not to change
     * them unless you mean to because this is the original list.</p>
     * @return The arrayList of children.
     */
    public ArrayList<Ent> getChildren() {
        return children;
    }

    /**
     * Returns an ArrayList of the direct exclusions to this group.
     */
    public ArrayList<Ent> getDirectExclusions() {
        return exclusions;
    }

    //TODO What are the chances of memory problems caused by this recursion on data sets of 10,000 ents?

    /**
     * <p>Returns all descendants of this Ent (it's children and their children etc.)</p>
     * <p>Doesn't check to make sure of logical consistency, that's done at specific times and order is maintained
     * by only performing certain transformations to the tree.</p>
     */
    ArrayList<Ent> getDescendants() {
        ArrayList<Ent> list = new ArrayList<>();
        //add each child and each of their children
        for (Ent e : children) {
            list.add(e);
            ArrayList<Ent> grand = e.getDescendants();
            for (Ent g : grand)
                if (!list.contains(g))
                    list.add(g);
        }
        return list;
    }

    /**
     * <p>Returns all ancestors of this Ent (it's parents and their parents etc.)</p>
     * <p>Doesn't check to make sure of logical consistency, that's done at specific times and order is maintained
     * by only performing certain transformations to the tree.</p>
     */
    ArrayList<Ent> getAncestors() {
        ArrayList<Ent> list = new ArrayList<>();
        //add each parent and each of their parents
        for (Ent e : parents) {
            list.add(e);
            ArrayList<Ent> grand = e.getAncestors();
            for (Ent g : grand)
                if (!list.contains(g))
                    list.add(g);
        }
        return list;
    }

    /**
     * <p>Returns an ArrayList of every single ent which is exclusive to this one, not just directly.
     * Probably a bit of computation.</p>
     * <p>To compile the list we first start with the Ents exclusive to this one, then include all of its descendants.
     * Then we do the same for each of this Ent's ancestors. Probably lots of room for optimization.</p>
     * <p><i>Currently may include multiple references to the same Ent, though that shouldn't be a problem for
     * the uses of this method, like simply checking that an Ent is not part of it.</i></p>
     */
    ArrayList<Ent> getAllExclusions() {
        ArrayList<Ent> results = new ArrayList<>();
        /* First add this Ent's direct exclusions and all of their descendants. */
        results.addAll(exclusions);
        for (Ent excluded : exclusions)
            results.addAll(excluded.getDescendants());
        /* Now do the same for all of this Ent's ancestors. */
        for (Ent ancestor : getAncestors()) {
            for (Ent excluded : ancestor.getDirectExclusions()) {
                results.add(excluded);
                results.addAll(excluded.getDescendants());
            }
        }
        return results;
    }

    /**
     * <p>Removes all references to this Ent object from its parents and children.</p>
     * <p>Exercise proper awareness when using this.</p>
     */
    void delink() {
        for (Ent p : parents) {
            p.forceRemoveChild(this);
        }
        for (Ent c : children) {
            c.forceRemoveChild(this);
        }
    }

    /**
     * Completely deletes this Ent and all references to it.
     */
    public void delete() {
        /* Remove this Ent from the Children lists of its parents. */
        for (Ent p : parents)
            p.removeChild(this);
        /* Remove this Ent from the Parents lists of its children. If the former child would become an orphan,
         * set it as a child of root at least. */
        for (Ent c : children) {
            if (c.getParents().size() == 1) {
                link(data.getRoot(), c);
            }
            c.removeParent(this);
        }
        /* Remove it from the Database HashMaps. */
        data.removeEnt(this);
    }

    /**
     * Is this Ent root?
     */
    public boolean isRoot() {
        return isRoot;
    }

    /**
     * Returns the current info
     */
    public String getInfo() {
        return info;
    }

    /**
     * Sets the info/description for this ent.
     */
    void setInfo(String info) {
        this.info = info;
    }

    /**
     * Make the ent generate it's own save file by appending to a string builder
     */
    void saveToString(StringBuilder b) {
        //string builder should be fast enough /s
        //first add the info specific to this ent
        b.append('\n').append(UID).append('#').append(name).append("@#").append(info).append("@#");
        //now do all of the connections, starting with parents
        b.append("\np");
        for (Ent p : parents)
            b.append(p.getUID()).append('$');
        //children
        b.append("\nc");
        for (Ent c : children)
            b.append(c.getUID()).append('$');
        //blank line for exclusions support later
        b.append("\ne");
        //ok now the StringBuilder will be used for other ents as well, and build it all at once.
    }
}
