package com.haritonov.apitests.db;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.config.Configuration;

import java.sql.*;

/**
 * Утилитный класс для взаимодействия с базой данных WordPress через JDBC.
 * <p>
 * Предоставляет методы для выполнения параметризованных SQL-запросов
 * к таблице {@code wp_posts} и управления соединением.
 */
public final class DatabaseManager {

    private static Connection connection;

    private DatabaseManager() {
    }

    /**
     * Инициализирует и возвращает соединение с БД.
     * Если соединение уже открыто, возвращает его.
     *
     * @return объект {@link Connection}
     * @throws RuntimeException если подключение к БД завершается с ошибкой
     */
    private static Connection getConnection() {
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
     * Получает статус поста по его ID.
     *
     * @param postId ID поста
     * @return строковый статус поста (например, "draft", "publish") или {@code null}, если пост не найден
     */
    public static String getPostStatusById(int postId) {
        String sql = "SELECT post_status FROM wp_posts WHERE ID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("post_status");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get post status for ID: " + postId, e);
        }
        return null;
    }

    /**
     * Проверяет существование поста в БД по его ID.
     *
     * @param postId ID поста
     * @return {@code true}, если пост существует, иначе {@code false}
     */
    public static boolean isPostExists(int postId) {
        String sql = "SELECT 1 FROM wp_posts WHERE ID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if post exists for ID: " + postId, e);
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

    /**
     * Получает заголовок поста по его ID.
     *
     * @param postId ID поста
     * @return заголовок поста или {@code null}, если пост не найден
     */
    public static String getPostTitleById(int postId) {
        String sql = "SELECT post_title FROM wp_posts WHERE ID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("post_title");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get post title for ID: " + postId, e);
        }
        return null;
    }

    /**
     * Получает контент поста по его ID.
     *
     * @param postId ID поста
     * @return контент поста или {@code null}, если пост не найден
     */
    public static String getPostContentById(int postId) {
        String sql = "SELECT post_content FROM wp_posts WHERE ID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("post_content");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get post content for ID: " + postId, e);
        }
        return null;
    }

    /**
     * Подсчитывает количество постов с указанным заголовком.
     * Используется для негативных проверок.
     *
     * @param title заголовок поста
     * @return количество найденных записей (0, если ничего не найдено)
     */
    public static int getPostCountByTitle(String title) {
        String sql = "SELECT COUNT(*) FROM wp_posts WHERE post_title = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count posts be title: " + title, e);
        }
        return 0;
    }

    /**
     * Вычисляет гарантированно несуществующий ID поста.
     * Запрашивает максимальный ID из таблицы и прибавляет к нему 1.
     *
     * @return несуществующий ID для негативных тестов
     */
    public static int getNonExistentPostId() {
        String sql = "SELECT MAX(ID) FROM wp_posts";
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int maxId = resultSet.getInt(1);
                return maxId + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get max post ID", e);
        }
        return 1;
    }
}
