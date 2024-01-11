package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Genre {
    private final int id;
    @NotBlank(message = "Название жанра не может быть пустым")
    private final String name;
}
