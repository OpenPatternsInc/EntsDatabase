package net.openpatterns.ents.moot;

import net.openpatterns.ents.database_library.Ent;

import java.util.ArrayList;

/**
 * <p>Holds the variables which determine what is displayed and how.</p>
 */
public class PanelVariables {

    private Ent entOfFocus;
    private ArrayList<Ent> entGroup;


    /***********************
     * C-C-Combo functions *
     ***********************/

    /**
     * Set the Ent whose information is being displayed.
     * @param newFocus The new ent of focus.
     * @return The same PanelVariables instance for chaining changes.
     */
    PanelVariables setEntFocus(Ent newFocus) {
        entOfFocus = newFocus;
        return this;
    }

    /**
     * Sets the EntGroup to be listed.
     * @param newEntGroup The new group of ents to be listed in this panel.
     * @return The same PanelVariables instance for chaining changes.
     */
    PanelVariables setEntGroup(ArrayList<Ent> newEntGroup) {
        entGroup = newEntGroup;
        return this;
    }

}
