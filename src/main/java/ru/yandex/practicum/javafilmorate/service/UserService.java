package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.*;
import ru.yandex.practicum.javafilmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.FriendStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.UserStorage;
import ru.yandex.practicum.javafilmorate.utils.CheckUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FriendStorage friendStorage;
    private final EventService eventService;

    public User addUser(User user) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление пользователя с id {}", user.getId());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на обновление пользователя с id {}", user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> findAll() {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка всех пользователей");
        return userStorage.findAll();
    }

    public void addFriend(Integer userId, Integer friendId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление пользователю с id {} друга с id {}",
                userId, friendId);
        friendStorage.addFriend(userId, friendId);
        eventService.add(new Event(EventType.FRIEND, OperationType.ADD, friendId, userId));
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на удаление у пользователя с id {} друга с id {}",
                userId, friendId);
        friendStorage.deleteFriend(userId, friendId);
        eventService.add(new Event(EventType.FRIEND, OperationType.REMOVE, friendId, userId));
    }

    public List<User> getUserFriends(Integer userId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка всех друзей пользователя с id {}", userId);
        return friendStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка общих друзей пользователей с id {} и id {}",
                userId, friendId);
        return friendStorage.getCommonsFriends(userId, friendId);
    }

    public User findById(Integer userId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение пользователя по id {}", userId);
        return userStorage.findById(userId);
    }

    public void deleteUser(int userId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на удаление у пользователя с id {}.", userId);
        CheckUtil.checkNotFound(userStorage.deleteUser(userId), " пользователь с id=" + userId);
    }

    public List<Film> findRecommendationsForUser(Integer userId) {
        log.info("СЕРВИС: Обработка запроса на рекомендации фильмов для пользователя с id {}", userId);
        /* Получаем матрицу всех лайков: каждый элемент содержит: Id фильма + список лайков этому фильму */
        Map<Integer, Set<Integer>> likes = userStorage.getAllLikes();
        /* Формируем список Id фильмов, которые лайкнул юзер, и список наборов лайков, где присутствует юзер */
        List<Integer> userFilmsListId = new ArrayList<>();
        List<Set<Integer>> listsIdUserPresent = new ArrayList<>();
        for (Map.Entry<Integer, Set<Integer>> filmLikes : likes.entrySet())
            if (filmLikes.getValue().contains(userId)) {
                userFilmsListId.add(filmLikes.getKey());
                listsIdUserPresent.add(filmLikes.getValue());
            }
        /* Определяем, какой юзер(похожий) чаще всего лайкал те же фильмы, что и юзер */
        int similarUserId = findSimilarUserId(listsIdUserPresent, userId);
        /* Заполняем список Id фильмов, которые лайкнул похожий юзер  */
        List<Integer> similarUserFilmsListId = new ArrayList<>();
        for (Map.Entry<Integer, Set<Integer>> filmLikes : likes.entrySet())
            if (filmLikes.getValue().contains(similarUserId))
                similarUserFilmsListId.add(filmLikes.getKey());
        /* Сравниваем списки фильмов, оставляем фильмы, которые лайкнул похожий юзер, а основной не лайкнул  */
        similarUserFilmsListId.removeAll(userFilmsListId);
        /* Получаем фильмы по Id  */
        List<Film> films = new ArrayList<>();
        similarUserFilmsListId.forEach(filmId ->
                films.add(filmStorage.findById(filmId))
        );

        return films;
    }

    private int findSimilarUserId(List<Set<Integer>> listsIdUserPresent, int userId) {
        int similarUserId = 0;
        int counterSimilarFilms = 0;
        /* Идем по списку наборов лайков для каждого фильма */
        for (int i = 0; i < listsIdUserPresent.size() && counterSimilarFilms < (listsIdUserPresent.size() - i); i++)
            /* Идем по конкретному набору лайков для конкретного фильиа */
            for (int candidateUserId : listsIdUserPresent.get(i))
                if (candidateUserId != userId && candidateUserId != similarUserId) {
                    /* Счетчик начинается с 1, т.к. кандидат в одном начальном наборе лайков с основным юзером */
                    int tmpCounter = 1;
                    /* Проходим по наборам лайков после текущего. Если находим кандидата в списке, то увеличиваем счетчик */
                    for (int j = i + 1; j < listsIdUserPresent.size(); j++)
                        if (listsIdUserPresent.get(j).contains(candidateUserId))
                            tmpCounter++;
                    /* Счетчик текущего кандидата сравниваем со счетчиком последнего похожего юзера */
                    if (tmpCounter > counterSimilarFilms) {
                        counterSimilarFilms = tmpCounter;
                        similarUserId = candidateUserId;
                    }
                }
        return similarUserId;
    }
}
