package com.example.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friend implements Serializable {
    private String name;
    private double amountOwed;
    private String phoneNumber;
    private List<DebtEntry> debtHistory;

    public Friend(String name, double amountOwed, String phoneNumber) {
        this.name = name;
        this.amountOwed = amountOwed;
        this.phoneNumber = phoneNumber;
        debtHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(double amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addHistory(String title, double amount) {
        if (debtHistory == null) {
            debtHistory = new ArrayList<>();
        }

        DebtEntry entry = new DebtEntry(title, amount);
        debtHistory.add(entry);
    }
    public List<DebtEntry> getDebtHistory() {
        return debtHistory;
    }

    public void addDebtEntry(DebtEntry entry) {
        debtHistory.add(0, entry);
    }


    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public void updateAmountOwed(double additionalAmount) {
        amountOwed += additionalAmount;
    }
}
