package ru.yandex.practicum.javafilmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        return reviewService.add(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void removeReview(@PathVariable("id") Integer reviewID) {
        reviewService.removeReview(reviewID);
    }

    @GetMapping("/{id}")
    public Review findReview(@PathVariable("id") Integer reviewID) {
        return reviewService.findReviewByID(reviewID);
    }

    @GetMapping
    List<Review> findAllReviews(@RequestParam(defaultValue = "0", required = false) Integer filmId,
                                @RequestParam(defaultValue = "10", required = false) Integer count) {
        return reviewService.findReviewsByFilmID(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer reviewID, @PathVariable("userId") Integer userId) {
        reviewService.addLike(reviewID, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable("id") Integer reviewID, @PathVariable("userId") Integer userId) {
        reviewService.addDislike(reviewID, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer reviewID, @PathVariable("userId") Integer userId) {
        reviewService.addDislike(reviewID, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable("id") Integer reviewID, @PathVariable("userId") Integer userId) {
        reviewService.addLike(reviewID, userId);
    }
}
