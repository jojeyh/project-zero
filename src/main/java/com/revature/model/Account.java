package com.revature.model;

import java.util.Objects;

public class Account {
    private int balance; // balance in cents
    private int id;
    private int clientId;
    private AccountType accountType;

    public Account() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getBalance() == account.getBalance() && getId() == account.getId() && getClientId() == account.getClientId() && getAccountType() == account.getAccountType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBalance(), getId(), getClientId(), getAccountType());
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", id=" + id +
                ", clientId=" + clientId +
                ", accountType=" + accountType +
                '}';
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
