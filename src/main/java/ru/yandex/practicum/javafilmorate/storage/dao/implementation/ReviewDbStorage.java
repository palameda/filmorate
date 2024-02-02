package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.EventType;
import ru.yandex.practicum.javafilmorate.model.OperationType;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.service.EventService;
import ru.yandex.practicum.javafilmorate.storage.dao.ReviewStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final EventService eventService;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate, EventService eventService) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventService = eventService;
    }

    @Override
    public Review add(Review review) {
        log.info("ХРАНИЛИЩЕ: Добавление отзыва в хранилище от пользователя с id {} на фильм с id {}",
                review.getUserId(), review.getFilmId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .withTableName("reviews")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> params = Map.of("CONTENT", review.getContent(), "IS_POSITIVE", review.getIsPositive(),
                "USER_ID", review.getUserId(), "FILM_ID", review.getFilmId());
        try {
            review.setReviewId(simpleJdbcInsert.executeAndReturnKey(params).intValue());
            eventService.add(new Event(EventType.REVIEW, OperationType.ADD, review.getReviewId(), review.getUserId()));
            return review;
        } catch (DataIntegrityViolationException e) {
            throw new UnregisteredDataException("Определен несуществующий пользователь или фильм");
        }
    }

    @Override
    public Review update(Review review) {
        log.info("ХРАНИЛИЩЕ: Обновление данных по отзыву с id {}", review.getReviewId());
        Review updateReview = findReviewByID(review.getReviewId());
        jdbcTemplate.update("UPDATE reviews SET CONTENT = ?,  IS_POSITIVE = ? WHERE ID = ?",
                review.getContent(), review.getIsPositive(), review.getReviewId());
        eventService.add(new Event(EventType.REVIEW, OperationType.UPDATE, review.getReviewId(), updateReview.getUserId()));
//        eventService.add(new Event(EventType.REVIEW, OperationType.UPDATE, review.getReviewId(), review.getUserId()));
        return findReviewByID(review.getReviewId());
    }

    @Override
    public void updateUseful(Review review) {
        log.info("ХРАНИЛИЩЕ: Обновление данных о полезности отзыва с id {}", review.getReviewId());
        Review updateReview = findReviewByID(review.getReviewId());
        jdbcTemplate.update("UPDATE reviews SET USEFUL = ?  WHERE ID = ?",
                review.getUseful(), review.getReviewId());
    }

    @Override
    public List<Review> findAllReviews(int count) {
        log.info("ХРАНИЛИЩЕ: Получение списка {} самых полезных отзывов", count);
        return jdbcTemplate.query("SELECT * FROM reviews", (rs, rowNum) -> makeReviewForList(rs))
                .stream()
                .sorted(Comparator.comparingInt(Review::getUseful).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findReviewsByFilmID(int filmID, int count) {
        log.info("ХРАНИЛИЩЕ: Получение списка {} самых полезных отзывов на фильм с id {}", count, filmID);
        return jdbcTemplate.query("SELECT * FROM reviews WHERE FILM_ID = ?", (rs, rowNum) -> makeReviewForList(rs), filmID)
                .stream()
                .sorted(Comparator.comparingInt(Review::getUseful).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Review findReviewByID(int reviewID) {
        log.info("ХРАНИЛИЩЕ: Получение отзыва по id {}", reviewID);
        return makeReview(jdbcTemplate.queryForRowSet("SELECT * FROM reviews WHERE ID = ? LIMIT 1", reviewID));
    }

    @Override
    public void removeReview(int reviewID) {
        log.info("ХРАНИЛИЩЕ: Удаление из хранилища отзыва с id {}", reviewID);
        eventService.add(new Event(EventType.REVIEW, OperationType.REMOVE, reviewID, findReviewByID(reviewID).getUserId()));
        jdbcTemplate.update("DELETE FROM reviews WHERE ID = ?", reviewID);
    }

    private Review makeReviewForList(ResultSet rs) throws SQLException {
        return new Review(
                rs.getInt("ID"),
                rs.getString("CONTENT"),
                rs.getBoolean("IS_POSITIVE"),
                rs.getInt("USER_ID"),
                rs.getInt("FILM_ID"),
                rs.getInt("USEFUL"));
    }

    private Review makeReview(SqlRowSet reviewRows) {
        Review review = null;
        if (reviewRows.next()) {
            review = new Review(
                    reviewRows.getInt("ID"),
                    reviewRows.getString("CONTENT"),
                    reviewRows.getBoolean("IS_POSITIVE"),
                    reviewRows.getInt("USER_ID"),
                    reviewRows.getInt("FILM_ID"),
                    reviewRows.getInt("USEFUL"));
        }
        return review;
    }
}
