package dev.task.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class SymbolInFilesSearcher {

    private final Consumer<Boolean> onFinish;
    private final Collection<String> filenames;
    private ExecutorService executorService;

    private int symbolsCounter = 0;
    private boolean found = false;

    public SymbolInFilesSearcher(Consumer<Boolean> onFinish) {
        this.onFinish = onFinish;
        this.filenames = new HashSet<>();
    }

    public void addFile(String filename) {
        this.filenames.add(filename);
    }

    public void search(char searchSymbol, int amount) {
        this.executorService = Executors.newFixedThreadPool(this.filenames.size());

        Consumer<Character> onFind = (Character symbol) -> {
            System.out.println(symbol);
            incrementUntil(amount);
        };

        Collection<Future<Void>> futures = new ArrayList<>();
        filenames.forEach(filename -> {
            SearchSymbolInFileTask searchTask = new SearchSymbolInFileTask(filename, searchSymbol, onFind);
            futures.add(executorService.submit(searchTask));
        });

        futures.forEach(future -> {
            try {
                future.get(10, TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                System.out.println(e.getMessage());
            }
        });
        executorService.shutdownNow();
        onFinish.accept(found);
    }

    private synchronized void incrementUntil(int amount) {
        symbolsCounter++;
        if (symbolsCounter == amount) {
            found = true;
            executorService.shutdownNow();
        }
    }

}
