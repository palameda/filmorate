package ru.yandex.practicum.javafilmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.javafilmorate.JavaFilmorateApplication;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.EventType;
import ru.yandex.practicum.javafilmorate.model.OperationType;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.EventDbStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class EventDbStorageTest {

    private final UserDbStorage userDbStorage;
    private final EventDbStorage eventDbStorage;
    private final User firstUser = new User(1, "email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"), null);
    private final Event event1 = new Event(EventType.REVIEW, OperationType.ADD, 111, 1);
    private final Event event2 = new Event(EventType.REVIEW, OperationType.REMOVE, 222, 1);
    private final Event event3 = new Event(EventType.REVIEW, OperationType.UPDATE, 333, 1);

    @BeforeEach
    void setUp() {
        userDbStorage.addUser(firstUser);
        eventDbStorage.add(event1);
        eventDbStorage.add(event2);
    }

    @Test
    @DisplayName("Тест метода Add для добавления Event в ленту")
    void testShouldAddEvent() {
        Optional<Event> event = Optional.of(eventDbStorage.add(event3));
        System.out.println(event);
        Assertions.assertThat(event.isPresent());
        Assertions.assertThat(event.stream().anyMatch(e -> e.getEventId() == 3));
        Assertions.assertThat(event.stream().anyMatch(e -> e.getEventType().equals("REVIEW")));
        Assertions.assertThat(event.stream().anyMatch(e -> e.getOperation().equals("UPDATE")));
        Assertions.assertThat(event.stream().anyMatch(e -> e.getEventId() == 333));
    }

    @Test
    @DisplayName("Тест метода getUserFeed для получения ленты событий по id пользователя")
    void testShouldReturnUserFeed() {
        List<Event> events = eventDbStorage.getUserEvents(firstUser.getId());
        System.out.println(events);
        Optional<Event> event = Optional.of(events.get(0));
        Assertions.assertThat(event.isPresent());
        Assertions.assertThat(event.stream().anyMatch(e -> e.getEventId() == 1));
        Assertions.assertThat(event.stream().anyMatch(e -> e.getEventType().equals("REVIEW")));
        Assertions.assertThat(event.stream().anyMatch(e -> e.getOperation().equals("ADD")));
        Assertions.assertThat(event.stream().anyMatch(e -> e.getEventId() == 111));
    }
}