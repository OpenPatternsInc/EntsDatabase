package net.openpatterns.ents.moot;

import net.openpatterns.ents.database_library.Database;
import net.openpatterns.ents.database_library.Ent;

import javax.swing.*;

/**
 * An EPanel displays a main Ent at the top of a JList of related Ents, with a description of it below.
 */
public class EPanel extends JPanel {

    /** The properties which determine what to display and how to do it. */
    PanelVariables panelVariables;
    /** The Ent of focus in this particular panel. */
    private Ent ent;
    /** The EList displaying the ents in the group. */
    private EList list;
    /** The TextArea to display the info about the ent of focus. */
    private JTextArea textArea;
    /** The Database instance used in this EntBranch. */
    private Database database;


    /**
     * Creates a blank EPanel for use with others in an EntBranch.
     */
    EPanel(Database databaseOfBranch) {
        database = databaseOfBranch;
        /* Initialize variables */
        list = new EList();
        textArea = new JTextArea();
        panelVariables = new PanelVariables();
    }


}
