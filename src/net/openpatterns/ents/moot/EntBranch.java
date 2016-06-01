package net.openpatterns.ents.moot;

import net.openpatterns.ents.database_library.Database;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A Graphical User Interface for exploring a number of Entities along a Taxonomical Hierarchy.</p>
 * <p>Like a tree branch it has nodes along where it splits.</p>
 */
public class EntBranch {

    /** The main graphical frame of the EntBranch GUI. Carries the variable number of panels. */
    private JFrame frame = new JFrame();

    private List<EPanel> panels;
    private Database database;
    private int panelNumber;

    public EntBranch(Database databaseOfBranch) {
        System.out.println(frame.toString());
        panels = new ArrayList<>();

        /* Start with 2 panels, root and direct children. */
        setPanelNumber(2);

    }


    void setPanelNumber(int newPanelNumber) {

        if (panelNumber == 0) {
            for (int x = 0; x < newPanelNumber; x++) {
                panels.add(new EPanel(database));
            }
        }

        panelNumber = newPanelNumber;

    }


}
