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

import net.openpatterns.ents.database_library.Database;
import net.openpatterns.ents.database_library.Ent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//TODO Remove the need for the TitleBar, so we can have greater control over the theme.

/**
 * The main frame used to explore Ents! Call this to bring up the primary UI.
 */
class ExplorerMainFrame extends JFrame implements ComponentListener, ActionListener, KeyListener {

    private JMenuBar menuBar;
    private JMenu fileMenu, helpMenu, entMenu;
    /** File MenuItems. */
    private JMenuItem saveDataItem;
    /** Help MenuItems. */
    private JMenuItem shortcutsItem;
    /** Ent MenuItems. */
    private JMenuItem newEntItem, deleteEntItem;

    private ExplorerMainPanel mainPanel;
    private Database data;

    ExplorerMainFrame(Database data) {
        this.data = data;
        setTitle("Ents");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(910, 410);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        menuBar = new JMenuBar();
        /* File Menu */
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        saveDataItem = new JMenuItem("Save (not working)");
        saveDataItem.addActionListener(this);
        //setup ctrl-s as shortcut to save
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        saveDataItem.setAccelerator(key);
        fileMenu.add(saveDataItem);
        /* Ent Menu */
        entMenu = new JMenu("Ent");
        menuBar.add(entMenu);
        newEntItem = new JMenuItem("New Ent (not working)");
        newEntItem.addActionListener(this);
        entMenu.add(newEntItem);
        deleteEntItem = new JMenuItem("Delete Ent (working)");
        deleteEntItem.addActionListener(this);
        entMenu.add(deleteEntItem);
        /* Help Menu */
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        shortcutsItem = new JMenuItem("Shortcuts (not working)");
        shortcutsItem.addActionListener(this);
        helpMenu.add(shortcutsItem);
        //add the menuBar
        setJMenuBar(menuBar);


        mainPanel = new ExplorerMainPanel(data, this);
        Ent root = data.getRoot();
        mainPanel.setEntFocus(root);

        addComponentListener(this);
        addKeyListener(this);

        add(mainPanel);

        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, true), null);

        setVisible(false);

        /** Create the static InfoWindow to be called later by other classes. */
        //InfoWindow.initializeWindow();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (e.getSource() == this) {
            //
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
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == saveDataItem) {
            JOptionPane.showMessageDialog(this, data.save());
        } else if (source == shortcutsItem) {
            //show shortcut tips
        } else if (source == newEntItem) {
            mainPanel.handleNewEnt();
        } else if (source == deleteEntItem) {
            mainPanel.handleDeleteEnt();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
