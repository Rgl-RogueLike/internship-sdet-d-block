package com.haritonov.apitests.db;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.config.Configuration;

import java.sql.*;

public final class DatabaseManager {

    private static Connection connection;

    private DatabaseManager(){}

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

    public static String getPostStatusById(int postId) {
        String sql = "SELECT post_status FROM wp_posts WHERE ID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)){
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

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }

    public static String getPostTitleById(int postId) {
        String sql = "SELECT post_title FROM wp_posts WHERE ID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("post_title");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get pot title for ID: " + postId, e);
        }
        return null;
    }
}
