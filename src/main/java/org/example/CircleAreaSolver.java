package org.example;

public class CircleAreaSolver {
    public double calculateCircleArea(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException();
        }
        return Math.PI * radius * radius;
    }
}
