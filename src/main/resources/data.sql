-- Заполнение данными таблицы рейтингов
MERGE INTO MPA (MPAID, NAME, DESCRIPTION)
VALUES (1, 'G', 'Без возрастных ограничений');
MERGE INTO MPA (MPAID, NAME, DESCRIPTION)
VALUES (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями');
MERGE INTO MPA (MPAID, NAME, DESCRIPTION)
VALUES (3, 'PG-13', 'Детям до 13 лет просмотр не желателен');
MERGE INTO MPA (MPAID, NAME, DESCRIPTION)
VALUES (4, 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого');
MERGE INTO MPA (MPAID, NAME, DESCRIPTION)
VALUES (5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');

-- Заполнение данными таблицы жанров
MERGE INTO GENRES (GENREID, NAME) VALUES (1, 'Комедия');
MERGE INTO GENRES (GENREID, NAME) VALUES (2, 'Драма');
MERGE INTO GENRES (GENREID, NAME) VALUES (3, 'Мультфильм');
MERGE INTO GENRES (GENREID, NAME) VALUES (4, 'Триллер');
MERGE INTO GENRES (GENREID, NAME) VALUES (5, 'Документальный');
MERGE INTO GENRES (GENREID, NAME) VALUES (6, 'Боевик');
