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

package net.openpatterns.ents.gui.ui_templates;

import javax.swing.*;
import java.awt.*;

/**
 * <p>This class holds information on what an element of a CustomJList actually represents. It is held by
 * the ListModel as the "value" component.</p>
 * <p>It could represent something, or call a designated action.</p>
 */
public abstract class ListElement {

    /**
     * <p>The types of element this is.</p>
     * <p>ABSTRACT is something which has a special function to be determined via subclasses.</p>
     * <p>ACTION means this element will call a given function.</p>
     */
    protected final static int ABSTRACT = 0, ACTION = 1;
    /**
     * The CustomJList this is displayed in.
     */
    protected CustomListPanel list;
    /**
     * Does this element represent something like an Ent or does it perform a given action?
     */
    private int type;
    /**
     * Stores whether or not the element is being hovered over with the mouse
     */
    private boolean isHoveredOver;
    /**
     * A popup menu to be shown if the item is right-clicked.
     */
    private PopupMenu popupMenu;


    /**
     * Initializes the ListElement Object to carry an Ent.
     */
    protected ListElement(int type, CustomListPanel list) {
        this.type = type;
        this.list = list;
    }


    /**************************************************************************
     * Class Methods
     **************************************************************************/

    /**
     * Returns the type of this element
     */
    int getType() {
        return type;
    }

    /**
     * Sets that this element is being hovered over
     */
    public void setHovering(boolean hovering) {
        isHoveredOver = hovering;
    }

    /**
     * Returns whether this element is being hovered over
     */
    public boolean isHoveredOver() {
        return isHoveredOver;
    }

    //TODO Assess the best way to accommodate both EntElements and ActionElements


    /***************************************************************************
     * Abstract Methods
     ***************************************************************************/

    /**
     * The function called when the UI component representing this element is selected.
     */
    public abstract void onLeftClicked();

    public abstract void onRightClicked();

    /**
     * Called when the element is pressed, but the mouse not yet released.
     */
    public abstract void onPressed();


    /**
     * Returns the text which should be displayed by the JLabel representing this element.
     */
    public abstract String getText();

    /**
     * Creates a JLabel to represent this Ent
     */
    public abstract JLabel generateLabel(int index, boolean isSelected, boolean cellHasFocus);


}
