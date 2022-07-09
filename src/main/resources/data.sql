INSERT INTO ACCOUNT (USERNAME, EMAIL, PASSWORD)
VALUES ('user1', 'user1@a.com',
        '$2a$10$mZPg2gNMXJgXpHQ2OPVD4OZAKDYORtApDx3TejDV.P83mskFPt4Lq'), /* password = abc */
       ('user2', 'user2@b.com',
        '$2a$10$uWpsBflFJw1yuRa2BHLjNOSrn.gki6aZ5ygqyCPngpbPTVZVFB9/S'); /* password = cba */

INSERT INTO LEARNING_SET
(TITLE, CREATION_TIME, PUBLICLY_VISIBLE, TERM_LANGUAGE, TRANSLATION_LANGUAGE)
VALUES ('niemieckie słówka', NOW(), TRUE, 'DE', 'PL'),
       ('animals in german', NOW(), FALSE, 'DE', 'EN');

INSERT INTO LEARNING_SET_ITEM (SET_ID, ITEM_ID, TERM, TRANSLATION)
VALUES (1, 1, 'hallo', 'cześć'),
       (2, 1, 'e Katze', 'cat'),
       (1, 2, 'r Hund', 'dog'),
       (1, 3, 'ja', 'tak'),
       (1, 4, 'nein', 'nie'),
       (2, 2, 'r Vogel', 'bird'),
       (2, 3, 'e Kuhe', 'cow');
