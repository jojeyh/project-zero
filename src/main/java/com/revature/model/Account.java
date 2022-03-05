package com.revature.model;

public class Account {
    private Integer balance; // balance in cents
    private int id;
    private AccountType accountType;

    private enum AccountType {
        CHECKING,
        SAVINGS;
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
