INSERT INTO ACCOUNT (USERNAME, EMAIL, PASSWORD)
VALUES ('user1', 'user1@a.com', 'abc'),
       ('user2', 'user2@b.com', 'cba');

INSERT INTO LEARNING_SET (CREATION_TIME, PUBLICLY_VISIBLE, TERM_LANGUAGE, TRANSLATION_LANGUGE)
VALUES (NOW(), TRUE, 'DE', 'PL'),
       (NOW(), FALSE, 'DE', 'EN');

INSERT INTO LEARNING_SET_ITEM (SET_ID, TERM, TRANSLATION)
VALUES (1, 'hallo', 'cześć'),
       (2, 'hallo', 'hi'),
       (1, 'r Hund', 'pies'),
       (1, 'ja', 'tak'),
       (1, 'nein', 'nie'),
       (2, 'ja', 'yes'),
       (2, 'nein', 'no');