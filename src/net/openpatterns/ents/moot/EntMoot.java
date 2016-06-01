package net.openpatterns.ents.moot;

import javax.swing.*;
import java.util.List;

/**
 * <p>Many JPanels, each with information about a given Ent.</p>
 * <p>The top part of each panel lists the ents within a group.</p>
 * <p>The list contains every </p>
 * <p></p>Each panel has  exploring a branch of a tree.</p>
 * <p>Each </p>
 * <h2>Main Controller Class</h2>
 */
public class EntMoot {

    /**
     * The main JFrame.
     */
    public JFrame frame;
    /**
     * The EntPanels representing each Ent in the branch.
     */
    public List<EntPanel> entPanelList;
    /**
     * <p>The number of theoretical cores on the System machine.</p>
     * <p>This knowledge optimizes the efficiency of </p>
     */
    public int cores = 10;
    /**
     * <p>A calculated optimal Thread amount.</p>
     */
    public int threadScalar = 1;
    /**
     * The ThreadPoolExecutor holds all functional archetypes within different threads and at different priority.
     */
    //ThreadPoolExecutor threadPoolExecutor;

    /**
     * An Array of the Threads of the program.
     */
    Thread[] threads;

    /**
     * <p>This thread controls the Graphical User Interface and responds to actions of the user.</p>
     * <p>It is the top priority because responsiveness is most important</p>
     */
    Thread thread_ui;

    /**
     * <p>This thread controls </p>
     */


    public EntMoot() {
        //threadPoolExecutor = new ThreadPoolExecutor();
    }


}
