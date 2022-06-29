INSERT INTO ACCOUNT (USERNAME, EMAIL, PASSWORD)
VALUES ('user1', 'user1@a.com', 'abc'),
       ('user2', 'user2@b.com', 'cba');

INSERT INTO LEARNING_SET
(TITLE, CREATION_TIME, PUBLICLY_VISIBLE, TERM_LANGUAGE, TRANSLATION_LANGUGE)
VALUES ('niemieckie słówka', NOW(), TRUE, 'DE', 'PL'),
       ('animals in german', NOW(), FALSE, 'DE', 'EN');

INSERT INTO LEARNING_SET_ITEM (SET_ID, TERM, TRANSLATION)
VALUES (1, 'hallo', 'cześć'),
       (2, 'e Katze', 'cat'),
       (1, 'r Hund', 'dog'),
       (1, 'ja', 'tak'),
       (1, 'nein', 'nie'),
       (2, 'r Vogel', 'bird'),
       (2, 'e Kuhe', 'cow');