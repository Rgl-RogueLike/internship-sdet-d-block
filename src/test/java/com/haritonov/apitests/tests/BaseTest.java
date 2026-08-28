package com.haritonov.apitests.tests;

import com.haritonov.apitests.db.DatabaseManager;
import org.testng.annotations.AfterSuite;

public abstract class BaseTest {

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        DatabaseManager.disconnect();
    }
}
