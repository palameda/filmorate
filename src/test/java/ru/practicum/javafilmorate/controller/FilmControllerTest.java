package ru.practicum.javafilmorate.controller;

import org.junit.jupiter.api.*;

import ru.practicum.javafilmorate.model.Film;
import ru.practicum.javafilmorate.utils.InvalidDataExcepion;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;



class FilmControllerTest {
    private FilmController controller;
    private static Validator validator;

    @BeforeAll
    public static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    public void initializeController() {
        controller = new FilmController();
    }

    @Test
    public void testFilmCreatedSuccessfully() {
        int initialID = 1;
        Film film = controller.createFilm(new Film("Once Upon a Time in America",
                "A former Prohibition-era Jewish gangster returns " +
                        "to the Lower East Side of Manhattan 35 years later, " +
                        "where he must once again confront the ghosts and regrets of his old life",
                LocalDate.of(1984, 6, 1), 229));

        List<Film> films = controller.findAll();

        Assertions.assertEquals(initialID, film.getID());
        Assertions.assertEquals(film, films.get(0)); // + проверка метода findAll при GET-запросе
    }

    @Test
    public void testAttemptToCreateFilmWithWrongTitle() {
        // 1 случай - пустая строка
        Set<ConstraintViolation<Film>> epmtyViolation = validator.validate(
                new Film("", "A former Prohibition-era Jewish gangster returns " +
                        "to the Lower East Side of Manhattan 35 years later, " +
                        "where he must once again confront the ghosts and regrets of his old life",
                        LocalDate.of(1984, 6, 1), 229));
        Assertions.assertFalse(epmtyViolation.isEmpty());
        // 2 случай - имя состоит только из пробелов
        Set<ConstraintViolation<Film>> spaceViolation = validator.validate(
                new Film(" ", "A former Prohibition-era Jewish gangster returns " +
                        "to the Lower East Side of Manhattan 35 years later, " +
                        "where he must once again confront the ghosts and regrets of his old life",
                        LocalDate.of(1984, 6, 1), 229));
        Assertions.assertFalse(spaceViolation.isEmpty());
        // 3 случай - null
        Set<ConstraintViolation<Film>> nullViolation = validator.validate(
                new Film(null, "A former Prohibition-era Jewish gangster returns " +
                        "to the Lower East Side of Manhattan 35 years later, " +
                        "where he must once again confront the ghosts and regrets of his old life",
                        LocalDate.of(1984, 6, 1), 229));
        Assertions.assertFalse(nullViolation.isEmpty());
    }

    @Test
    public void testAttemptToCreateFilmWithDescriptionLengthMoreThan200Symbols() {
        Set<ConstraintViolation<Film>> descriptionViolation = validator.validate(
                new Film("Once Upon a Time in America",
                        "Once Upon a Time in America (Italian: C'era una volta in America) " +
                                "is a 1984 epic crime film co-written and directed by Italian filmmaker Sergio Leone, " +
                                "and starring Robert De Niro and James Woods. The film is an Italian–American venture " +
                                "produced by The Ladd Company, Embassy International Pictures, " +
                                "PSO Enterprises and Rafran Cinematografica, and distributed by Warner Bros. " +
                                "Based on Harry Grey's novel The Hoods, it chronicles the lives of best friends " +
                                "David \"Noodles\" Aaronson and Maximilian \"Max\" Bercovicz " +
                                "as they lead a group of Jewish ghetto youths who rise to prominence " +
                                "as Jewish gangsters in New York City's world of organized crime. " +
                                "The film explores themes of childhood friendships, love, lust, greed, betrayal, " +
                                "loss, broken relationships, together with the rise of mobsters in American society.",
                        LocalDate.of(1984, 6, 1), 229));
        Assertions.assertFalse(descriptionViolation.isEmpty());
    }

    @Test
    public void testAttemptToCreateFilmWithWrongDate() {
        InvalidDataExcepion exception = Assertions.assertThrows(
                InvalidDataExcepion.class,
                () -> controller.createFilm(new Film("Matrix", "When a beautiful stranger " +
                        "leads computer hacker Neo to a forbidding underworld, " +
                        "he discovers the shocking truth--the life he knows " +
                        "is the elaborate deception of an evil cyber-intelligence",
                        LocalDate.of(1799, 3, 31), 136)));
        Assertions.assertEquals("Дата фильма не должна предшествовать 28.12.1895 г.", exception.getMessage());
    }

    @Test
    public void testAttemptToCreateFilmWithNonPositiveDuration() {
        // 1 случай - продолжительность отрицательная
        Set<ConstraintViolation<Film>> negativeViolation = validator.validate(new Film("Matrix",
                "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, " +
                        "he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence",
                        LocalDate.of(1999, 3, 31), -136));
        Assertions.assertFalse(negativeViolation.isEmpty());
        // 2 случай - продолжительность равна нулю
        Set<ConstraintViolation<Film>> zeroViolation = validator.validate(new Film("Matrix",
                "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, " +
                        "he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence",
                LocalDate.of(1999, 3, 31), 0));
        Assertions.assertFalse(zeroViolation.isEmpty());
    }

    @Test
    public void testAttemptToUpdateFilmWithNullID() {
        InvalidDataExcepion exception = Assertions.assertThrows(
                InvalidDataExcepion.class,
                () -> controller.updateFilm(new Film("Matrix", "When a beautiful stranger " +
                        "leads computer hacker Neo to a forbidding underworld, " +
                        "he discovers the shocking truth--the life he knows " +
                        "is the elaborate deception of an evil cyber-intelligence",
                        LocalDate.of(1999, 3, 31), 136)));
        Assertions.assertEquals("Данные о фильме отсутствуют в системе", exception.getMessage());
    }

    @Test
    public void testAttemptToUpdateRegisteredFilm() {
        Film matrix = new Film("Matrix", "When a beautiful stranger " +
                "leads computer hacker Neo to a forbidding underworld, " +
                "he discovers the shocking truth--the life he knows " +
                "is the elaborate deception of an evil cyber-intelligence",
                LocalDate.of(1999, 3, 31), 136);
        controller.createFilm(matrix);

        Film matrixRessurection = new Film("Matrix Ressurection", "Return to a world of two realities: " +
                "one, everyday life; the other, what lies behind it. To find out if his reality is a construct, " +
                "Mr. Anderson will have to choose to follow the white rabbit once more",
                LocalDate.of(2021, 12, 18), 148);
        matrixRessurection.setID(matrix.getID());
        controller.updateFilm(matrixRessurection);

        List<Film> films = controller.findAll();
        Assertions.assertNotEquals(matrix, films.get(0));
    }
}