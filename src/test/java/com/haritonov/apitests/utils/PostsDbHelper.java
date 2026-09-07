package com.haritonov.apitests.utils;

import com.haritonov.apitests.db.PostDao;

/**
 * Хелпер для подготовки тестовых данных постов напрямую в БД.
 */
public final class PostsDbHelper {

    /**
     * Контейнер для хранения тестового поста, созданного в БД.
     */
    public record DbTestPost(int id, String title, String content) {
    }

    private PostsDbHelper() {
    }

    /**
     * Создает тестовый пост напрямую в БД.
     * Генерирует данные, выполняет INSERT и возвращает контейнер с ID и текстами.
     *
     * @param status Статус поста
     * @return Объект DbTestPost с ID и сгенерированными текстами
     */
    public static DbTestPost createTestPostInDb(String status) {
        String uniqueTitle = DataGenerator.generatePostTitle();
        String uniqueContent = DataGenerator.generatePostContent();
        int postId = PostDao.createPostDirectly(uniqueTitle, uniqueContent, status);
        return new DbTestPost(postId, uniqueTitle, uniqueContent);
    }
}
