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
import net.openpatterns.ents.database_library.EntsUserInterface;
import net.openpatterns.ents.gui.launcher.Launcher;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;

/**
 * <p>Class which handles creation and logic relating to the GUI Explorer.</p>
 * <p>Creates the ExplorerFrame and helps it, keeping UI code in there.</p>
 */
public class Explorer implements EntsUserInterface, WindowListener {

    /**
     * The ExplorerFrame which is the main window of this Explorer instance.
     */
    private ExplorerMainFrame mainFrame;
    /**
     * A dummy JFrame to use as the parent for JOptionDialogs and file choosers.
     */
    private JFrame dummyFrame = new JFrame();
    /**
     * The Launcher which manages the Explorer windows.
     */
    private Launcher launcher;

    /**
     * </p>Initialize this Explorer instance.<p>
     * <p>Before we open the ExplorerFrame we first need to know what database
     * we are exploring.</p>
     * <p>If there are saved settings, see if there was a recent database to open.
     * If not, ask the user to choose from recent databases or to choose a new file,
     * create a new one, or open the example database that is hard coded in
     * ents.database_library.Database</p>
     */
    public Explorer(Launcher launcher) {
        this.launcher = launcher;
    }

    /**
     * Sets the MainFrame to be visible.
     */
    public void setMainFrameVisible(boolean visible) {
        mainFrame.setVisible(visible);
    }

    @Override
    public Path chooseSaveFileLocation(String databaseName) {
        return null;
    }

    @Override
    public void displayMessage(String message) {

    }

    @Override
    public void saveSuccessful(Database database) {

    }

    @Override
    public void setDatabase(Database database) {
        mainFrame = new ExplorerMainFrame(database);
        mainFrame.addWindowListener(this);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * The MainFrame has likely closed, so inform the launcher.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        Object source = e.getSource();
        if (source == mainFrame) {
            launcher.onExplorerClose(this);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
