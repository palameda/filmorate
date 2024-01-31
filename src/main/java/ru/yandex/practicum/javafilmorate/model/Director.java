package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class Director {
    private Integer id;
    @NotBlank(message = "Имя режиссера не может быть пустым")
    private String name;

    public Map<String, Object> directorToMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("DIRECTOR_NAME", name);
        return values;
    }
}
