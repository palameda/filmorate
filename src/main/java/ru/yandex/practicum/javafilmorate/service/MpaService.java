package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.dao.MpaStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa getById(int id) {
        log.info("Получен рейтинг по id {}", id);
        return new Mpa(id, mpaStorage.findById(id));
    }

    public List<Mpa> getAll() {
        log.info("Получен список рейтингов");
        return mpaStorage.findAll();
    }
}
