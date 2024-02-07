package ru.yandex.practicum.javafilmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.JavaFilmorateApplication;
import ru.yandex.practicum.javafilmorate.model.Director;
import ru.yandex.practicum.javafilmorate.storage.dao.DirectorStorage;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorDbStorageTest {
    private final DirectorStorage directorStorage;
    private final Director director1 = new Director(null, "Name1");
    private final Director director2 = new Director(null, "Name2");
    private final Director director3 = new Director(null, "Name3");
    private final Director director4 = new Director(null, "Name4");

    @BeforeEach
    void createDirectors() {
        directorStorage.addDirector(director1);
        directorStorage.addDirector(director2);
        directorStorage.addDirector(director3);
        directorStorage.addDirector(director4);
    }

    @AfterEach
    void deleteDirector() {
        directorStorage.deleteDirector(director1.getId());
        directorStorage.deleteDirector(director2.getId());
        directorStorage.deleteDirector(director3.getId());
        directorStorage.deleteDirector(director4.getId());
    }

    @Test
    @DisplayName("Проверка метода findAll для Director")
    void testShouldGetDirectorListWithSize_4() {
        List<Director> directors = directorStorage.findAll();
        Assertions.assertEquals(directors.size(), 4, "Количество режиссёров в списке не совпадает");
    }

    @Test
    @DisplayName("Проверка метода findById для Director")
    void testShouldGetDirectorById() {
        Optional<Director> director = Optional.ofNullable(directorStorage.findById(director1.getId()));
        Assertions.assertTrue(director.isPresent(), "Получен null при попытке доступа к режиссёру по id");
        Assertions.assertEquals(director.get().getId(), director1.getId(), "Данные режиссёров не совпадают");
    }

    @Test
    @DisplayName("Проверка метода updateDirector для Director")
    void testShouldUpdatedDirector() {
        Director updatedDirector = new Director(director1.getId(), "UpdateName1");
        directorStorage.updateDirector(updatedDirector);
        String checkDirectorName = directorStorage.findById(director1.getId()).getName();
        Assertions.assertEquals(checkDirectorName, updatedDirector.getName(), "Не удалось обновить режиссёра");
    }

    @Test
    @DisplayName("Проверка методов addDirector и deleteDirector для Director")
    void testShouldDeleteDirectorById() {
        final Director testDirector = new Director(null, "TestName");
        directorStorage.addDirector(testDirector);
        List<Director> beforeDeletion = directorStorage.findAll();
        Assertions.assertEquals(beforeDeletion.size(), 5, "Количество режиссёров в списке не совпадает");
        directorStorage.deleteDirector(testDirector.getId());
        List<Director> afterDeletion = directorStorage.findAll();
        Assertions.assertEquals(afterDeletion.size(), 4, "Количество режиссёров в списке не совпадает");
    }
}
