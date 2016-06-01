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
import java.awt.event.*;
import java.util.ArrayList;

/**
 * A JList which only counts clicks directly on it's options, not also empty space beneath them.
 */
public class CustomListPanel extends JPanel implements MouseListener, ActionListener, ComponentListener, MouseMotionListener, InfoWindowMethods {
    

    /**
     * The JList to hold the ListElements.
     */
    protected JList<ListElement> list;
    /**
     * listModel can be updated later and the list will reflect this automatically.
     */
    protected DefaultListModel<ListElement> listModel;
    /**
     * The element which is being hovered over at the moment.
     */
    protected ListElement hoveringElement;
    /**
     * The InfoWindow used for a popup
     */
    //protected InfoWindow infoWindow;
    /**
     * An element which has been pressed but not yet released. Used to later determine if a "click" was made, or whether to "drag".
     */
    protected ListElement pressedElement;
    /**
     * The point where the mouse has been pressed on an element. Used to differentiate between clicks and drags.
     */
    protected Point pressedPoint;

    JScrollPane scrollPane;
    
    /**
     * Initialize the List
     */
    public CustomListPanel() {
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.addMouseListener(this);
        list.addMouseMotionListener(this);
        list.setCellRenderer(new CustomListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        /* JScrollPane to hold the JList and make it scrollable. */
        scrollPane = new JScrollPane(list);
        scrollPane.add(new JScrollBar(JScrollBar.VERTICAL));
        /* Border Layout automatically resizes stuff? */
        this.setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        /* Listen for the panel to be resized so that we can resize the scrollPane. */
        addComponentListener(this);
        /* Make a border around this JPanel to make it look nice */
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    /*****************************************************************************
     * Custom Methods
     *****************************************************************************/

    /**
     * Add a ListElement to the List.
     */
    public int addElement(ListElement newElement) {
        if (listModel.contains(newElement))
            return 0;
        listModel.addElement(newElement);
        return 1;
    }

    /**
     * Adds an arrayList of elements.
     */
    public void addElements(ArrayList<ListElement> elementsToAdd) {
        for (ListElement newElement : elementsToAdd)
            if (!listModel.contains(newElement))
                listModel.addElement(newElement);
    }

    /**
     * Clear all elements from the list.
     */
    public void clearList() {
        listModel.clear();
    }

    /**
     * A redone locationToIndex() method which doesn't count clicking the empty space beneath the last entry as
     * counting for that. If we click empty space we don't want that to register a click on an element. Just a
     * design preference.
     * @param point The relative point within the JList which has been clicked.
     */
    public int locationToIndex(Point point) {
        int index = list.locationToIndex(point);
        /** Check to see if the mouse is actually over no element but instead below the last. */
        if (listModel.getSize() - index > 0) {
            //it returned the last element, so lets check if the mouse is actually over it
            Rectangle rect = list.getCellBounds(index, index);
            if (rect != null && !rect.contains(point))
                return -1;
        }
        return index;
    }

    /*******************************************************************************
     * Events
     *
     * When we determine a relevant event has occurred to a ListElement, tell the
     * list element directly, it should know what to do.
     *******************************************************************************/

    /**
     * The method called when a popup menu has been clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void componentResized(ComponentEvent e) {

        if (e.getSource() == this) {
            scrollPane.setPreferredSize(new Dimension(this.getHeight() - 10, this.getHeight() - 10));
            scrollPane.setMaximumSize(new Dimension(this.getHeight() - 10, this.getHeight() - 10));
            scrollPane.setMinimumSize(new Dimension(this.getHeight() - 10, this.getHeight() - 10));
        }

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

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
        if (button == MouseEvent.BUTTON1) {
            //OK, so we have a left click even. How many clicks?
            if (count == 1) {
                //single left press
                //System.out.println("Single left press.");
                //Select the element
                listModel.get(index).onLeftClicked();
            } else if (count == 2) {
                //double left click. dunno if this is possible.
                //System.out.println("Double left press.");
            }
        } else if (button == MouseEvent.BUTTON3) {
            //right button press. how many clicks
            if (count == 1) {
                //single right click
                //System.out.println("Single right press.");
            } else if (count == 2) {
                //double right click. again, dunno if this is possible.
                //System.out.println("Double right press.");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        list.clearSelection();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

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

    @Override
    public void mouseDragged(MouseEvent e) {

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

    @Override
    public void setInfoWindow(InfoWindow window) {

    }

    @Override
    public void showInfoWindow(String text, int x, int y) {

    }
}
