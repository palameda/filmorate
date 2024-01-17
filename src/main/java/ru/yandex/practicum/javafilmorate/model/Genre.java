package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Genre {
    private int id;
    @NotBlank(message = "Название жанра не может быть пустым")
    private String name;
}
