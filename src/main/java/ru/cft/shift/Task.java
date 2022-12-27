package ru.cft.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class Task implements Callable<Double> {
    private static final Logger log = LoggerFactory.getLogger(Task.class);
    private final long numberFrom;
    private final long numberTo;

    public Task(long numberFrom, long numberTo) {
        this.numberFrom = numberFrom;
        this.numberTo = numberTo;
    }

    @Override
    public Double call(){
        double result = 0;
        for (long i = numberFrom; i <= numberTo; i++) {
            result += Function.evaluateFunction(i);
        }
        log.info(String.format("thread result = %.6f", result));
        return result;
    }
}
