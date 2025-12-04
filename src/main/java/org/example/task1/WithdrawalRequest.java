package org.example.task1;

import java.util.LinkedHashMap;
import java.util.Map;

public class WithdrawalRequest {

    private int remainingAmount;
    private final Map<Integer, Integer> inventorySnapshot;
    private final Map<Integer, Integer> notesToDispense = new LinkedHashMap<>();

    public WithdrawalRequest(int amount, Map<Integer, Integer> inventorySnapshot) {
        this.remainingAmount = amount;
        this.inventorySnapshot = new LinkedHashMap<>(inventorySnapshot);
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }

    public int getAvailableNotes(int denomination) {
        return inventorySnapshot.getOrDefault(denomination, 0);
    }

    public void useNotes(int denomination, int count) {
        if (count <= 0) {
            return;
        }
        int available = getAvailableNotes(denomination);
        if (count > available) {
            throw new IllegalArgumentException("Trying to use more notes than available");
        }
        notesToDispense.merge(denomination, count, Integer::sum);
        remainingAmount -= denomination * count;
    }

    public Map<Integer, Integer> getNotesToDispense() {
        return notesToDispense;
    }
}
