/*******************************************************************************
 * The Ents Database Library provides access to functions,
 * analysis and implementations of a Taxonomical Hierarchy.
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

package net.openpatterns.ents.database_library;

/**
 * <p>This Object type is returned when someone requests that a database file is loaded and
 * used to generate a Database Object.</p>
 * <p>It contains any explanations for any problems that arose.</p>
 */
public class DatabaseLoadReturnElement {

    /**
     * Flags indicating result of load.
     */
    public final static int LOAD_SUCCESSFUL = 0, ERROR_LOADING = 1, BLANK_FILE = 2, INCOMPATABLE_FILE = 3;
    /**
     * The success flag for this particular element.
     */
    private int successFlag;
    /**
     * The Database Object generated from the file. Only populated if LOAD_SUCCESSFUL.
     */
    private Database database;
    /**
     * An explanation if necessary, giving the user some idea as to what went wrong.
     * To be displayed in a GUI, rather than a error message sent to console.
     */
    private String explanation;

    /**
     * Initiate the return element with the success flag, Database Object, if any, and a String
     * explanation if necessary.
     */
    DatabaseLoadReturnElement(int successFlag, Database database, String explanation) {
        this.successFlag = successFlag;
        this.database = database;
        this.explanation = explanation;
    }


    public int getSuccessFlag() {
        return successFlag;
    }

    public Database getDatabase() {
        return database;
    }

    public String getExplanation() {
        return explanation;
    }


}
