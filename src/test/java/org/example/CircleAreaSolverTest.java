package org.example;

import org.junit.Assert;
import org.junit.Test;

public class CircleAreaSolverTest {
    private static final double DELTA = 1E-6;
    private final CircleAreaSolver circleAreaSolver = new CircleAreaSolver();

    @Test
    public void testRadius10() {
        double radius = circleAreaSolver.calculateCircleArea(10);
        Assert.assertEquals(10 * 10 * Math.PI, radius, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRadius() {
        circleAreaSolver.calculateCircleArea(-5);
    }

    @Test
    public void testRadius0() {
        double radius = circleAreaSolver.calculateCircleArea(0);
        Assert.assertEquals(0, radius, 0);
    }
}
