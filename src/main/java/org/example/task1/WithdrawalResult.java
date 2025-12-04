package org.example.task1;

import java.util.Map;

public class WithdrawalResult {

    private final WithdrawalStatus status;
    private final Map<Integer, Integer> notes;
    private final String errorMessage;

    private WithdrawalResult(WithdrawalStatus status,
                             Map<Integer, Integer> notes,
                             String errorMessage) {
        this.status = status;
        this.notes = notes == null ? Map.of() : Map.copyOf(notes);
        this.errorMessage = errorMessage;
    }

    public static WithdrawalResult success(Map<Integer, Integer> notes) {
        return new WithdrawalResult(WithdrawalStatus.SUCCESS, notes, null);
    }

    public static WithdrawalResult failure(WithdrawalStatus status, String message) {
        if (status == WithdrawalStatus.SUCCESS) {
            throw new IllegalArgumentException("Use success() for SUCCESS status");
        }
        return new WithdrawalResult(status, null, message);
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public Map<Integer, Integer> getNotes() {
        return notes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getTotalCoinsDispensed() {
        return notes.values().stream().mapToInt(Integer::intValue).sum();
    }
}
