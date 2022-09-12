USE MODEL;
GO

IF NOT EXISTS(SELECT *
              FROM SYS.DATABASES
              WHERE NAME = 'WORDAPP')
        BEGIN
            CREATE DATABASE WORDAPP;
        END;
GO

USE WORDAPP;
GO

IF OBJECT_ID('ACCOUNT', N'U') IS NULL
        BEGIN
            CREATE TABLE [ACCOUNT]
            (
                [ID]       INT                 NOT NULL IDENTITY (1, 1),
                [USERNAME] VARCHAR(25) UNIQUE  NOT NULL,
                [EMAIL]    VARCHAR(320) UNIQUE NOT NULL,
                [PASSWORD] VARCHAR(60)         NOT NULL,

                PRIMARY KEY (ID)
            );
        END
GO

IF OBJECT_ID('LEARNING_SET', N'U') IS NULL
        BEGIN
            CREATE TABLE [LEARNING_SET]
            (
                [ID]                   INT          NOT NULL IDENTITY (1, 1),
                [TITLE]                VARCHAR(200) NOT NULL,
                [PUBLICLY_VISIBLE]     BIT          NOT NULL,
                [CREATION_TIME]        DATETIME2    NOT NULL,
                [TERM_LANGUAGE]        VARCHAR(2)   NOT NULL,
                [TRANSLATION_LANGUAGE] VARCHAR(2)   NOT NULL,

                PRIMARY KEY (ID)
            );
        END
GO

IF OBJECT_ID('ACCESS_ROLE', N'U') IS NULL
        BEGIN
            CREATE TABLE ACCESS_ROLE
            (
                [ID]      INT     NOT NULL IDENTITY (1, 1),
                [ROLE]    TINYINT NOT NULL,
                [SET_ID]  INT     NOT NULL,
                [USER_ID] INT     NOT NULL,

                PRIMARY KEY (ID),
                FOREIGN KEY (SET_ID) REFERENCES [LEARNING_SET] (ID),
                FOREIGN KEY (USER_ID) REFERENCES [ACCOUNT] (ID)
            );
        END
GO

IF OBJECT_ID('LEARNING_SET_ITEM', N'U') IS NULL
        BEGIN
            CREATE TABLE LEARNING_SET_ITEM
            (
                [SET_ID]      INT          NOT NULL,
                [ITEM_ID]     INT          NOT NULL,
                [TERM]        VARCHAR(255) NOT NULL,
                [TRANSLATION] VARCHAR(255) NOT NULL,

                PRIMARY KEY (SET_ID, ITEM_ID),
                FOREIGN KEY (SET_ID) REFERENCES [LEARNING_SET] (ID)
            );
        END
GO

IF NOT EXISTS(SELECT NAME
              FROM SYSINDEXES
              WHERE NAME = 'IX_ACCOUNT_ID')
    CREATE INDEX IX_ACCOUNT_ID ON [ACCOUNT] (ID);
GO

IF NOT EXISTS(SELECT NAME
              FROM SYSINDEXES
              WHERE NAME = 'IX_ACCOUNT_USERNAME')
    CREATE INDEX IX_ACCOUNT_USERNAME ON [ACCOUNT] (USERNAME);
GO

IF NOT EXISTS(SELECT NAME
              FROM SYSINDEXES
              WHERE NAME = 'IX_LEARNING_SET_ID')
    CREATE INDEX IX_LEARNING_SET_ID ON [LEARNING_SET] (ID);
GO

IF NOT EXISTS(SELECT NAME
              FROM SYSINDEXES
              WHERE NAME = 'IX_LEARNING_SET_ITEM_SET_ID')
    CREATE INDEX IX_LEARNING_SET_ITEM_SET_ID ON [LEARNING_SET_ITEM] (SET_ID);
GO
