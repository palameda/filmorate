package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;
import ru.yandex.practicum.javafilmorate.validation.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Размер описания не должен превышать 200 символов")
    private final String description;
    @ReleaseDateValidation(message = "Дата показа не может предшествовать 28 декабря 1895 года")
    private final LocalDate releaseDate;
    @NotNull
    @Positive(message = "Продолжительнось фильма должна быть больше 0")
    private final Integer duration;
    private final Mpa mpa;
    private final int likes;
    private Set<Genre> genres = new HashSet<>();
    private Set<Director> directors = new HashSet<>();

    public Film(Integer id, String name, String description, LocalDate releaseDate,
                Integer duration, Mpa mpa, int likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.likes = likes;
    }

    public Map<String, Object> filmToMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_NAME", name);
        values.put("FILM_DESCRIPTION", description);
        values.put("FILM_RELEASE_DATE", releaseDate);
        values.put("FILM_DURATION", duration);
        values.put("MPA_ID", mpa.getId());
        return values;
    }
}