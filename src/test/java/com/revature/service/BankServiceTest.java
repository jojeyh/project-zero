package com.revature.service;

import com.revature.dao.BankDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BankServiceTest {

    /*
    @Test
    public void testCreateClient() {

    }
    */

    // Positive
    @Test
    public void testGetAllClients() {
        BankDao mockedDao = mock(BankDao.class);

        List<Client> fakeClients = new ArrayList<>();
        fakeClients.add(new Client("TE", "Lawrence", 1));
        fakeClients.add(new Client("Sharif", "Ali", 2));
        fakeClients.add(new Client("Prince", "Faisel", 3));

        when(mockedDao.getAllClients()).thenReturn(fakeClients);

        BankService bankService = new BankService(mockedDao);

        List<Client> actual = bankService.getAllClients();

        List<Client> expected = new ArrayList<>(fakeClients);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getClientById_clientNotFound() throws SQLException, ClientNotFoundException {
        BankDao mockedDao = mock(BankDao.class);
        BankService bankService = new BankService(mockedDao);

        when(mockedDao.getClientWithId(10)).thenReturn(null);

        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            bankService.getClientWithId("10");
        });
    }

    @Test
    public void test_getClientById_positiveTest() throws SQLException, ClientNotFoundException {
        BankDao mockedDao = mock(BankDao.class);
        BankService bankService = new BankService(mockedDao);

        when(mockedDao.getClientWithId(10)).thenReturn(new Client("Peter", "Pan", 10));

        Client actual = bankService.getClientWithId("10");
        Client expected = new Client("Peter", "Pan", 10);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void test_getStudentById_invalidID() throws SQLException, ClientNotFoundException {
        BankDao mockedDao = mock(BankDao.class);
        BankService bankService = new BankService(mockedDao);

        try {
            bankService.getClientWithId("abc");
            fail();
        } catch (IllegalArgumentException e) {
            String expectedMsg = "Id provided must be a valid int";
            String actualMsg = e.getMessage();

            Assertions.assertEquals(expectedMsg, actualMsg);
        }
    }
}
