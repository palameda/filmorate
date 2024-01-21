package ru.yandex.practicum.javafilmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.service.MpaService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping()
    public List<Mpa> findAll() {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /mpa");
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable Integer id) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /genres/{}", id);
        return mpaService.findById(id);
    }
}
