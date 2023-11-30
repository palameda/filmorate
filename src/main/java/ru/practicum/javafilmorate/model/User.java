package ru.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

/**
 * Data-класс User является моделью сущности "Пользователь" сервиса Filmorate и содержит поля:
 * <ul>
 *     <li>ID - идентификатор пользователя (натуральное число), назначается в классе-контроллере UserController;</li>
 *     <li>email - адрес электронной почты, который должен соответствовать общепринятому
 *     шаблону и проверяется с помощью аннотации @Email;
 *     </li>
 *     <li>login - логин пользователя в сервисе, который не может быть
 *  *     пустой строкой и содержать пробелы, проверяется с помощью аннотации @NotBlank;</li>
 *     <li>name - имя пользователя, которое может быть пустым и заменяется на login;</li>
 *     <li>birthday - дата рождения пользователя, которая не может быть будущей датой относительно текущей и
 *     проверяется с помощью аннотации @Past.</li>
 * </ul>
 * Аннотации предоставляются библиотекой Lombok.<br>
 * Для создания экземпляра класса используется конструктор, инициализирующий все поля, кроме поля ID.
 */
@Data
public class User {
    private int ID;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private final LocalDate birthday;

    User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
