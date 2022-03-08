package com.revature.model;

public class Account {
    private int balance; // balance in cents
    private int id;
    private AccountType accountType;

    public Account() {};

    public enum AccountType {
        CHECKING,
        SAVINGS;
    }

    public Account(int balance, int id) {
        this.balance = balance;
        this.id = id;
    }

    public Account(int balance, int id, AccountType accountType) {
        this.balance = balance;
        this.id = id;
        this.accountType = accountType;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
