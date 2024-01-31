package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Director;
import ru.yandex.practicum.javafilmorate.storage.dao.DirectorStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class DirectorService {

    DirectorStorage directorStorage;

    public List<Director> findAll() {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка режиссёров");
        return directorStorage.findAll();
    }

    public Director findById(Integer directorId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение режиссёра с id {}", directorId);
        return directorStorage.findById(directorId);
    }

    public Director addDirector(Director director) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление режиссёра {}", director.getName());
        return directorStorage.addDirector(director);
    }

    public Director updateDirector(Director director) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на обновление режиссёра с id {}", director.getId());
        return directorStorage.updateDirector(director);
    }

    public void deleteDirector(Integer directorId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на удаление режиссёра с id {}", directorId);
        directorStorage.deleteDirector(directorId);
    }
}
