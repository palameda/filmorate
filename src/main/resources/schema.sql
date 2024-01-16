CREATE TABLE IF NOT EXISTS "Films" (
    "FilmID" int   NOT NULL,
    "Name" varchar(50)   NOT NULL,
    "Description" varchar(200)   NOT NULL,
    "ReleaseDate" date   NOT NULL,
    "Duration" int   NOT NULL,
    "MpaID" int   NOT NULL,
    CONSTRAINT "pk_Films" PRIMARY KEY (
        "FilmID"
     )
);

CREATE TABLE IF NOT EXISTS "Mpa" (
    "MpaID" int   NOT NULL,
    "Mpa" varchar(10)   NOT NULL,
    "Description" varchar(200)   NOT NULL,
    CONSTRAINT "pk_Mpa" PRIMARY KEY (
        "MpaID"
     )
);

CREATE TABLE IF NOT EXISTS "Genres" (
    "GenreID" int   NOT NULL,
    "Name" varchar(100)   NOT NULL,
    CONSTRAINT "pk_Genres" PRIMARY KEY (
        "GenreID"
     )
);

CREATE TABLE IF NOT EXISTS "FilmGenres" (
    "ID" int   NOT NULL,
    "FilmID" int   NOT NULL,
    "GenreID" int   NOT NULL,
    CONSTRAINT "pk_FilmGenres" PRIMARY KEY (
        "ID"
     )
);

CREATE TABLE IF NOT EXISTS "Users" (
    "UserID" int   NOT NULL,
    "Email" varchar(200)   NOT NULL,
    "Login" varchar(50)   NOT NULL,
    "Name" varchar(200)   NOT NULL,
    "Birthday" date   NOT NULL,
    CONSTRAINT "pk_Users" PRIMARY KEY (
        "UserID"
     )
);

CREATE TABLE IF NOT EXISTS "Friends" (
    "ID" int   NOT NULL,
    "UserID" int   NOT NULL,
    "FriendID" int   NOT NULL,
    "Status" bool   NOT NULL,
    CONSTRAINT "pk_Friends" PRIMARY KEY (
        "ID"
     )
);

CREATE TABLE IF NOT EXISTS "Likes" (
    "LikeID" int   NOT NULL,
    "FilmID" int   NOT NULL,
    "UserID" int   NOT NULL,
    CONSTRAINT "pk_Likes" PRIMARY KEY (
        "LikeID"
     )
);

ALTER TABLE "Films" ADD CONSTRAINT "fk_Films_MpaID" FOREIGN KEY("MpaID")
REFERENCES "Mpa" ("MpaID");

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

