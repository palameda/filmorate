package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.dao.MpaStorage;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String findById(int id) {
        String sqlQuery = String.format("SELECT MPA_NAME FROM MPA WHERE MPA_ID = %d", id);
        List<String> mpaNames = jdbcTemplate.queryForList(sqlQuery, String.class);
        if (mpaNames.size() != 1) {
            throw new InvalidDataException("Передан некорректный id рейтинга");
        }
        log.info("Получение названия рейтинга по id");
        return mpaNames.get(0);
    }

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "SELECT MPA_ID, MPA_NAME FROM MPA";
        log.info("Получение списка рейтингов");
        return jdbcTemplate.query(sqlQuery, this::rowMapper);
    }

    private Mpa rowMapper(ResultSet rs, int rowNum) throws SQLException {
        log.info("Производится маппинг рейтинга");
        return new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"));
    }
}
