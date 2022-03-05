package com.revature.model;

import java.util.ArrayList;

public class Client {
    private String firstName;
    private String lastName;
    private int id;
    private ArrayList<Account> accounts;

    public Client(int id, ArrayList<Account> accounts) {
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
}
