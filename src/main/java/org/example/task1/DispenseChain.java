package org.example.task1;

public interface DispenseChain {

    void setNext(DispenseChain next);

    void dispense(WithdrawalRequest request);
}
