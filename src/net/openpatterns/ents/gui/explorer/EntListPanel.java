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
import net.openpatterns.ents.gui.ui_templates.CustomListPanel;
import net.openpatterns.ents.gui.ui_templates.ListElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * <p>A custom list specifically for Ents.</p>
 * <p>Pretty much a CustomList with the ability to set the elements
 * as an ArrayList of Ents.</p>
 */
class EntListPanel extends CustomListPanel implements ActionListener {

    /**
     * Popup Menu options.
     */
    private static final String TOGGLE = "Toggle";
    /**
     * The EntExplorerPanel this list is apart of.
     */
    public EntExplorerPanel explorerPanel;
    /**
     * Which type is this list? A list of parents or children.
     */
    public ListType type;
    /**
     * Constants to identify parents or children.
     */
    //public static final int PARENTS = 0, CHILDREN = 1, EXCLUSIONS = 2;
    /**
     * A popup menu for showing actions to perform.
     */
    JPopupMenu popupMenu;
    /**
     * No fancy constructor yet.
     */
    EntListPanel(EntExplorerPanel explorerPanel, ListType type) {
        this.explorerPanel = explorerPanel;
        this.type = type;
        popupMenu = new JPopupMenu();
    }

    /**
     * Set the ents which will be displayed in this list.
     * @param ents The ents themselves.
     */
    void setEnts(ArrayList<Ent> ents) {
        clearList();
        /* For each Ent we need to make an EntElement and add it to the ListModel */
        for (Ent ent : ents)
            listModel.addElement(new EntElement(ent, this));
    }

    /**
     * Called when an EntElement has been right clicked.
     * @param element The element that was clicked.
     * @param point   The location of the click.
     */
    protected void onEntRightClicked(ListElement element, Point point) {

    }

    /*****************************************************************************************
     * Custom Element Action Handlers
     *****************************************************************************************/

    /**
     * <p>To enable the custom functionality we need to listen for mouse presses.</p>
     * <p>When a list item is pressed, it should be "selected" and depress. If the mouse then
     * is released without moving, the action associated with that list element will be
     * performed. If the mouse is moved between pressing and releasing we will count that as
     * a drag.</p>
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //figure out which item was selected
        int index = locationToIndex(e.getPoint());
        //don't care if no element was pressed
        if (index == -1)
            return;
        //check what click type it is
        int button = e.getButton();
        int count = e.getClickCount();
        /* Get the element that was pressed */
        ListElement element = listModel.get(index);

        if (button == MouseEvent.BUTTON1) {
            //OK, so we have a left click even. How many clicks?
            if (count == 1) {
                //single left press
                //System.out.println("Single left press.");
                //Select the element
                element.onPressed();
                pressedElement = element;
                pressedPoint = e.getPoint();
            } else if (count == 2) {
                //double left click. dunno if this is possible.
                //System.out.println("Double left press.");
            }
        } else if (button == MouseEvent.BUTTON3) {
            //right button press. how many clicks
            if (count == 1) {
                //single right click
                //System.out.println("Single right press.");
                element.onPressed();
                pressedElement = element;
                pressedPoint = e.getPoint();
            } else if (count == 2) {
                //double right click. again, dunno if this is possible.
                //System.out.println("Double right press.");
            }
        }
    }


    /*****************************************************************************************
     * UI Event Handlers
     *****************************************************************************************/

    @Override
    public void mouseReleased(MouseEvent e) {

        /* If it was released very close to where it was pressed, call it a click. */
        if (pressedPoint.distance(e.getPoint()) < 5) {
            int button = e.getButton();
            if (button == MouseEvent.BUTTON1)
                pressedElement.onLeftClicked();
            else if (button == MouseEvent.BUTTON3)
                onEntRightClicked(pressedElement, e.getPoint());
        }
        list.clearSelection();
    }

    /**
     * When the mouse exits the JList we know to clear the
     * hovering element.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == list) {
            if (hoveringElement != null) {
                hoveringElement.setHovering(false);
                hoveringElement = null;
                list.clearSelection();
                repaint();
            }
        }
    }

    /**
     * If dragged after an element was pressed, this means reordering unless it was very slight.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        /* What element are we over now? */
        int index = locationToIndex(e.getPoint());
        /* If the element ...*/
        //TODO Finish this stuff.
    }

    /**
     * <p>The mouse has moved while no button is pressed, so lets determine if it is over
     * an element.</p>
     * <p>If hovering has changed, inform the ListElement directly.</p>
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        /* Is it above an element? */
        int index = locationToIndex(e.getPoint());
        /* If the index is -1 then we should clearList the hoveringElement if needed. */
        if (index == -1) {
            if (hoveringElement != null) {
                hoveringElement.setHovering(false);
                hoveringElement = null;
                //TODO Do we need to repaint the list here?
                repaint();
            }
            /* No more to be done. */
        } else {
            ListElement newHoveringElement = listModel.getElementAt(index);
            if (newHoveringElement != hoveringElement) {
                /* Hovering element has changed. */
                if (hoveringElement != null)
                    hoveringElement.setHovering(false);
                /* Save the hovering element. */
                hoveringElement = newHoveringElement;
                /* Inform it that it is now hovering. */
                newHoveringElement.setHovering(true);
                //TODO Repaint here?
                repaint();
            }
        }
    }

    /**
     * The types of lists this may be.
     */
    public enum ListType {
        PARENTS, CHILDREN, EXCLUSIONS
    }


}
