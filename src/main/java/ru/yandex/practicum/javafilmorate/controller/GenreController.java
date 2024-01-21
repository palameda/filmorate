package ru.yandex.practicum.javafilmorate.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.service.GenreService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping()
    public List<Genre> findAll() {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /genres");
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable Integer id) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /genres/{}", id);
        return genreService.findById(id);
    }
}
