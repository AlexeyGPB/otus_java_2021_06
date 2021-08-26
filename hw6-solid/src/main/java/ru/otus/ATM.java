package ru.otus;

import java.util.*;

public class ATM {

    SortedMap<Denomination, Integer> dispensers = new TreeMap<>(Collections.reverseOrder());

    public void deposit(int notes, Denomination denomination) {
        int value = dispensers.getOrDefault(denomination, 0);
        dispensers.put(denomination, value + notes);
    }

    public int getBalance() {
        return dispensers.entrySet()
                .stream()
                .mapToInt(entry -> entry.getKey().getValue() * entry.getValue())
                .sum();
    }

    public Map<Denomination, Integer> giveOut(final int cash) {
        Map<Denomination, Integer> payment = new HashMap<>();

        int amount = cash;
        try {
            if (amount == 0) {
                throw new AtmException("Выберете сумму отличную от 0");
            }
            if (getBalance() == 0) {
                throw new AtmException(String.format("В банкомате нет денег! Баланс: %s", getBalance()));
            }
            if (getBalance() < cash) {
                throw new AtmException(String.format("В банкомате не достаточно средств: %d!", (getBalance() - cash)));
            }
        } catch (AtmException ex) {
            System.out.println(ex.getMessage());
        }
        for (Denomination denomination : Denomination.values()) {
            int haveNotes = dispensers.getOrDefault(denomination, 0);
            if (haveNotes > 0) {
                int needNotes = amount / denomination.getValue();
                int notesToDispense = Math.min(haveNotes, needNotes);
                payment.put(denomination, notesToDispense);
                amount -= denomination.getValue() * notesToDispense;
            }
        }
        try {
            if (amount > 0 && amount != cash) {
                payment.forEach((denomination, notesToDispense) -> dispensers.compute(denomination, (key, haveNotes) -> {
                    return haveNotes;
                }));
                throw new AtmException(String.format("Невозможно выдать сумму %d, нет доступных купюр. Нет возможности выдать сумму %s. Выберете другую сумму.", cash, amount));
            } else {
                payment.forEach((denomination, notesToDispense) -> dispensers.compute(denomination, (key, haveNotes) -> {
                    return haveNotes - notesToDispense;
                }));
            }
        } catch (AtmException ex) {
            System.out.println(ex.getMessage());
        }
        return payment;
    }
}
