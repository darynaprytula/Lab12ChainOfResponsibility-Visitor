package org.example;

import org.example.task1.ATM;
import org.example.task1.WithdrawalResult;
import org.example.task1.WithdrawalStatus;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    @Test
    void constructorThrowsOnNegativeCountOrInvalidDenom() {
        assertThrows(IllegalArgumentException.class,
                () -> new ATM(Map.of(-10, 5)));

        assertThrows(IllegalArgumentException.class,
                () -> new ATM(Map.of(10, -1)));
    }

    @Test
    void withdrawFailsOnZeroOrNegativeAmount() {
        ATM atm = new ATM(Map.of(50, 2, 20, 1));

        WithdrawalResult r1 = atm.withdraw(0);
        assertEquals(WithdrawalStatus.CANNOT_DISPENSE_EXACT_AMOUNT, r1.getStatus());

        WithdrawalResult r2 = atm.withdraw(-10);
        assertEquals(WithdrawalStatus.CANNOT_DISPENSE_EXACT_AMOUNT, r2.getStatus());
    }

    @Test
    void withdrawsExactSingleDenomination() {
        ATM atm = new ATM(Map.of(50, 5));

        WithdrawalResult res = atm.withdraw(150);

        assertEquals(WithdrawalStatus.SUCCESS, res.getStatus());
        assertEquals(3, res.getNotes().get(50));
        assertEquals(3, res.getTotalCoinsDispensed());
    }

    @Test
    void withdrawAllMoneyLeavesInventoryZero() {
        ATM atm = new ATM(Map.of(
                100, 1,
                50, 1,
                20, 2
        )); // total = 190

        WithdrawalResult res = atm.withdraw(190);
        assertEquals(WithdrawalStatus.SUCCESS, res.getStatus());

        Map<Integer, Integer> inv = atm.getInventorySnapshot();
        assertEquals(0, inv.get(100));
        assertEquals(0, inv.get(50));
        assertEquals(0, inv.get(20));
    }

    @Test
    void inventoryUpdatesAcrossMultipleWithdrawals() {
        ATM atm = new ATM(Map.of(
                100, 2,
                50, 2
        ));

        WithdrawalResult r1 = atm.withdraw(150);
        assertEquals(WithdrawalStatus.SUCCESS, r1.getStatus());

        Map<Integer, Integer> invAfter1 = atm.getInventorySnapshot();
        assertEquals(1, invAfter1.get(100));
        assertEquals(1, invAfter1.get(50));

        WithdrawalResult r2 = atm.withdraw(100);
        assertEquals(WithdrawalStatus.SUCCESS, r2.getStatus());

        Map<Integer, Integer> invAfter2 = atm.getInventorySnapshot();
        assertEquals(0, invAfter2.get(100));
        assertEquals(1, invAfter2.get(50));
    }

    @Test
    void cannotDispenseWhenSmallestDenominationTooBig() {
        ATM atm = new ATM(Map.of(
                10, 10,
                20, 10
        ));

        WithdrawalResult res = atm.withdraw(5);
        assertEquals(WithdrawalStatus.CANNOT_DISPENSE_EXACT_AMOUNT, res.getStatus());
    }

    @Test
    void chainAppliesInDescendingOrder() {
        Map<Integer, Integer> inv = Map.of(
                100, 1,
                50, 1,
                20, 1,
                10, 1
        );

        ATM atm = new ATM(inv);

        WithdrawalResult res = atm.withdraw(130);

        // must use 100 + 20 + 10, not 50+50+20+10
        assertEquals(1, res.getNotes().get(100));
        assertEquals(0, res.getNotes().getOrDefault(50, 0));
        assertEquals(1, res.getNotes().get(20));
        assertEquals(1, res.getNotes().get(10));
    }
}
