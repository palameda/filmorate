package ru.yandex.practicum.javafilmorate.storage.dao;

import java.util.List;

public interface LikeStorage {
    void addLike(Integer filmId, Integer userId);

    void deleteLike(int filmId, int userId);

    List<Integer> getLikes(int filmId);
}
