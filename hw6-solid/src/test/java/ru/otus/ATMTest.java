package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

public class ATMTest {
    private ATM atm;

    @BeforeEach
    public void setUp() throws Exception {
        atm = new ATM();
    }

    // принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
    // выдавать сумму остатка денежных средств
    @Test
    public void shouldAcceptBanknotesOfDifferentDenominations() {
        atm.deposit(10, Denomination.ONE_HUNDRED);
        atm.deposit(5, Denomination.FIVE_HUNDRED);
        assertEquals(3500, atm.getBalance());
    }

    // выдавать запрошенную сумму минимальным количеством банкнот
    @Test
    public void shouldGiveOut() {
        atm.deposit(10, Denomination.ONE_HUNDRED);
        atm.deposit(10, Denomination.FIVE_HUNDRED);
        int balanceBefore = atm.getBalance();
        Map<Denomination, Integer> payment = atm.giveOut(700);
        assertEquals(balanceBefore - 700, atm.getBalance());
        assertEquals(1, payment.get(Denomination.FIVE_HUNDRED).intValue());
        assertEquals(2, payment.get(Denomination.ONE_HUNDRED).intValue());
    }

    // выдавать запрошенную сумму минимальным количеством банкнот или ошибку если сумму нельзя выдать
    @Test
    void shouldShowCanNotCash() {
        atm.deposit(10, Denomination.FIVE_HUNDRED);
        atm.deposit(2, Denomination.ONE_HUNDRED);
        Map<Denomination, Integer> payment = atm.giveOut(800);
        assertEquals(5200, atm.getBalance());
        int cash = 800;
        int amount = 100;

        Throwable msg = assertThrows(AtmException.class, () -> {

            throw new AtmException(String.format("Невозможно выдать сумму %d, нет доступных купюр. Нет возможности выдать сумму %s. Выберете другую сумму.", cash, amount));
        });
        assertEquals("Невозможно выдать сумму 800, нет доступных купюр. Нет возможности выдать сумму 100. Выберете другую сумму.", msg.getMessage());
    }

    @Test
    void shouldShowNoMoney() {
        assertEquals(0, atm.getBalance());
        Map<Denomination, Integer> payment = atm.giveOut(10000);
        Throwable msg = assertThrows(AtmException.class, () -> {

            throw new AtmException(String.format("В банкомате нет денег! Баланс: %s", atm.getBalance()));
        });

        assertEquals("В банкомате нет денег! Баланс: 0", msg.getMessage());
    }

    @Test
    void shouldShowNotEnoughMoney() {
        atm.deposit(100, Denomination.TEN);
        assertEquals(1000, atm.getBalance());
        Map<Denomination, Integer> payment = atm.giveOut(5000);
        assertEquals(1000, atm.getBalance());
        int cash = 5000;
        Throwable msg = assertThrows(AtmException.class, () -> {

            throw new AtmException(String.format("В банкомате не достаточно средств: %d!", (atm.getBalance() - cash)));
        });

        assertEquals("В банкомате не достаточно средств: -4000!", msg.getMessage());

    }

    @Test
    public void shouldNotGiveOutZero() {
        atm.deposit(10, Denomination.ONE_HUNDRED);
        atm.deposit(10, Denomination.FIVE_HUNDRED);
        int balanceBefore = atm.getBalance();
        Map<Denomination, Integer> payment = atm.giveOut(0);
        assertEquals(balanceBefore - 0, atm.getBalance());
        Throwable msg = assertThrows(AtmException.class, () -> {
            throw new AtmException("Выберете сумму отличную от 0");
        });
        assertEquals("Выберете сумму отличную от 0", msg.getMessage());

    }

    @Test
    public void shouldGiveWithCorrectBanknotes() {
        atm.deposit(1, Denomination.ONE_THOUSAND);
        atm.deposit(10, Denomination.FIVE_HUNDRED);
        int balanceBefore = atm.getBalance();
        Map<Denomination, Integer> payment = atm.giveOut(2500);
        assertEquals(balanceBefore - 2500, atm.getBalance());
        assertEquals(3, payment.get(Denomination.FIVE_HUNDRED).intValue());
        assertEquals(1, payment.get(Denomination.ONE_THOUSAND).intValue());
    }
}