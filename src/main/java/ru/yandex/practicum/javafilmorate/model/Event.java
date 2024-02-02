package ru.yandex.practicum.javafilmorate.model;

import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Event {
    private int eventId;
    @NonNull
    private EventType eventType;
    @NonNull
    private OperationType operation;
    @NonNull
    private int entityId;
    @NonNull
    private int userId;
    @NonNull
    private long timestamp;

    public Event(@NonNull EventType eventType, @NonNull OperationType operation, @NonNull int entityId, @NonNull int userId) {
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
        this.userId = userId;
        timestamp = Instant.now().toEpochMilli();
    }
}
