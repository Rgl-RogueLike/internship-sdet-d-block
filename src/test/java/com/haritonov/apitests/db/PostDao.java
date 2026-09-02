package com.haritonov.apitests.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object для взаимодействия с таблицей wp_posts.
 */
public final class PostDao {

    private PostDao() {
    }

    /**
     * Получает статус поста по его ID.
     *
     * @param postId ID поста
     * @return строковый статус поста (например, "draft", "publish") или {@code null}, если пост не найден
     */
    public static String getPostStatusById(int postId) {
        String sql = "SELECT post_status FROM wp_posts WHERE ID = ?";
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(sql)) {
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
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if post exists for ID: " + postId, e);
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
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(sql)) {
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
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(sql)) {
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
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(sql)) {
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
        try (Statement statement = DbConnection.getConnection().createStatement();
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
