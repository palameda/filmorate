package ru.yandex.practicum.javafilmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Director;
import ru.yandex.practicum.javafilmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Director> findAll() {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /directors");
        return directorService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Director findById(@PathVariable Integer id) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /directors/{}", id);
        return directorService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director addDirector(@Valid @RequestBody Director director) {
        log.info("КОНТРОЛЛЕР: POST-запрос по эндпоинту /directors");
        return directorService.addDirector(director);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.info("КОНТРОЛЛЕР: PUT-запрос по эндпоинту /directors");
        return directorService.updateDirector(director);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDirector(@PathVariable Integer id) {
        log.info("КОНТРОЛЛЕР: DELETE-запрос по эндпоинту /directors/{}", id);
        directorService.deleteDirector(id);
    }
}
