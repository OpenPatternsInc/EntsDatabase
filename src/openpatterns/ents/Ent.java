        /*
        The Ents database library provides access to a Taxonomical Hierarchy.
        Copyright (C) 2016  Jason Stockwell

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

/**<p>This is the basic class which represents an Entity.</p>
 * <p>An Ent represents a category of things, and may have subcategories of
 * additional Ent classes.</p>
 * <p>This class holds all the the methods used by Ents to manipulate themselves
 * like editing variables or adding parents or children.</p>
 * <p>This is intended to be well documented.</p>
 * */

public class Ent {

    /**Unique identifier for this particular entity.*/
    private int UID;
    /**The name of this ent to be displayed in the viewer.
     * Maybe should be unique to avoid confusion?*/
    private String name;
    /**ArrayLists of the ent's direct parents and children.*/
    private ArrayList<Ent> parents, children;
    /**Data object which holds all information about the current system. Does making this static create a security risk?*/
    private Data data;
    /** Is this Ent root? Maybe inefficient to store this as boolean in each Ent, but OK for now */
    private boolean isRoot;
    /** Allow the root Ent to be accessed statically via the Ent class. Maybe that will be helpful? Prolly not. */
    protected static Ent root;


    /**Generates a new basic Ent object.
     * Does not add it to the Data class.*/
    public Ent(Data data) {
        /* Must initialize arrayLists before use.*/
        parents = new ArrayList<>();
        children = new ArrayList<>();
        /* Set the Data object */
        this.data = data;
        /* Generate a new UID */
        int id = data.getRandomInt();
        //id 0 is reserved for root. might make things easier?
        while(data.getUIDMap().containsKey(id) | id == 0)
            id = data.getRandomInt();
        UID = id;
        isRoot = false;
    }

    /**<p>Makes this Ent the root ent which represents the category of "everything" and so contains
     * all other categories. Makes sure there isn't already another root, which could screw stuff up.</p>
     * <p><i>Don't call this unless you want to mess up everything lol</i></p>
     */
    protected void makeRoot() {
        if (data.getRoot() != null)
            return;
        isRoot = true;
        UID = 0;
        name = "root";
        root = this;
    }

    //TODO Make sure CLI input does not get confused with Ent names
    //TODO Make special method for adding names without checking if they are valid (for fast loading from file)
    /**<p>Sets the name of this specific Ent if it's not already used by another
     * Ent in the system.</p>
     * <p>Name is not set if already taken.</p>
     *
     * @param newName The desired new name.
     * @return 1 if set successfully, 0 if already taken.
     */
    protected int setName(String newName) {
        if (newName == null || newName.length() < 3)
            return 0;
        if (!data.getNameMap().containsKey(newName)) {
            name = newName;
            return 1;
        } else
            return 0;
    }

    /**<i>Do not use this to change the Ent's name, use XXX instead.</i>
     * @return The Ent's name as a String.
     */
    public String getName() {
        return name;
    }

    /**<i>Do not use this to change the Ent's UID,
     * in fact don't try to change it at all there's no reason to.</i>
     * @return The Ent's unique ID number as an int.
     */
    public int getUID() {
        return UID;
    }

    /**<p></p>Adds an Ent to this Ent's list of children unless it's there already. Also automatically
     * adds itself to the parent's list of children.</p>
     * <p><i>Checks logical consistency, may not be fast.</i></p>
     * @param newParent The parent to be added
     * @return Returns either "success" or the error when adding.
     */
    protected String addParent(Ent newParent) {
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
        for (Ent a: ancestors)
                removeParent(a);
        //ok now add it
        parents.add(newParent);
        newParent.forceAddChild(this);
        return "success";
    }

