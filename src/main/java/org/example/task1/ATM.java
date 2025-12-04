package org.example.task1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATM {

    private final Map<Integer, Integer> inventory = new HashMap<>();

    public ATM(Map<Integer, Integer> initialInventory) {
        for (Map.Entry<Integer, Integer> e : initialInventory.entrySet()) {
            if (e.getKey() <= 0 || e.getValue() < 0) {
                throw new IllegalArgumentException("Invalid denomination or count");
            }
        }
        inventory.putAll(initialInventory);
    }

    public synchronized WithdrawalResult withdraw(int amount) {
        if (amount <= 0) {
            return WithdrawalResult.failure(
                    WithdrawalStatus.CANNOT_DISPENSE_EXACT_AMOUNT,
                    "Amount must be positive"
            );
        }

        int totalCash = inventory.entrySet().stream()
                .mapToInt(e -> e.getKey() * e.getValue())
                .sum();

        if (amount > totalCash) {
            return WithdrawalResult.failure(
                    WithdrawalStatus.INSUFFICIENT_FUNDS,
                    "Not enough money in ATM"
            );
        }

        Map<Integer, Integer> snapshot = new HashMap<>(inventory);
        WithdrawalRequest request = new WithdrawalRequest(amount, snapshot);

        List<Integer> sortedDenoms = new ArrayList<>(snapshot.keySet());
        sortedDenoms.sort(Comparator.reverseOrder());

        DispenseChain first = null;
        DispenseChain current = null;
        for (int denom : sortedDenoms) {
            NoteDispenser node = new NoteDispenser(denom);
            if (first == null) {
                first = node;
            } else {
                current.setNext(node);
            }
            current = node;
        }

        if (first != null) {
            first.dispense(request);
        }

        if (request.getRemainingAmount() != 0) {
            return WithdrawalResult.failure(
                    WithdrawalStatus.CANNOT_DISPENSE_EXACT_AMOUNT,
                    "Cannot provide exact amount with available denominations"
            );
        }

        Map<Integer, Integer> dispensed = request.getNotesToDispense();

        for (Map.Entry<Integer, Integer> e : dispensed.entrySet()) {
            int denom = e.getKey();
            int count = e.getValue();
            inventory.merge(denom, -count, Integer::sum);
        }

        return WithdrawalResult.success(dispensed);
    }

    public Map<Integer, Integer> getInventorySnapshot() {
        return Map.copyOf(inventory);
    }
}
