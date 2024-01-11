package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Mpa {
    @NotBlank(message = "Наименование рейтинга (MPA) не может быть пустым")
    private final String rating;
    @Size(max = 200, message = "Описание рейтинга (MPA) не может превышать 200 символов")
    private final String description;
}
