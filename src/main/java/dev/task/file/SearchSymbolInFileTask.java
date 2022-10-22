package dev.task.file;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class SearchSymbolInFileTask implements Callable<Boolean> {

    private final String filename;
    private final char searchSymbol;
    private final Consumer<Character> onFind;

    public SearchSymbolInFileTask(String filename, char searchSymbol, Consumer<Character> onFind) {
        this.filename = filename;
        this.searchSymbol = searchSymbol;
        this.onFind = onFind;
    }

    @Override
    public Boolean call() {
        try(FileReader reader = new FileReader(filename)) {
            int readData = reader.read();
            while (readData != -1) {
                if (Thread.currentThread().isInterrupted()) {
                    reader.close();
                    return false;
                }
                if ((char) readData == searchSymbol) {
                    onFind.accept(searchSymbol);
                }
                readData = reader.read();
            }
            return true;
        } catch (IOException e) {
            System.out.println("File " + filename + " not found");
            return false;
        }
    }
}
