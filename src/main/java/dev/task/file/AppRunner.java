package dev.task.file;

import java.util.function.Consumer;

public class AppRunner {
    public static void main(String[] args) {
        Consumer<Boolean> onFinish = System.out::println;
        SymbolInFilesSearcher searcher = new SymbolInFilesSearcher(onFinish);
        searcher.addFile("file1.txt");
        searcher.addFile("file2.txt");
        searcher.addFile("file3.txt");
        searcher.search('a', 10);
    }

}
