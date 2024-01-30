package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.storage.dao.ReviewDao;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public Review add(Review review) {
        if (review.getUserId() < 0)
            throw new UnregisteredDataException("Идентификатор пользователя не может быть отрицательным");
        if (review.getFilmId() < 0)
            throw new UnregisteredDataException("Идентификатор фильма не может быть отрицательным");
        return reviewDao.add(review);
    }

    public Review update(Review review) {
        return reviewDao.update(review);
    }

    public List<Review> findReviewsByFilmID(int filmID, int count) {
        return (filmID != 0) ? reviewDao.findReviewsByFilmID(filmID, count) : reviewDao.findAllReviews(count);
    }

    public Review findReviewByID(int reviewID) {
        Review review = reviewDao.findReviewByID(reviewID);
        if (review != null) {
            return review;
        } else throw new UnregisteredDataException("Нет отзыва с ID: " + reviewID);
    }

    public void removeReview(int reviewID) {
        reviewDao.removeReview(reviewID);
    }

    public void addLike(int reviewID, int userID) {
        Review review = findReviewByID(reviewID);
        review.setUseful(review.getUseful() + 1);
        reviewDao.updateUseful(review);
    }

    public void addDislike(int reviewID, int userID) {
        Review review = findReviewByID(reviewID);
        review.setUseful(review.getUseful() - 1);
        reviewDao.updateUseful(review);
    }
}
