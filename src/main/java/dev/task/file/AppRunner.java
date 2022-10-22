package dev.task.file;

import java.util.function.Consumer;

public class AppRunner {
    public static void main(String[] args) {
        Consumer<Boolean> onFinish = System.out::println;
        SymbolsInTwoFilesSearcher searcher = new SymbolsInTwoFilesSearcher(onFinish);
        searcher.search('a');
    }

}
