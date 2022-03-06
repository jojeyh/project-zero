package com.revature.service;

import com.revature.dao.BankDao;

public class BankService {
    private final BankDao bankDao;

    public BankService() {
        this.bankDao = new BankDao();
    }

    public void createClient(String lastName, String firstName) {
        bankDao.createClient(lastName, firstName);
    }
}
