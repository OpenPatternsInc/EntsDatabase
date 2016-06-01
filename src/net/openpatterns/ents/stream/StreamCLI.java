package net.openpatterns.ents.stream;

import net.openpatterns.ents.database_library.Database;
import net.openpatterns.ents.database_library.EntsUserInterface;

import java.nio.file.Path;

/**
 * <p></p>
 */
public class StreamCLI implements StreamInterface, EntsUserInterface {

    Stream stream;
    Database database;

    StreamCLI() {


    }


    @Override
    public void i(String text) {

        println(text);

    }


    private void print(String text) {
        System.out.print(text);
    }

    private void println(String text) {
        System.out.println(text);
    }

    @Override
    public Path chooseSaveFileLocation(String databaseName) {
        return null;
        //TODO implement this part
    }

    @Override
    public void displayMessage(String message) {
        i(message);
    }

    @Override
    public void saveSuccessful(Database database) {
        i("Save Successful.");
    }

    @Override
    public void setDatabase(Database database) {
        this.database = database;
    }
}
