package dev.task.file;

public class AppRunner {
    public static void main(String[] args) {
        SymbolInFilesSearcher searcher = new SymbolInFilesSearcher(System.out::println);
        searcher.addFile("file1.txt");
        searcher.addFile("file2.txt");
        searcher.addFile("file3.txt");
        searcher.search('a', 10);
    }

}
