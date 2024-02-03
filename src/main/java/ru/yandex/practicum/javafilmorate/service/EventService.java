package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.storage.dao.EventStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.UserStorage;
import ru.yandex.practicum.javafilmorate.utils.CheckUtil;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

    private EventStorage eventStorage;
    private UserStorage userStorage;

    public void add(Event event) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление event userId={}", event.getUserId());
        eventStorage.add(event);
    }

    public List<Event> getUserFeed(int userId) {
        CheckUtil.checkNotFound(userStorage.findById(userId));
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение ленты user с Id={}", userId);
        return eventStorage.getUserEvents(userId);
    }


}

