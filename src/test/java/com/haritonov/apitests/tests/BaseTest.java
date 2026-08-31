package com.haritonov.apitests.tests;

import com.haritonov.apitests.db.DatabaseManager;
import org.testng.annotations.AfterSuite;

/**
 * Базовый абстрактный класс для всех тестовых классов.
 * <p>
 * Содержит общие настройки и завершающие действия для всего тестового набора.
 * Объявлен {@code abstract}, так как не предназначен для самостоятельного создания экземпляров
 * и служит только для наследования.
 */
public abstract class BaseTest {

    /**
     * Постусловие: Закрывает соединение с базой данных после выполнения всех тестов в сьюте.
     * Флаг {@code alwaysRun = true} гарантирует закрытие даже в случае падения каких-либо тестов.
     */
    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        DatabaseManager.disconnect();
    }
}
