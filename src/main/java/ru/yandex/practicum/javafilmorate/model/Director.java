package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Director {
    private Integer id;
    @NotBlank(message = "Имя режиссера не может быть пустым")
    private String name;
}
