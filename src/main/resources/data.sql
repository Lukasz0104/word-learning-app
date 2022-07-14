INSERT INTO ACCOUNT (USERNAME, EMAIL, PASSWORD)
VALUES ('user1', 'user1@a.com',
        '$2a$10$mZPg2gNMXJgXpHQ2OPVD4OZAKDYORtApDx3TejDV.P83mskFPt4Lq'), /* password = abc */
       ('user2', 'user2@b.com',
        '$2a$10$uWpsBflFJw1yuRa2BHLjNOSrn.gki6aZ5ygqyCPngpbPTVZVFB9/S'), /* password = cba */
       ('user3', 'user3@c.com',
        '$2a$10$AqWV7uP25ifbQ6sS7134JeioRKWlxhPu3NDvdyLLY3Qot0G.GuW2u'), /* password = password */
       ('user4', 'user4@d.com',
        '$2a$10$TNqra3eqJTd1229a2TVSR.CWK43BkufHLHZfwqb/3HyYdZL5y.Xz6'); /* password = p4$$w0rd */

INSERT INTO LEARNING_SET
(TITLE, CREATION_TIME, PUBLICLY_VISIBLE, TERM_LANGUAGE, TRANSLATION_LANGUAGE)
VALUES ('niemieckie słówka', NOW(), TRUE, 'de', 'pl'),
       ('animals in german', NOW(), FALSE, 'de', 'en'),
       ('polish words', NOW(), FALSE, 'pl', 'en');

INSERT INTO LEARNING_SET_ITEM (SET_ID, ITEM_ID, TERM, TRANSLATION)
VALUES (1, 1, 'hallo', 'cześć'),
       (2, 1, 'e Katze', 'cat'),
       (1, 2, 'r Hund', 'dog'),
       (1, 3, 'ja', 'tak'),
       (1, 4, 'nein', 'nie'),
       (2, 2, 'r Vogel', 'bird'),
       (2, 3, 'e Kuhe', 'cow'),
       (3, 1, 'tak', 'yes'),
       (3, 2, 'nie', 'no');

INSERT INTO ACCESS_ROLE (ROLE, SET_ID, USER_ID)
VALUES (20, 1, 1), -- user1 is the owner of set 1
       (15, 1, 2), -- user2 can edit set 1
       (10, 1, 3), -- user3 can propose changes to set 1
       (20, 2, 2), -- user2 is the owner of set 2
       (5, 2, 1),  -- user1 can read items in set 2
       (15, 2, 4), -- user4 can edit set 2
       (20, 3, 3), -- user3 is the owner of set 3
       (5, 3, 1),  -- user1 can view items in set 3
       (10, 3, 4); -- user4 can propose changes to set 3
