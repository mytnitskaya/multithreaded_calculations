package ru.cft.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadedCalculations {
    private static final Logger log = LoggerFactory.getLogger(MultiThreadedCalculations.class);
    private static final int maxNumberOfThreads = 10;
    private static final int minNumberOfThreads = 1;


    public void calculate(long numberTo) {
        long numberFrom = 0;
        int numberOfThreads = calculateMaxNumberOfThreads(numberFrom, numberTo);
        List<Map.Entry<Long, Long>> sections = splitInterval(numberFrom, numberTo, numberOfThreads);
        numberOfThreads = Math.min(numberOfThreads, sections.size());
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        List<Task> tasks = createTasks(sections);
        List<Future<Double>> futures = new ArrayList<>();
        for (Task task : tasks) {
            var future = executor.submit(task);
            futures.add(future);
        }
        executor.shutdown();

        double result = 0;
        try {
            for (var res : futures) {
                result += res.get();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        System.out.println("Result of evaluation: " + result);
    }


    private List<Task> createTasks(List<Map.Entry<Long, Long>> sections) {
        List<Task> tasks = new ArrayList<>();
        for (Map.Entry<Long, Long> section : sections) {
            Task task = new Task(section.getKey(), section.getValue());
            tasks.add(task);
        }
        return tasks;
    }

    private int calculateMaxNumberOfThreads(long numberFrom, long numberTo) {
        var interval = numberTo - numberFrom + 1;
        return interval < maxNumberOfThreads ? minNumberOfThreads : maxNumberOfThreads;
    }

    private List<Map.Entry<Long, Long>> splitInterval(long numberFrom, long numberTo, int maxNumberOfSplit) {
        var lengthOfSection = getLengthOfSection(numberFrom, numberTo, maxNumberOfSplit);

        List<Map.Entry<Long, Long>> sections = new ArrayList<>();
        long sectionFrom = numberFrom;
        long sectionTo = numberFrom + lengthOfSection - 1;

        do {
            sections.add(new AbstractMap.SimpleEntry<>(sectionFrom, sectionTo));
            if (sectionTo == numberTo) {
                break;
            }
            sectionFrom += lengthOfSection;
            sectionTo += lengthOfSection;
            sectionTo = Math.min(sectionTo, numberTo);
        } while (true);
        return sections;
    }

    private long getLengthOfSection(long numberFrom, long numberTo, int numberOfSplit) {
        var interval = numberTo - numberFrom + 1;
        var sectionLength = interval / numberOfSplit;
        return interval % numberOfSplit == 0 ? sectionLength : sectionLength + 1;
    }


}
