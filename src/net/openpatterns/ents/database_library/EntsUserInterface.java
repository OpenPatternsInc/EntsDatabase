/*******************************************************************************
 * The Ents Database Library provides access to functions,
 * analysis and implementations of a Taxonomical Hierarchy.
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

package net.openpatterns.ents.database_library;

import java.nio.file.Path;

/**
 * <p>This interface needs to be implemented by Objects calling Database functions.</p>
 * <p>This ensures that the Object can fulfill certain tasks like choosing a save
 * location for a new database.</p>
 * <p>We may want other types of interfaces later, but for now all interaction with
 * databases will be via users, so this is somewhat appropriate.</p>
 */
public interface EntsUserInterface {

    /**
     * Ask the user to choose a location and filename to save an Ents database.
     * @param databaseName The name of the Ents Database to be saved.
     * @return The path to the desired file.
     */
    Path chooseSaveFileLocation(String databaseName);

    /**
     * <p>Displays a message to the user.</p>
     * <p>There may be messages in quick succession, so they all need to be
     * displayed in order.</p>
     */
    void displayMessage(String message);

    /**
     * <p>Called when the saving of a database was successful.</p>
     */
    void saveSuccessful(Database database);

    /**
     * Sets the Database of this user interface.
     */
    void setDatabase(Database database);

}
