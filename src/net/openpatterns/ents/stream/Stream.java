package net.openpatterns.ents.stream;

import net.openpatterns.ents.database_library.Database;

/**
 * <p></p>
 */
public class Stream {

    private StreamInterface ui;

    private Database database;

    public Stream(StreamInterface ui, Database database) {
        this.ui = ui;
        this.database = database;
    }

    /**
     * Begins the stream.
     */
    public void begin() {

        /*
         * Inform the user of the initial options.
         */

        ui.i("Beginning stream of " + database.getName());

        /*
         * The user can either be in command or upkeep mode.
         * The command mode allows the user to perform direct operations.
         * Upkeep mode asks the user questions to gather Ent relationship information. This information allows
         * for further questioning to to be optimized.
         */


    }

    Database getDatabase() {
        return database;
    }

    void setDatabase(Database database) {
        this.database = database;
    }


}
