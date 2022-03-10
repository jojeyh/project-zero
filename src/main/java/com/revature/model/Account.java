package com.revature.model;

public class Account {
    private int balance; // balance in cents
    private int id;
    private int clientId;
    private AccountType accountType;

    public Account() {};

    public enum AccountType {
        CHECKING,
        SAVINGS
    }

    public Account(int balance, int id, int clientId, AccountType accountType) {
        this.balance = balance;
        this.id = id;
        this.clientId = clientId;
        this.accountType = accountType;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
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
