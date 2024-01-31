package ru.yandex.practicum.javafilmorate.storage.dao;

import ru.yandex.practicum.javafilmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    public Review add(Review review);

    public Review update(Review review);

    public void updateUseful(Review review);

    public List<Review> findAllReviews(int count);

    public List<Review> findReviewsByFilmID(int filmID, int count);

    public Review findReviewByID(int reviewID);

    public void removeReview(int reviewID);

}
