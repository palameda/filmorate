package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Mpa {
    private int id;
    @NotBlank(message = "Наименование рейтинга (MPA) не может быть пустым")
    private String name;
}
