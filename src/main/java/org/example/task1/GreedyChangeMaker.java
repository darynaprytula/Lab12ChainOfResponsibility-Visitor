package org.example.task1;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GreedyChangeMaker {

    private GreedyChangeMaker() {
    }

    public static Map<Integer, Integer> makeChangeGreedy(int amount, List<Integer> denominations) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        if (denominations == null || denominations.isEmpty()) {
            throw new IllegalArgumentException("No denominations provided");
        }

        List<Integer> sorted = new ArrayList<>(denominations);
        sorted.sort(Comparator.reverseOrder());

        Map<Integer, Integer> result = new LinkedHashMap<>();
        int remaining = amount;

        for (int denom : sorted) {
            if (denom <= 0) {
                throw new IllegalArgumentException("Denomination must be positive: " + denom);
            }
            int count = remaining / denom;
            if (count > 0) {
                result.put(denom, count);
                remaining -= count * denom;
            }
        }

        if (remaining != 0) {
            throw new IllegalArgumentException("Cannot make exact change with given denominations");
        }

        return result;
    }
}
