package com.haritonov.apitests.db;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления соединением с базой данных.
 */
public final class DbConnection {

    private static Connection connection;

    private DbConnection() {
    }

    /**
     * Инициализирует и возвращает соединение с БД.
     * Если соединение уже открыто, возвращает его.
     *
     * @return объект {@link Connection}
     * @throws RuntimeException если подключение к БД завершается с ошибкой
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Configuration config = ConfigManager.getConfig();
                connection = DriverManager.getConnection(
                        config.dbUrl(),
                        config.dbUser(),
                        config.dbPassword()
                );
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    /**
     * Закрывает активное соединение с базой данных, если оно открыто.
     * Рекомендуется вызывать в методах очистки (например, в {@code @AfterSuite}).
     */
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database connection:" + e);
        }
    }
}
