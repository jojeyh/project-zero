package com.revature.service;

import com.revature.dao.BankDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.WrongIdException;
import com.revature.model.Account;
import com.revature.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BankServiceTest {

    BankDao mockDao = mock(BankDao.class);
    BankService bankService = new BankService(mockDao);

    @Test
    public void test_createClient_positiveTest() {
        when(mockDao.createClient(new Client("Peter", "Pan", 1))).thenReturn(1);

        Integer actual = bankService.createClient(new Client("Peter", "Pan", 1));
        Integer expected = 1;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_createClient_invalidFirstName() {
        try {
            bankService.createClient(new Client("Ger445akd", "Bernard"));
        } catch (IllegalArgumentException e) {
            String expected = "First name must be alphabetical characters only";
            String actual = e.getMessage();

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void test_createClient_invalidLastName() {
        try {
            bankService.createClient(new Client("George", "Ber4929d"));
        } catch (IllegalArgumentException e) {
            String expected = "Last name must be alphabetical characters only";
            String actual = e.getMessage();

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetAllClients() {

        List<Client> fakeClients = new ArrayList<>();
        fakeClients.add(new Client("TE", "Lawrence", 1));
        fakeClients.add(new Client("Sharif", "Ali", 2));
        fakeClients.add(new Client("Prince", "Faisel", 3));

        when(mockDao.getAllClients()).thenReturn(fakeClients);

        List<Client> actual = bankService.getAllClients();

        List<Client> expected = new ArrayList<>(fakeClients);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getClientById_clientNotFound() throws SQLException, ClientNotFoundException {

        when(mockDao.getClientWithId(10)).thenReturn(null);

        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            bankService.getClientWithId("10");
        });
    }

    @Test
    public void test_getClientById_positiveTest() throws SQLException, ClientNotFoundException {
        when(mockDao.getClientWithId(10)).thenReturn(new Client("Peter", "Pan", 10));

        Client actual = bankService.getClientWithId("10");
        Client expected = new Client("Peter", "Pan", 10);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void test_getClientById_invalidID() throws SQLException, ClientNotFoundException {
        try {
            bankService.getClientWithId("abc");
            fail();
        } catch (IllegalArgumentException e) {
            String expectedMsg = "Id provided must be a valid int";
            String actualMsg = e.getMessage();

            Assertions.assertEquals(expectedMsg, actualMsg);
        }
    }

    @Test
    public void test_updateClientWithId_positiveTest() throws WrongIdException {
        Client client = new Client("Peter", "Pan", 10);
        when(mockDao.updateClientWithId(client))
                .thenReturn(client);

        Client actual = bankService.updateClientWithId(client, 10);
        Client expected = new Client("Peter", "Pan", 10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_updateClientWithId_WrongId() {
        try {
            bankService.updateClientWithId(new Client("Peter", "Pan", 10), 12);
        } catch (WrongIdException e) {
            String expected = "Cannot change a client's ID.  Either create a new record or update with same ID";
            String actual = e.getMessage();

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void test_deleteClientWithId_positiveTest() {
        when(mockDao.deleteClientWithId(anyInt())).thenReturn(true);

        Boolean actual = bankService.deleteClientWithId("2");
        Boolean expected = true;

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void test_deleteClientWithId_invalidID() {
        try {
            bankService.deleteClientWithId("AAA");
            fail();
        } catch (IllegalArgumentException e) {
            String expected = "Invalid ID entered";
            String actual = e.getMessage();

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void test_addAccountById_positiveTest() {
        Account account = new Account(1, 1, 1, Account.AccountType.CHECKING);
        when(mockDao.addAccountById(account)).thenReturn(true);

        Boolean actual = bankService.addAccountById(account);
        Boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_addAccountById_negativeBalance() {
        Account account = new Account(-1, 1, 1, Account.AccountType.CHECKING);

        try {
            bankService.addAccountById(account);
        } catch (IllegalArgumentException e) {
            String actual = e.getMessage();
            String expected = "Cannot have a negative account balance";

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void test_addAccountById_negativeClientId() {
        Account account = new Account(1, 1, -1, Account.AccountType.CHECKING);

        try {
            bankService.addAccountById(account);
        } catch (IllegalArgumentException e) {
            String actual = e.getMessage();
            String expected = "Id must be a non-negative integer";

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void test_getAllClientAccounts_postiveTest() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1, 1, 1, Account.AccountType.CHECKING));
        accounts.add(new Account(2, 2, 1, Account.AccountType.SAVINGS));

        when(mockDao.getAllClientAccounts(1)).thenReturn(accounts);

        List<Account> actual = bankService.getAllClientAccounts("1");
        List<Account> expected = new ArrayList<>(accounts);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getAllClientAccounts_invalidID() {
        try {
            bankService.getAllClientAccounts("aekddkd");
            fail();
        } catch (IllegalArgumentException e) {
            String actual = e.getMessage();
            String expected = "Invalid ID entered";

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void test_getAllClientsInBetween_positiveTest() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(20, 2, 1, Account.AccountType.CHECKING));

        when(mockDao.getAllClientAccountsInBetween(1, 10, 30))
                .thenReturn(accounts);

        List<Account> actual = bankService.getAllClientAccountsInBetween("1", "10", "30");
        List<Account> expected = new ArrayList<>(accounts);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getAccountById_positiveTest() {
        Account account = new Account(1000, 1, 1, Account.AccountType.CHECKING);
        when(mockDao.getAccountById(1)).thenReturn(account);

        Account actual = bankService.getAccountById("1");
        Account expected = new Account(1000, 1, 1, Account.AccountType.CHECKING);

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void test_getAccountById_invalidId() {
        try {
            bankService.getAccountById("abasd");
            fail();
        } catch (IllegalArgumentException e) {
            String actual = e.getMessage();
            String expected = "Invalid ID entered";

            Assertions.assertEquals(expected, actual);
        }
    }
}
