package net.openpatterns.ents.stream;

import net.openpatterns.ents.database_library.Database;

/**
 * <p></p>
 */
public class Driver {


    public static void main(String[] args) {
        StreamCLI cli = new StreamCLI();
        Stream stream = new Stream(cli, Database.generateExampleDatabase(cli));
        stream.begin();
    }

}
