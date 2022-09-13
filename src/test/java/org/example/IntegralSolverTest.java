package org.example;

import org.example.integration.IntegralSolver;
import org.example.integration.InvalidLimitsException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;

public class IntegralSolverTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testResearchFunction() {
        Function<Double, Double> function = x -> Math.sin(x) * x;
        int lowerLimit = 0;
        int upperLimit = 1;
        int exactNumbers = 6;

        IntegralSolver integralSolver = new IntegralSolver();
        double result = integralSolver.solveIntegral(function, lowerLimit, upperLimit, exactNumbers);

        Assert.assertEquals(0.301168678939757, result, 1E-6);
    }

    @Test
    public void testSquareFunction() {
        Function<Double, Double> function = x -> x * x;
        int lowerLimit = 0;
        int upperLimit = 1;
        int exactNumbers = 6;

        IntegralSolver integralSolver = new IntegralSolver();
        double result = integralSolver.solveIntegral(function, lowerLimit, upperLimit, exactNumbers);

        Assert.assertEquals(0.333333333333, result, 1E-6);
    }

    @Test
    public void testResearchFunctionWithHighLimits() {
        Function<Double, Double> function = x -> Math.sin(x) * x;
        int lowerLimit = -100;
        int upperLimit = 100;
        int exactNumbers = 6;

        IntegralSolver integralSolver = new IntegralSolver();
        double result = integralSolver.solveIntegral(function, lowerLimit, upperLimit, exactNumbers);

        Assert.assertEquals(-173.476505739756, result, 1E-3);
    }

    @Test
    public void testConstantFunction() {
        Function<Double, Double> function = x -> 123.456;
        int lowerLimit = 0;
        int upperLimit = 1;
        int exactNumbers = 6;

        IntegralSolver integralSolver = new IntegralSolver();
        double result = integralSolver.solveIntegral(function, lowerLimit, upperLimit, exactNumbers);

        Assert.assertEquals(123.456, result, 1E-3);
    }

    @Test
    public void testSimpleFunction() {
        Function<Double, Double> function = x -> x;
        int lowerLimit = 0;
        int upperLimit = 2;
        int exactNumbers = 8;

        IntegralSolver integralSolver = new IntegralSolver();
        double result = integralSolver.solveIntegral(function, lowerLimit, upperLimit, exactNumbers);

        Assert.assertEquals(2, result, 1E-8);
    }

    @Test
    public void testInvalidLimits() {
        Function<Double, Double> function = x -> x;
        int lowerLimit = 2;
        int upperLimit = 0;
        int exactNumbers = 8;

        IntegralSolver integralSolver = new IntegralSolver();

        exception.expect(InvalidLimitsException.class);
        integralSolver.solveIntegral(function, lowerLimit, upperLimit, exactNumbers);
    }

    @Test
    public void testSameLimits() {
        Function<Double, Double> function = x -> x * x;
        int lowerLimit = 1;
        int upperLimit = 1;
        int exactNumbers = 6;

        IntegralSolver integralSolver = new IntegralSolver();
        double result = integralSolver.solveIntegral(function, lowerLimit, upperLimit, exactNumbers);

        Assert.assertEquals(0, result, 1E-6);
    }

}
