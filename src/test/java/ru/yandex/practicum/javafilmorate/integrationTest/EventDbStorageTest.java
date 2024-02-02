package ru.yandex.practicum.javafilmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.javafilmorate.JavaFilmorateApplication;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.EventType;
import ru.yandex.practicum.javafilmorate.model.OperationType;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.EventDbStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.UserDbStorage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class EventDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private final EventDbStorage eventDbStorage;

    private final User firstUser = new User(1, "email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"), null);
    private final Event event_1 = new Event(EventType.REVIEW, OperationType.ADD, 111, 1);
    private final Event event_2 = new Event(EventType.REVIEW, OperationType.REMOVE, 222, 1);
    private final Event event_3 = new Event(EventType.REVIEW, OperationType.UPDATE, 333, 1);

    @BeforeEach
    void setUp() {
        userDbStorage.addUser(firstUser);

        eventDbStorage.add(event_1);
        eventDbStorage.add(event_2);

    }

    @Test
    void testAdd() {
        Event event = eventDbStorage.add(event_3);
        System.out.println(event);
    }

    @Test
    void testGet() {
        Event event = eventDbStorage.get(1);
        System.out.println(event);
    }

    @Test
    void testGetUserFeed() {
        List<Event> events = eventDbStorage.getUserEvents(1);
        System.out.println(events);
    }
}