CREATE TABLE ACCOUNT
(
    ID       INT                 NOT NULL AUTO_INCREMENT PRIMARY KEY,
    USERNAME VARCHAR(20) UNIQUE  NOT NULL,
    EMAIL    VARCHAR(320) UNIQUE NOT NULL,
    PASSWORD VARCHAR(60)         NOT NULL
);

CREATE TABLE LEARNING_SET
(
    ID                   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TITLE                VARCHAR(200) NOT NULL,
    PUBLICLY_VISIBLE     BOOLEAN      NOT NULL,
    CREATION_TIME        TIMESTAMP    NOT NULL,
    TERM_LANGUAGE        VARCHAR(2)   NOT NULL,
    TRANSLATION_LANGUAGE VARCHAR(2)   NOT NULL
);

CREATE TABLE ACCESS_ROLE
(
    ID      INT     NOT NULL PRIMARY KEY AUTO_INCREMENT,
    ROLE    TINYINT NOT NULL,
    SET_ID  INT     NOT NULL,
    USER_ID INT     NOT NULL,

    FOREIGN KEY (SET_ID) REFERENCES LEARNING_SET (ID),
    FOREIGN KEY (USER_ID) REFERENCES ACCOUNT (ID)
);

CREATE TABLE LEARNING_SET_ITEM
(
    SET_ID      INT          NOT NULL,
    ITEM_ID     INT          NOT NULL,
    TERM        VARCHAR(255) NOT NULL,
    TRANSLATION VARCHAR(255) NOT NULL,

    PRIMARY KEY (SET_ID, ITEM_ID),
    FOREIGN KEY (SET_ID) REFERENCES LEARNING_SET (ID)
);
