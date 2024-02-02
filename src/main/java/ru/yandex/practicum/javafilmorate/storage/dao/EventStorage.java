package ru.yandex.practicum.javafilmorate.storage.dao;

import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.EventType;
import ru.yandex.practicum.javafilmorate.model.OperationType;

import java.time.Instant;
import java.util.List;

public interface EventStorage {

    Event add(Event event);

    boolean deleteEvent(int entityID, EventType type);

    List<Event> getUserEvents(int userId);



}
