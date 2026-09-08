package com.haritonov.apitests.wordpress.tests;

import com.haritonov.apitests.common.BaseTest;
import com.haritonov.apitests.wordpress.db.DbConnection;
import org.testng.annotations.AfterSuite;

/**
 * Базовый класс для тестов, работающих с базой данных WordPress.
 */
public abstract class BaseDbTest extends BaseTest {

    /**
     * Постусловие: Закрывает соединение с базой данных после выполнения всех тестов в сьюте.
     * Флаг {@code alwaysRun = true} гарантирует закрытие даже в случае падения каких-либо тестов.
     */
    @AfterSuite(alwaysRun = true)
    public void tearDownDb() {
        DbConnection.disconnect();
    }
}
