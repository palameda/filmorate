package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public Map<String, Object> userRowMap() {
        Map<String, Object> userRow = new HashMap<>();
        userRow.put("USER_EMAIL", email);
        userRow.put("USER_LOGIN", login);
        userRow.put("USER_NAME", name);
        userRow.put("USER_BIRTHDAY", birthday);
        return userRow;
    }
}
