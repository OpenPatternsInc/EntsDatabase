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

package net.openpatterns.ents.gui.ui_templates;

import net.openpatterns.ents.database_library.Ent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


//TODO Not 100% sure this window can't be moused over. The user should use right-click for all actions.

/**
 * <p>This frame is used to display info when hovering over an item, like an Ent in a JList.</p>
 * <p>It disappears when the user moves away from the object of interest.</p>
 */
public class InfoWindow extends JWindow implements MouseListener {

    /**
     * Error Message to display when no text is set
     */
    private static final String ERROR_NO_TEXT = "Error: No text yet...";
    /**
     * Keep a static reference to one window, to be used so we don't need to create a bunch of instances of one.
     */
    private static InfoWindow infoWindow;
    /**
     * The JLabel to hold basic Strings should the need arise.
     */
    private JTextArea textArea;

    /**
     * Initializes the static InfoWindow for use by multiple classes.
     * Can be private because we only want to call it once and save the garbage collection some effort.
     */
    private InfoWindow() {
        //setSize(200, 200);
        textArea = new JTextArea(ERROR_NO_TEXT, 10, 30);
        textArea.setFocusable(false);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JPanel panel = new JPanel();
        panel.add(textArea);
        add(panel);
        setFocusable(false);
        setVisible(false);
        pack();
    }

    /**
     * Sets what element the InfoWindow is giving info for.
     */
    static void setListElement(ListElement element) {
        infoWindow.textArea.setText(element.getText());
    }

    /**
     * Sets that an Ent's info should be displayed in the infoWindow.
     */
    static void setEnt(Ent ent) {
        infoWindow.textArea.setText(ent.getInfo());
    }

    /**
     * Clears the window... nobody should see a clearList window, but better than a wrong window...
     */
    static void clear() {
        infoWindow.textArea.setText(ERROR_NO_TEXT);
    }

    /**
     * Sets the static InfoWindow and sets it's JFrame parent
     */
    static void initializeWindow() {
        infoWindow = new InfoWindow();
        infoWindow.addMouseListener(infoWindow);
    }

    /**
     * Reinitializes the window. There seems to be a way that the window will remain visible when the user
     * is able to get their mouse over it. So, just dispose of it and remake it, because setting it invisible
     * doesn't seem to work...
     */
    private static void reinitialize() {
        infoWindow.dispose();
        infoWindow = new InfoWindow();
        infoWindow.addMouseListener(infoWindow);
    }

    //TODO Currently causes problems when it would display under the mouse. Need to display it instead somewhere else.

    /**
     * Shows the static InfoWindow at the given location relative to the parent frame
     */
    static void showWindow(Point point, Component relativeComponent) {
        //TODO Draw location seems to depend on size of InfoWindow... it should not.
        infoWindow.setLocation((int) (relativeComponent.getX() + point.getX() + 20), (int) (point.getY() + relativeComponent.getY()));
        infoWindow.setVisible(true);
    }

    /**
     * Hides the static window.
     */
    static void hideWindow() {
        infoWindow.setVisible(false);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        reinitialize();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
