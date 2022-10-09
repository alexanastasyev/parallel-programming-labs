package org.example;

import org.example.console.ThreadConsoleInteractor;

public class Runner {
    public static void main(String[] args) {
        ThreadConsoleInteractor interactor = new ThreadConsoleInteractor();
        interactor.run();
    }
}
