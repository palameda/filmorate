package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.EventType;
import ru.yandex.practicum.javafilmorate.model.OperationType;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.storage.dao.ReviewStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;


import java.util.List;

@Slf4j
@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final EventService eventService;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, EventService eventService) {
        this.reviewStorage = reviewStorage;
        this.eventService = eventService;
    }

    public Review add(Review review) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление отзыва с id {}", review.getReviewId());
        if (review.getUserId() < 0)
            throw new UnregisteredDataException("Идентификатор пользователя не может быть отрицательным");
        if (review.getFilmId() < 0)
            throw new UnregisteredDataException("Идентификатор фильма не может быть отрицательным");
        Review addReview = reviewStorage.add(review);
        eventService.add(new Event(EventType.REVIEW, OperationType.ADD, review.getReviewId(), review.getUserId()));
        return addReview;
    }

    public Review update(Review review) {
        Review oldReview = findReviewByID(review.getReviewId());
        log.info("СЕРВИС: Отправлен запрос к хранилищу на обновление отзыва с id {}", review.getReviewId());
        Review newReview = reviewStorage.update(review);
        eventService.add(new Event(EventType.REVIEW, OperationType.UPDATE, oldReview.getReviewId(), oldReview.getUserId()));
        return newReview;
    }

    public List<Review> findReviewsByFilmID(int filmID, int count) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка отзывов на фильм с id {}", filmID);
        return (filmID != 0) ? reviewStorage.findReviewsByFilmID(filmID, count) : reviewStorage.findAllReviews(count);
    }

    public Review findReviewByID(int reviewID) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение отзыва по id {}", reviewID);
        Review review = reviewStorage.findReviewByID(reviewID);
        if (review != null) {
            return review;
        } else throw new UnregisteredDataException("Нет отзыва с ID: " + reviewID);
    }

    public void removeReview(int reviewID) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на удаление отзыва по id {}", reviewID);
        int userId = reviewStorage.findReviewByID(reviewID).getUserId();
        reviewStorage.removeReview(reviewID);
        eventService.add(new Event(EventType.REVIEW, OperationType.REMOVE, reviewID, userId));
    }

    public void addLike(int reviewID, int userID) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление лайка на отзыв по id {} от пользователя с id {} ",
                reviewID, userID);
        Review review = findReviewByID(reviewID);
        review.setUseful(review.getUseful() + 1);
        reviewStorage.updateUseful(review);
    }

    public void addDislike(int reviewID, int userID) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление дислайка на отзыв по id {} от пользователя с id {} ",
                reviewID, userID);
        Review review = findReviewByID(reviewID);
        review.setUseful(review.getUseful() - 1);
        reviewStorage.updateUseful(review);
    }
}
