package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.dao.MpaStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa getById(int id) {
        return new Mpa(id, mpaStorage.findById(id));
    }

    public List<Mpa> getAll() {
        return mpaStorage.findAll();
    }
}
