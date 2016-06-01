package net.openpatterns.ents.moot;

import net.openpatterns.ents.database_library.Ent;

import javax.swing.*;

/**
 * <p></p>
 */
public class ETextArea extends JTextArea {


    private Ent entFocus;


    ETextArea() {
    }


    ETextArea setEntFocus(Ent newFocus) {
        this.entFocus = newFocus;
        return this;
    }


}
