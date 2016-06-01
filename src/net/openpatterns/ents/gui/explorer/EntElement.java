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

package net.openpatterns.ents.gui.explorer;

import net.openpatterns.ents.database_library.Ent;
import net.openpatterns.ents.gui.ui_templates.ListElement;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * <p>The value of an element of a ListModel or JList, which holds an Ent.</p>
 * <p>Displays the Ent's name and makes sure it's the right color.</p>
 */
class EntElement extends ListElement {

    /**
     * Colors to indicate relationships between Ents.
     */
    //static final Color CHILD = Color.green, PARENT = Color.blue, EXCLUSION = Color.red, ANCESTOR = Color.cyan, DESCENDANT = Color.yellow;
    /**
     * The background color of a selected Ent, which essentially means one which has been pressed down.
     */
    private final Color SELECTED_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    /**
     * The list for an EntElement will be an EntListPanel.
     */
    protected EntListPanel list;
    /**
     * The Ent that this element represents.
     */
    private Ent ent;
    /**
     * Borders which indicate things like mouseover.
     */
    private Border normalBorder, hoverBorder;


    EntElement(Ent ent, EntListPanel list) {
        super(ListElement.ABSTRACT, null);
        this.list = list;
        this.ent = ent;
        normalBorder = BorderFactory.createLineBorder(Color.gray, 1);
        hoverBorder = BorderFactory.createLineBorder(Color.blue, 1);
    }

    /**********************************************************************
     * Class Methods
     **********************************************************************/

    /**
     *
     */

    /**********************************************************************
     * Implement Abstract Methods
     **********************************************************************/

    public Ent getEnt() {
        return ent;
    }

    /**
     * <p>The Element has been clicked.</p>
     * <p>When this element is selected, we should move the Ent this element represents to focus.</p>
     * <p>For the EntElement, the only argument will be the parent EntPanel, in order to call its setEntFocus().</p>
     */
    @Override
    public void onLeftClicked() {
        System.out.println("Ent: \"" + ent.getName() + "\" has been clicked.");

        list.explorerPanel.setEntFocus(ent);
    }

    @Override
    public void onRightClicked() {
        System.out.println("Right click");
    }

    /**
     * Called when the element is pressed, but not released yet.
     */
    @Override
    public void onPressed() {

    }

    /**
     * Returns the name of the Ent as the text to display on the JLabel
     */
    @Override
    public String getText() {
        return ent.getInfo();
    }

    /**
     * Creates a JLabel with the Ent's name, etc.
     */
    @Override
    public JLabel generateLabel(int index, boolean isSelected, boolean cellHasFocus) {

        JLabel label = new JLabel(ent.getName());
        label.setOpaque(true);
        if (isHoveredOver())
            label.setBorder(hoverBorder);
        else
            label.setBorder(normalBorder);
        //selected?
        if (isSelected)
            label.setBackground(SELECTED_BACKGROUND_COLOR);
        return label;
    }
}
