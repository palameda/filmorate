package ru.yandex.practicum.javafilmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Mpa> getAll() {
        log.info("Получение списка рейтингов");
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getById(@PathVariable Integer id) {
        log.info("Получение рейтинга из базы данных по id = {}", id);
        return new ResponseEntity<>(mpaService.getById(id), HttpStatus.OK);
    }
}
