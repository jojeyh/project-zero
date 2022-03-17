package com.revature.utility;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ConnectionUtilityTest {

    @Test
    public void test_getConnection() throws SQLException {
        ConnectionUtility cutil = new ConnectionUtility(
                System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASS")
        );
        cutil.getConnection();
    }

}
