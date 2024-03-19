package org.example;

import java.util.List;

public class Piwi {
    public static int whichIsTheLargest(int... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("At least one number must be provided");
        }

        int largest = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > largest) {
                largest = numbers[i];
            }
        }

        return largest;
    }

    public static int whichIsTheSmallest(int... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("At least one number must be provided");
        }

        int smallest = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < smallest) {
                smallest = numbers[i];
            }
        }

        return smallest;
    }

    public static <T> boolean indexExists(List<T> list, int index) {
        return index >= 0 && index < list.size();
    }
}
