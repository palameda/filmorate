package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.storage.dao.ReviewDao;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.util.List;

@Slf4j
@Service
public class ReviewService {
    @Autowired
    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public Review add(Review review) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление отзыва с id {}", review.getReviewId());
        if (review.getUserId() < 0)
            throw new UnregisteredDataException("Идентификатор пользователя не может быть отрицательным");
        if (review.getFilmId() < 0)
            throw new UnregisteredDataException("Идентификатор фильма не может быть отрицательным");
        return reviewDao.add(review);
    }

    public Review update(Review review) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на обновление отзыва с id {}", review.getReviewId());
        return reviewDao.update(review);
    }

    public List<Review> findReviewsByFilmID(int filmID, int count) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка отзывов на фильм с id {}", filmID);
        return (filmID != 0) ? reviewDao.findReviewsByFilmID(filmID, count) : reviewDao.findAllReviews(count);
    }

    public Review findReviewByID(int reviewID) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение отзыва по id {}", reviewID);
        Review review = reviewDao.findReviewByID(reviewID);
        if (review != null) {
            return review;
        } else throw new UnregisteredDataException("Нет отзыва с ID: " + reviewID);
    }

    public void removeReview(int reviewID) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на удаление отзыва по id {}", reviewID);
        reviewDao.removeReview(reviewID);
    }

    public void addLike(int reviewID, int userID) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление лайка на отзыв по id {} от пользователя с id {} ",
                reviewID, userID);
        Review review = findReviewByID(reviewID);
        review.setUseful(review.getUseful() + 1);
        reviewDao.updateUseful(review);
    }

    public void addDislike(int reviewID, int userID) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление дислайка на отзыв по id {} от пользователя с id {} ",
                reviewID, userID);
        Review review = findReviewByID(reviewID);
        review.setUseful(review.getUseful() - 1);
        reviewDao.updateUseful(review);
    }
}
