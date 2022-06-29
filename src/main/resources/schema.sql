CREATE TABLE ACCOUNT
(
    ID       INT                 NOT NULL AUTO_INCREMENT,
    USERNAME VARCHAR(20) UNIQUE  NOT NULL,
    EMAIL    VARCHAR(320) UNIQUE NOT NULL,
    PASSWORD VARCHAR(100)        NOT NULL,

    PRIMARY KEY (ID)
);

CREATE TABLE LEARNING_SET
(
    ID                  INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TITLE               VARCHAR(100) NOT NULL,
    PUBLICLY_VISIBLE    BOOLEAN      NOT NULL,
    CREATION_TIME       TIMESTAMP    NOT NULL,
    TERM_LANGUAGE       VARCHAR(2)   NOT NULL,
    TRANSLATION_LANGUGE VARCHAR(2)   NOT NULL
--     AUTHOR_ID           INT        NOT NULL,
--
--     FOREIGN KEY (AUTHOR_ID) REFERENCES ACCOUNT (ID)
);

CREATE TABLE LEARNING_SET_ITEM
(
    SET_ID      INT          NOT NULL,
    ITEM_ID     INT          NOT NULL AUTO_INCREMENT,
    TERM        VARCHAR(255) NOT NULL,
    TRANSLATION VARCHAR(255) NOT NULL,

    PRIMARY KEY (SET_ID, ITEM_ID),
    FOREIGN KEY (SET_ID) REFERENCES LEARNING_SET (ID)
);
