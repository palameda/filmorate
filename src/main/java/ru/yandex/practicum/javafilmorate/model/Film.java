package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Размер описания не должен превышать 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = "Продолжительнось фильма должна быть больше 0")
    private final int duration;
    private final Set<Integer> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
    private final Mpa mpa;
}