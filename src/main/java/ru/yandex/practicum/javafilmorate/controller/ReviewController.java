package ru.yandex.practicum.javafilmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        log.info("КОНТРОЛЛЕР: POST-запрос по эндпоинту /reviews");
        return reviewService.add(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("КОНТРОЛЛЕР: PUT-запрос по эндпоинту /reviews");
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void removeReview(@PathVariable("id") Integer reviewID) {
        log.info("КОНТРОЛЛЕР: DELETE-запрос по эндпоинту /reviews/{}", reviewID);
        reviewService.removeReview(reviewID);
    }

    @GetMapping("/{id}")
    public Review findReview(@PathVariable("id") Integer reviewID) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /reviews/{}", reviewID);
        return reviewService.findReviewByID(reviewID);
    }

    @GetMapping
    List<Review> findAllReviews(@RequestParam(defaultValue = "0") Integer filmId,
                                @RequestParam(defaultValue = "10") Integer count) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /reviews");
        return reviewService.findReviewsByFilmID(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer reviewID, @PathVariable("userId") Integer userId) {
        log.info("КОНТРОЛЛЕР: PUT-запрос по эндпоинту /reviews/{}/like/{}", reviewID, userId);
        reviewService.addLike(reviewID, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable("id") Integer reviewID, @PathVariable("userId") Integer userId) {
        log.info("КОНТРОЛЛЕР: PUT-запрос по эндпоинту /reviews/{}/dislike/{}", reviewID, userId);
        reviewService.addDislike(reviewID, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer reviewID, @PathVariable("userId") Integer userId) {
        log.info("КОНТРОЛЛЕР: DELETE-запрос по эндпоинту /reviews/{}/like/{}", reviewID, userId);
        reviewService.addDislike(reviewID, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable("id") Integer reviewID, @PathVariable("userId") Integer userId) {
        log.info("КОНТРОЛЛЕР: DELETE-запрос по эндпоинту /reviews/{}/dislike/{}", reviewID, userId);
        reviewService.addLike(reviewID, userId);
    }
}