    /**<p>Adds an Ent to this Ent's list of children unless it's there already. Also automatically
     * adds itself to the child's list of parents.</p>
     * <p><i>Checks logical consistency, may not be fast.</i></p>
     * @param newChild The parent to be added
     * @return Returns either "success" or the error when adding.
     */
    protected String addChild(Ent newChild) {
        /* If newParent isn't already a parent, add it, or else don't.
        * Not sure if we actually need to check this.*/
        if (parents.contains(newChild)) {
            return "\"" + newChild.getName() + "\" is already a parent.";
        } else if (getAncestors().contains(newChild)) {
            return "\"" + newChild.getName() + "\" is already an ancestor.";
        } else if (getChildren().contains(newChild)) {
            return "\"" + newChild.getName() + "\" is already a child.";
        } else if (getDescendants().contains(newChild)) {
            return "\"" + newChild.getName() + "\" is already a descendant.";
        }
        //we got here... so it must be valid
        //if this Ent's children include any of the new child's descendants, we can remove them to maintain consistency of the hierarchy
        ArrayList<Ent> descs = newChild.getDescendants();
        for (Ent c: children)
            if (descs.contains(c))
                forceRemoveChild(c);
        //TODO Check if there are any inconsistencies with exclusivity
        //ok now add the new child
        children.add(newChild);
        newChild.forceAddParent(this);
        return "success";

    }

    /** Adds an ent to the children list without checking anything. */
    protected void forceAddChild(Ent child) {
        children.add(child);
    }

    /** Adds an ent to the parents list without checking anything. */
    protected void forceAddParent(Ent parent) {
        parents.add(parent);
    }

    /** Removes an ent from the children list without checking anything. */
    protected void forceRemoveChild(Ent child) {
        children.remove(child);
    }

    /** Removes an ent from the parents list without checking anything. */
    protected void forceRemoveParent(Ent parent) {
        parents.remove(parent);
    }

    /** <p>Tries to remove an Ent from the Children List.</p>
     *  <p>If the Ent to be removed would become an orphan Ent we must make it a child of root.
     *  That is handled when the to-be-removed Ent's removeParent() method is called.</p>
     */
    protected String removeChild(Ent child) {
        if (children.contains(child)) {
            children.remove(child);
            return "success";
        } else {
            return "Could not remove because \"" + child.getName() + "\" was not a child.";
        }
    }

    /** <p>Tries to remove an Ent from the parent list.</p>
     * <p>If the Ent isn't actually a parent then it won't be done.</p>
     * <p>If this Ent would then become parentless, make root its parent.</p>
     */
    protected String removeParent(Ent parent) {
        if (parents.contains(parent)) {
            parents.remove(parent);
            if (parents.size() == 0)
                addParent(root);
            return "success";
        } else {
            return "Could not remove because \"" + parent.getName() + "\" was not a parent.";
        }
    }

    /**<p>Returns the parents of this Ent as an arrayList. Try not to change
     * them unless you mean to because this is the original list.</p>
     * @return The arrayList of parents.
     */
    protected ArrayList<Ent> getParents() {
        return parents;
    }

    /**<p>Returns the children of this Ent as an arrayList. Try not to change
     * them unless you mean to because this is the original list.</p>
     * @return The arrayList of children.
     */
    protected ArrayList<Ent> getChildren() {
        return children;
    }

    /** <p>Returns all descendants of this Ent (it's children and their children etc.)</p>
     * <p>Doesn't check to make sure of logical consistency, that's done at specific times and order is maintained
     * by only performing certain transformations to the tree.</p>
     */
    protected ArrayList<Ent> getDescendants() {
        ArrayList<Ent> list = new ArrayList<>();
        //add each child and each of their children
        for (Ent e: children) {
            list.add(e);
            ArrayList<Ent> grand = e.getDescendants();
            for (Ent g: grand)
                if (!list.contains(g))
                    list.add(g);
        }
        return list;
    }

    /** <p>Returns all ancestors of this Ent (it's parents and their parents etc.)</p>
     * <p>Doesn't check to make sure of logical consistency, that's done at specific times and order is maintained
     * by only performing certain transformations to the tree.</p>
     */
    protected ArrayList<Ent> getAncestors() {
        ArrayList<Ent> list = new ArrayList<>();
        //add each parent and each of their parents
        for (Ent e: parents) {
            list.add(e);
            ArrayList<Ent> grand = e.getAncestors();
            for (Ent g: grand)
                if (!list.contains(g))
                    list.add(g);
        }
        return list;
    }

    /**Is this Ent root?*/
    protected boolean isRoot() {
        return isRoot;
    }
}
