package org.example;

import org.example.task1.GreedyChangeMaker;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GreedyChangeMakerTest {

    @Test
    void largeAmountWorks() {
        Map<Integer, Integer> res = GreedyChangeMaker.makeChangeGreedy(
                999,
                List.of(1, 5, 10, 25)
        );

        int sum = res.entrySet().stream().mapToInt(e -> e.getKey() * e.getValue()).sum();
        assertEquals(999, sum);
    }

    @Test
    void oneDenominationOnly() {
        Map<Integer, Integer> res = GreedyChangeMaker.makeChangeGreedy(
                300,
                List.of(100)
        );

        assertEquals(3, res.get(100));
        assertEquals(1, res.size());
    }

    @Test
    void throwsOnEmptyDenominations() {
        assertThrows(IllegalArgumentException.class, () ->
                GreedyChangeMaker.makeChangeGreedy(100, List.of()));
    }

    @Test
    void throwsOnNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                GreedyChangeMaker.makeChangeGreedy(-1, List.of(1, 2, 5)));
    }
}
