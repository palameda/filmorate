package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class Review {
    private int reviewId;
    @NotBlank(message = "Описание отзыва является обязательным")
    @Size(max = 500, message = "Длина отзыва должна быть не больше {max} символов")
    private final String content;
    @NonNull
    private final Boolean isPositive;
    //@PositiveOrZero(message = "Идентификатор пользователя не может быть отрицательным")
    @NonNull
    private final Integer userId;
    //@PositiveOrZero(message = "Идентификатор фильма не может быть отрицательным")
    @NonNull
    private final Integer filmId;
    private int useful;
}
