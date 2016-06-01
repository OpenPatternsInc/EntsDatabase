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
import net.openpatterns.ents.gui.ui_templates.InfoWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by jstockwell on 3/20/16.
 */
class EntInfoPanel extends JPanel implements MouseListener, ComponentListener {

    private JTextArea infoArea;
    private Ent displayedEnt;
    private JScrollBar infoScrollBar;
    private JScrollPane infoScrollPane;
    private InfoWindow infoWindow;

    EntInfoPanel() {
        infoArea = new JTextArea();
        infoArea.setWrapStyleWord(true);
        infoArea.setLineWrap(true);
        infoScrollPane = new JScrollPane(infoArea);
        infoScrollBar = new JScrollBar(JScrollBar.VERTICAL);


        setLayout(new BorderLayout());
        add(infoScrollPane);

        addComponentListener(this);

        /* Make an empty border to make it look nice. */
        setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        setVisible(true);
    }

    /**
     * Sets the Ent which this panel is displaying.
     */
    protected void setEnt(Ent ent) {
        displayedEnt = ent;
        infoArea.setText(ent.getInfo());
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

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void componentResized(ComponentEvent e) {

        if (e.getSource() == this) {
            Dimension dim = new Dimension(getWidth(), getHeight());
            //*
            infoScrollPane.setPreferredSize(dim);
            infoScrollPane.setMinimumSize(dim);
            infoScrollPane.setMaximumSize(dim);
            //*/


            /*
            infoArea.setPreferredSize(dim);
            infoArea.setMinimumSize(dim);
            infoArea.setMaximumSize(dim);
            //*/

            revalidate();
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

    public void setInfoWindow(InfoWindow infoWindow) {
        this.infoWindow = infoWindow;
    }
}
