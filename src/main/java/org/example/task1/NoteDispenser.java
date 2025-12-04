package org.example.task1;

public class NoteDispenser implements DispenseChain {

    private final int denomination;
    private DispenseChain next;

    public NoteDispenser(int denomination) {
        if (denomination <= 0) {
            throw new IllegalArgumentException("Denomination must be positive");
        }
        this.denomination = denomination;
    }

    @Override
    public void setNext(DispenseChain next) {
        this.next = next;
    }

    @Override
    public void dispense(WithdrawalRequest request) {
        int amount = request.getRemainingAmount();
        if (amount >= denomination) {
            int available = request.getAvailableNotes(denomination);
            int needed = amount / denomination;
            int used = Math.min(needed, available);
            if (used > 0) {
                request.useNotes(denomination, used);
            }
        }
        if (next != null && request.getRemainingAmount() > 0) {
            next.dispense(request);
        }
    }
}
