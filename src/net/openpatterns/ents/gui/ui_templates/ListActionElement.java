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

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p></p>
 */
public class ListActionElement extends ListElement {

    /**
     * The method to be called when the element is selected.
     */
    private Method method;
    /**
     * The instance of an Object which the method will be invoked under.
     */
    private Object instance;
    /**
     * The text to be displayed in the JList.
     */
    private String text;

    /**
     * Initializes the ListElement Object to carry an Ent.
     * @param list The list this element is displayed in.
     */
    public ListActionElement(CustomListPanel list, String text, Method method, Object instance) {
        super(ListElement.ACTION, list);
        this.text = text;
        this.method = method;
        this.instance = instance;
    }

    @Override
    public void onLeftClicked() {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            //e.printStackTrace();
            System.out.println(e);
            e.getCause().printStackTrace();
        }
    }

    @Override
    public void onRightClicked() {

    }

    @Override
    public void onPressed() {
        //Nothing yet
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public JLabel generateLabel(int index, boolean isSelected, boolean cellHasFocus) {
        return new JLabel(text);
    }
}
