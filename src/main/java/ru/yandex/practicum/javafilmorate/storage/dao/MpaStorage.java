package ru.yandex.practicum.javafilmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Mpa;

import java.util.List;

@Component
public interface MpaStorage {
    String findById(int id);

    List<Mpa> findAll();
}
