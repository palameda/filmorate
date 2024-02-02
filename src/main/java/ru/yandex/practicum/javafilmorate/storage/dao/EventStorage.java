package ru.yandex.practicum.javafilmorate.storage.dao;

import ru.yandex.practicum.javafilmorate.model.Event;

import java.util.List;

public interface EventStorage {

    Event add(Event event);

    List<Event> getUserEvents(int userId);



}
