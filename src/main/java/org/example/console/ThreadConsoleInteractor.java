package org.example.console;

import org.example.integration.IntegralSolver;
import org.example.thread.ConsoleThreadController;
import org.example.thread.ThreadController;

import java.util.Scanner;

public class ThreadConsoleInteractor {

    private final ThreadController threadController = new ConsoleThreadController();

    public void run() {
        while(true) {
            System.out.print("\n> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] splitted = input.split(" ");

            if (!(1 <= splitted.length && splitted.length <= 2)) {
                System.out.println("Invalid syntax");
                continue;
            }

            if (splitted.length == 1) {
                if (splitted[0].equals("exit")) {
                    threadController.stopAll();
                    break;
                } else {
                    if (!splitted[0].equals("")) {
                        System.out.println("Invalid syntax or unknown command");
                    }
                    continue;
                }
            }

            int value;
            try {
                value = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid parameter value");
                continue;
            }

            switch (splitted[0]) {
                case "start": {
                    Runnable task = () -> {
                        try {
                            long startTime = System.currentTimeMillis();
                            double result = solveIntegralFromTask(value);
                            long finishTime = System.currentTimeMillis();

                            System.out.println("\n------------------------------");
                            System.out.println("Result: " + result);
                            System.out.println("Time: " + (finishTime - startTime) + " ms");
                            System.out.println("------------------------------\n");
                            System.out.print("> ");
                        } catch (InterruptedException ignored) {
                        }
                    };
                    threadController.start(task);
                    break;
                }
                case "stop": {
                    threadController.stop(value);
                    break;
                }
                case "await": {
                    threadController.await(value);
                    break;
                }
                default: {
                    System.out.println("Unknown command");
                }
            }
        }
    }

    private double solveIntegralFromTask(int exactNumbers) throws InterruptedException {
        IntegralSolver integralSolver = new IntegralSolver.Builder()
                .function(x -> Math.sin(x) * x)
                .lowerLimit(0)
                .upperLimit(1)
                .exactNumbers(exactNumbers)
                .build();

        return integralSolver.solveIntegral();
    }

}
