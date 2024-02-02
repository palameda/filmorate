package ru.yandex.practicum.javafilmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.EventService;
import ru.yandex.practicum.javafilmorate.storage.dao.ReviewStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.*;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private ReviewStorage reviewStorage;

    private EventService eventService;
    private int film1Id, film2Id;
    private int user1Id, user2Id, user3Id;

    @BeforeEach
    void beforeEach() {
        reviewStorage = new ReviewDbStorage(jdbcTemplate, eventService);
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        DirectorDbStorage directorDbStorage = new DirectorDbStorage(jdbcTemplate);
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate, mpaDbStorage, genreStorage, directorDbStorage);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        Film film1 = new Film(null, "Film1", "Description1", LocalDate.parse("1970-01-01"),
                140, new Mpa(1, "G"), 0);
        filmStorage.addFilm(film1);
        film1Id = film1.getId();
        Film film2 = new Film(null, "Film2", "Description2", LocalDate.parse("1980-01-01"),
                90, new Mpa(2, "PG"), 0);
        filmStorage.addFilm(film2);
        film2Id = film2.getId();

        User user1 = new User(1, "email1@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"), null);
        userDbStorage.addUser(user1);
        user1Id = user1.getId();
        User user2 = new User(2, "email2@gmail.com", "Login2", "Name2", LocalDate.parse("1980-01-01"), null);
        userDbStorage.addUser(user2);
        user2Id = user2.getId();
        User user3 = new User(3, "email3@gmail.com", "Login3", "Name3", LocalDate.parse("1990-01-01"), null);
        userDbStorage.addUser(user3);
        user3Id = user3.getId();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void testAddUpdateGetReview() {
        /* Создаем первый отзыв и получаем его обратно из БД. Сравниваем. */
        Review review1 = new Review(0, "ReviewUser1Film1", false, user1Id, film1Id, 0);
        int review1Id = reviewStorage.add(review1).getReviewId();
        Review testReviewAfterAdd = reviewStorage.findReviewByID(review1Id);
        assertThat(testReviewAfterAdd)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(review1);
        /* Создаем второй и третий отзывы. Получаем их из БД вместе и по ID фильма */
        Review review2 = new Review(0, "ReviewUser2Film1", false, user2Id, film1Id, 0);
        int review2Id = reviewStorage.add(review2).getReviewId();
        Review review3 = new Review(0, "ReviewUser1Film2", false, user1Id, film2Id, 0);
        int review3Id = reviewStorage.add(review3).getReviewId();
        List<Review> reviewList = reviewStorage.findAllReviews(10);
        assertThat(reviewList.size()).isEqualTo(3);
        reviewList = reviewStorage.findReviewsByFilmID(film1Id, 10);
        assertThat(reviewList.size()).isEqualTo(2);
        reviewList = reviewStorage.findReviewsByFilmID(film2Id, 10);
        assertThat(reviewList.size()).isEqualTo(1);
        /* Получаем отзывы с ограничением количества */
        reviewList = reviewStorage.findAllReviews(2);
        assertThat(reviewList.size()).isEqualTo(2);
        /* Меняем контекст и оценку фильма в отзыве. Данные о пользователе, фильме и полезности могут быть любые - ни на что не влияют */
        Review updatedReview = new Review(review1Id, "updatedReviewContext", true, 9999, 8888, 123);
        reviewStorage.update(updatedReview);
        Review testReviewAfterUpdate = reviewStorage.findReviewByID(review1Id);
        assertThat(testReviewAfterUpdate.getContent()).isEqualTo("updatedReviewContext");
        assertThat(testReviewAfterUpdate.getIsPositive()).isEqualTo(true);
        /* Пытаемся изменить данные у отзыва с несуществующим Id и проверяем попал ли новый отзыв в БД*/
        Review updatedReviewWithBadId = new Review(999, "updatedReviewContext", true, 9999, 8888, 123);
        reviewStorage.update(updatedReviewWithBadId);
        Review testReviewAfterBadUpdate = reviewStorage.findReviewByID(999);
        assertNull(testReviewAfterBadUpdate);
        /* Добавляем отзывы несуществующего юзера или о несуществующем фильме: должны получить исключение */
        Review reviewFromNotExistUser = new Review(0, "ReviewFromNotExistUser", false, 999, film2Id, 0);
        Review reviewFromNotExistFilm = new Review(0, "ReviewFromNotExistFilm", false, user2Id, 999, 0);
        final UnregisteredDataException exceptionNotUser = assertThrows(
                UnregisteredDataException.class, () -> reviewStorage.add(reviewFromNotExistUser));
        assertEquals("Определен несуществующий пользователь или фильм", exceptionNotUser.getMessage());
        final UnregisteredDataException exceptionNotFilm = assertThrows(
                UnregisteredDataException.class, () -> reviewStorage.add(reviewFromNotExistFilm));
        assertEquals("Определен несуществующий пользователь или фильм", exceptionNotFilm.getMessage());
    }
}
