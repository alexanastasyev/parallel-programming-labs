package dev.task.file;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class SymbolsInTwoFilesSearcher {

    private static final int SHOULD_FIND_AMOUNT = 10;
    private final Consumer<Boolean> onFinish;
    private int symbolsCounter = 0;
    private boolean found = false;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public SymbolsInTwoFilesSearcher(Consumer<Boolean> onFinish) {
        this.onFinish = onFinish;
    }

    public void search(char searchSymbol) {
        Consumer<Character> onFind = (Character symbol) -> {
            System.out.println(symbol);
            increment();
        };

        SearchSymbolInFileTask searchTask1 = new SearchSymbolInFileTask("file1.txt", searchSymbol, onFind);
        SearchSymbolInFileTask searchTask2 = new SearchSymbolInFileTask("file2.txt", searchSymbol, onFind);

        Future<Boolean> future1 = executorService.submit(searchTask1);
        Future<Boolean> future2 = executorService.submit(searchTask2);

        try {
            future1.get(10, TimeUnit.MINUTES);
            future2.get(10, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println(e.getMessage());
        } finally {
            executorService.shutdown();
            if (!found) {
                onFinish.accept(false);
            }
        }
    }

    private synchronized void increment() {
        symbolsCounter++;
        if (symbolsCounter == SHOULD_FIND_AMOUNT) {
            found = true;
            onFinish.accept(true);
            executorService.shutdownNow();
        }
    }

}
