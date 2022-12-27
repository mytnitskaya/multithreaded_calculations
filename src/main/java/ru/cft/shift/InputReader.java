package ru.cft.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputReader {

    private static final Logger log = LoggerFactory.getLogger(InputReader.class);

    public static long readLongFromConsole() {
        var scanner = new Scanner(System.in);
        long input = 0;
        var continueScanner = true;

        do {
            try {
                System.out.println("Enter positive integer:");
                input = scanner.nextLong();
                if (input < 0) {
                    log.error("Entered number must be greater or equal than 0");
                    throw new Exception();
                }
                System.out.println("The function will be evaluated for numbers from 0 to " + input);
                continueScanner = false;
            } catch (InputMismatchException e) {
                log.error("Incorrect input: an integer is required");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                scanner.nextLine();
            }
        } while (continueScanner);

        return input;
    }
}
