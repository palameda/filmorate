package ru.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Data-класс Film является моделью сущности "Фильм" в сервисе Filmorate и содержит поля:
 * <ul>
 *     <li>ID - идентификатор фильма (натуральное число), назначается в классе-контроллере FilmController;</li>
 *     <li>title - наименование фильма, которое не может изменяться, быть пустым и содержать пробелы,
 *     проверяется с помощью аннотации @NotBlank;</li>
 *     <li>description - краткое описание фильма, которое не может изменяться и содержать более 200 символов,
 *     проверяется с помощью аннотации @Size;</li>
 *     <li>releaseDate - дата выхода фильма в прокат, которая не может изменяться и не может быть раньше 28 декабря 1895 года,
 *     проверяется с помощью custom-валидации;</li>
 *     <li>duration - продолжительность фильма, которая не может изменяться и должна быть положительным числом,
 *     проверяется с помощью аннотации @Positive</li>

 * </ul>
 * Аннотации @NotBlank, @Size и @Positive предоставляются библиотекой Lombok.<br>
 * Для создания экземпляра класса неявно используется @RequaredArgsConstructor, входящий в @Data,
 * инициализирующий все (final) поля, кроме поля ID.
 */

@Data
public class Film {
    private int ID;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}