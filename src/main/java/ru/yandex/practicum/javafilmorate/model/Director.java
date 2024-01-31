package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Director {
    private Integer id;
    @NotBlank(message = "Имя режиссера не может быть пустым")
    private String name;
}
