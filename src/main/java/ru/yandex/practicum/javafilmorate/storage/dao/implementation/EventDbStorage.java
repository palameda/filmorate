package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.EventType;
import ru.yandex.practicum.javafilmorate.model.OperationType;
import ru.yandex.practicum.javafilmorate.storage.dao.EventStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.FriendStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.UserStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Component()
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Event add(Event event) {
        log.info("ХРАНИЛИЩЕ: Добавление в хранилище event с userId={}", event.getUserId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Events")
                .usingGeneratedKeyColumns("EVENT_ID");
        Map<String, Object> params = Map.of("EVENT_TYPE", event.getEventType().toString(),
                "OPERATION_TYPE", event.getOperation().toString(), "ENTITY_ID", event.getEntityId(),
                "USER_ID", event.getUserId(), "EVENT_TIME", event.getTimestamp());
        event.setEventId(simpleJdbcInsert.executeAndReturnKey(params).intValue());
        return event;
    }

    public Event get(int eventId) {
        String sqlQuery = "SELECT * FROM Events e WHERE e.event_id=?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> Event.builder()
                .eventId(rs.getInt("EVENT_ID"))
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .operation(OperationType.valueOf(rs.getString("OPERATION_TYPE")))
                .entityId(rs.getInt("ENTITY_ID"))
                .userId(rs.getInt("USER_ID"))
                .timestamp(rs.getLong("EVENT_TIME"))
                .build(), eventId);
    }

    public List<Event> getUserEvents(int userId) {
        isRegistered(userId);
        String sqlQuery = "SELECT * FROM Events e WHERE e.USER_ID=?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> Event.builder()
                .eventId(rs.getInt("EVENT_ID"))
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .operation(OperationType.valueOf(rs.getString("OPERATION_TYPE")))
                .entityId(rs.getInt("ENTITY_ID"))
                .userId(rs.getInt("USER_ID"))
                .timestamp(rs.getLong("EVENT_TIME"))
                .build(), userId);
    }

    public boolean deleteEvent(int entityID, EventType type) {
        log.info("ХРАНИЛИЩЕ: Удаление из хранилища события с сущностью id {} и типом {}", entityID, type);
        String sqlQuery = "DELETE FROM Events WHERE ENTITY_ID=? AND EVENT_TYPE =?";
        return jdbcTemplate.update(sqlQuery, entityID, type.toString() ) > 0;
    }

    private void isRegistered(int userId) {
        log.info("ХРАНИЛИЩЕ: Проверка регистрации пользователя в системе");
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        if (!userRow.next()) {
            throw new UnregisteredDataException("Пользователь с id " + userId + " не зарегистрирован в системе");
        }
    }

}
