package com.revature.services;

import java.util.Date;

public class Account {
    protected int accountNumber;
    protected int ownerID;
    protected String accountType;
    protected double balance;
    protected Date openingDate;
    protected String active;
    protected  double pendingTransaction;

    public Account() {

    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public double getPendingTransaction() {
        return pendingTransaction;
    }

    public void setPendingTransaction(double pendingTransaction) {
        this.pendingTransaction = pendingTransaction;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", ownerID=" + ownerID +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", openingDate=" + openingDate +
                ", active='" + active + '\'' +
                ", pendingTransaction=" + pendingTransaction +
                '}';
    }
}
