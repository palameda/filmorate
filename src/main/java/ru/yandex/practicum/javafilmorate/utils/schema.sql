
CREATE TABLE "Films" (
    "FilmID" int   NOT NULL,
    "Name" varchar(50)   NOT NULL,
    "Description" varchar(200)   NOT NULL,
    "ReleaseDate" date   NOT NULL,
    "Duration" int   NOT NULL,
    "MPA" varchar(10)   NOT NULL,
    CONSTRAINT "pk_Films" PRIMARY KEY (
        "FilmID"
     )
);

CREATE TABLE "Mpa" (
    "MPA" varchar(10)   NOT NULL,
    "Description" varchar(200)   NOT NULL,
    CONSTRAINT "pk_Mpa" PRIMARY KEY (
        "MPA"
     )
);

CREATE TABLE "Genres" (
    "GenreID" int   NOT NULL,
    "Name" varchar(10)   NOT NULL,
    CONSTRAINT "pk_Genres" PRIMARY KEY (
        "GenreID"
     )
);

CREATE TABLE "FilmGenres" (
    "ID" int   NOT NULL,
    "FilmID" int   NOT NULL,
    "GenreID" int   NOT NULL,
    CONSTRAINT "pk_FilmGenres" PRIMARY KEY (
        "ID"
     )
);

CREATE TABLE "Users" (
    "UserID" int   NOT NULL,
    "Email" varchar(200)   NOT NULL,
    "Login" varchar(50)   NOT NULL,
    "Name" varchar(200)   NOT NULL,
    "Birthday" date   NOT NULL,
    CONSTRAINT "pk_Users" PRIMARY KEY (
        "UserID"
     )
);

CREATE TABLE "Friends" (
    "ID" int   NOT NULL,
    "UserID" int   NOT NULL,
    "FriendID" int   NOT NULL,
    "Status" bool   NOT NULL,
    CONSTRAINT "pk_Friends" PRIMARY KEY (
        "ID"
     )
);

CREATE TABLE "Likes" (
    "LikeID" int   NOT NULL,
    "FilmID" int   NOT NULL,
    "UserID" int   NOT NULL,
    CONSTRAINT "pk_Likes" PRIMARY KEY (
        "LikeID"
     )
);

ALTER TABLE "Films" ADD CONSTRAINT "fk_Films_MPA" FOREIGN KEY("MPA")
REFERENCES "Mpa" ("MPA");

ALTER TABLE "FilmGenres" ADD CONSTRAINT "fk_FilmGenres_FilmID" FOREIGN KEY("FilmID")
REFERENCES "Films" ("FilmID");

ALTER TABLE "FilmGenres" ADD CONSTRAINT "fk_FilmGenres_GenreID" FOREIGN KEY("GenreID")
REFERENCES "Genres" ("GenreID");

ALTER TABLE "Friends" ADD CONSTRAINT "fk_Friends_UserID" FOREIGN KEY("UserID")
REFERENCES "Users" ("UserID");

ALTER TABLE "Friends" ADD CONSTRAINT "fk_Friends_FriendID" FOREIGN KEY("FriendID")
REFERENCES "Users" ("UserID");

ALTER TABLE "Likes" ADD CONSTRAINT "fk_Likes_FilmID" FOREIGN KEY("FilmID")
REFERENCES "Films" ("FilmID");

ALTER TABLE "Likes" ADD CONSTRAINT "fk_Likes_UserID" FOREIGN KEY("UserID")
REFERENCES "Users" ("UserID");

