/*******************************************************************************
 * The GUI Ents Explorer helps you visually alter and view an Ents Database Hierarchy.
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

package net.openpatterns.ents.gui.explorer;

import net.openpatterns.ents.database_library.Ent;
import net.openpatterns.ents.gui.UI;
import net.openpatterns.ents.gui.ui_templates.ListElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>This is a CustomList specifically for displaying a list of Ents.</p>
 */
class RelationListPanel extends EntListPanel implements ActionListener {


    /**
     * Popup Menu options for a Parents List.
     */
    private static final String REMOVE_CHILD = "Remove from children (working)";
    /**
     * Popup Menu options for a Children List.
     */
    private static final String REMOVE_PARENT = "Remove from parents (working)";
    /**
     * The element which the current popup menu is for.
     */
    private ListElement popupElement;


    /**
     * No special initialization yet.
     */
    RelationListPanel(EntExplorerPanel explorerPanel, ListType type) {
        super(explorerPanel, type);
        populateContextMenu();
    }


    private void populateContextMenu() {
        if (type == ListType.PARENTS) {
            addPopupMenuItem(REMOVE_PARENT);
        } else if (type == ListType.CHILDREN) {
            addPopupMenuItem(REMOVE_CHILD);
        }
    }

    private void addPopupMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        popupMenu.add(item);
        item.addActionListener(this);
    }


    /************************************************************************************************
     * Database Operations Handlers
     ************************************************************************************************/

    private void handleChildRemoval(EntElement element) {
        Ent entFocus = explorerPanel.getEntFocus();
        Ent entToRemove = element.getEnt();
        /* Does the Ent have any other parents besides entFocus? */
        if (entToRemove.getParents().size() == 1) {
        /* If the ent of focus is root, we can't remove it anyways. */
            //TODO hide that menu option automatically so this warning becomes unnecessary.
            if (entFocus.isRoot()) {
                JOptionPane.showMessageDialog(this, "\"" + entToRemove.getName() + "\" has root as its only parent.\nCan't remove.");
            } else {
                if (1 == UI.confirmDialog(this, "\"" + entToRemove.getName() + "\" has only one parent.\nRemove and set only parent as root?", "Remove")) {
                    Ent.delink(entFocus, entToRemove);
                    listModel.removeElement(element);
                    entToRemove.data.getRoot().addChild(entToRemove);
                }
            }
        } else {
            /* Ent will not become an orphan. Still confirm removal, as this is not likely a common action. */
            if (1 == UI.confirmDialog(this, "Confirm removal of \"" + entToRemove.getName() + "\" from the children of \"" + entFocus.getName() + "\".", "Confirm")) {
                Ent.delink(entFocus, entToRemove);
                listModel.removeElement(element);
            }
        }
    }

    private void handleParentRemoval(EntElement element) {
        Ent entFocus = explorerPanel.getEntFocus();
        Ent entToRemove = element.getEnt();
        /* Does the Ent have any other parents besides the one being removed? */
        if (entFocus.getParents().size() == 1) {
        /* If the ent to remove is root, we can't remove it anyways. */
            //TODO hide that menu option automatically so this warning becomes unnecessary.
            if (entFocus.isRoot()) {
                JOptionPane.showMessageDialog(this, "\"" + entToRemove.getName() + "\" has root as its only parent.\nCan't remove.");
            } else {
                if (1 == UI.confirmDialog(this, "\"" + entFocus.getName() + "\" has only one parent.\nRemove it and set only parent as root?", "Remove")) {
                    Ent.delink(entToRemove, entFocus);
                    listModel.removeElement(element);
                    entToRemove.data.getRoot().addChild(entFocus);
                }
            }
        } else {
            /* Ent will not become an orphan. Still confirm removal, as this is not likely a common action. */
            if (1 == UI.confirmDialog(this, "Confirm removal of \"" + entToRemove.getName() + "\" from the parents of \"" + entFocus.getName() + "\".", "Confirm")) {
                Ent.delink(entToRemove, entFocus);
                listModel.removeElement(element);
            }
        }
    }


    /************************************************************************************************
     * Overridden Methods
     ************************************************************************************************/

    /**
     * Called when an EntElement has been right clicked.
     * @param element The element that was clicked.
     * @param point The location of the click.
     */
    @Override
    protected void onEntRightClicked(ListElement element, Point point) {
        popupMenu.show(list, (int) point.getX(), (int) point.getY());
        popupElement = element;
    }

    /**
     * Listen for Popup menu events.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        /* Make sure it was a JMenuItem that was clicked. */
        if (e.getSource() instanceof JMenuItem && popupElement instanceof EntElement) {
            /* Which item was clicked? */
            String text = ((JMenuItem) e.getSource()).getText();
            switch (text) {
                case REMOVE_CHILD:
                    handleChildRemoval((EntElement) popupElement);
                    break;
                case REMOVE_PARENT:
                    handleParentRemoval((EntElement) popupElement);
                    break;
                default:
                    break;
            }
        }

    }

}
