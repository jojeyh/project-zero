package com.revature.model;

import java.util.ArrayList;
import java.util.Objects;

public class Client {
    private String firstName;
    private String lastName;
    private int id;
    private ArrayList<Integer> accounts;

    public Client() {
    }

    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = 0;
        this.accounts = new ArrayList<Integer>();
        this.accounts.add(0);
    }

    public Client(String firstName, String lastName, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.accounts = new ArrayList<Integer>();
        this.accounts.add(0);
    }

    public Client(String firstName, String lastName, int id, ArrayList<Integer> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return getId() == client.getId() && getFirstName().equals(client.getFirstName()) && getLastName().equals(client.getLastName()) && getAccounts().equals(client.getAccounts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getId(), getAccounts());
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                '}';
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

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getAccounts() {
        return accounts;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccounts(ArrayList<Integer> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Integer accountId) {
        accounts.add(accountId);
    }
}
