package com.revature.model;

import java.util.ArrayList;
import java.util.Objects;

public class Client {
    private String firstName;
    private String lastName;
    private int id;
    private ArrayList<Account> accounts;

    public Client() {
    }

    public Client(String firstName, String lastName, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.accounts = new ArrayList<Account>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(accounts, client.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, id, accounts);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Client(int id, String firstName, String lastName, ArrayList<Account> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.accounts = accounts;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}
