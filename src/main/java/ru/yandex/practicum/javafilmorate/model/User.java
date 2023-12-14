package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    @Email(message = "Передан некорректный e-mail адрес")
    private String email;
    @NotBlank(message = "Поле login не должно быть пустым")
    private String login;
    private String name;
    @Past(message = "Дата рождения пользователя не может быть в будущем")
    private final LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
