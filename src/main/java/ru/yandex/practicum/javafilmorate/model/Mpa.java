package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Mpa {
    private final int id;
    @NotBlank(message = "Наименование рейтинга (MPA) не может быть пустым")
    private final String rating;
}
