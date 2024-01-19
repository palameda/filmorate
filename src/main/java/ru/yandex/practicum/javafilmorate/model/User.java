package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Integer id;
    @Email(message = "Передан некорректный e-mail адрес")
    private String email;
    @NotBlank(message = "Поле login не должно быть пустым")
    private String login;
    private String name;
    @Past(message = "Дата рождения пользователя не может быть в будущем")
    private final LocalDate birthday;
    private Set<Integer> friends;

    public Map<String, Object> userRowMap() {
        Map<String, Object> userRow = new HashMap<>();
        userRow.put("USER_EMAIL", email);
        userRow.put("USER_LOGIN", login);
        userRow.put("USER_NAME", name);
        userRow.put("USER_BIRTHDAY", birthday);
        return userRow;
    }
}
