package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.storage.dao.EventStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

    private EventStorage eventStorage;

    public void add(Event event) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление event userId={}", event.getUserId());
        eventStorage.add(event);
    }

    public List<Event> getUserFeed(int userId) {
        return eventStorage.getUserEvents(userId);
    }


}

