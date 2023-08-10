drop database if exists get_together;

create
database get_together
character set utf8mb4
collate utf8mb4_0900_ai_ci;
use get_together;

drop table if exists user;
drop table if exists event;

CREATE TABLE user (
                      id              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      user_name       VARCHAR(50)  NOT NULL,
                      password        VARCHAR(255) NOT NULL,
                      first_name      VARCHAR(50)  NOT NULL,
                      last_name       VARCHAR(50)  NOT NULL,
                      phone_number    VARCHAR(20)  NULL,
                      biography       VARCHAR(255) NULL,
                      mail            VARCHAR(50)  NOT NULL,
                      created         DATETIME     NOT NULL
);


CREATE TABLE event (
                       id              INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       header          VARCHAR(255)    NOT NULL,
                       user_id         INT             NOT NULL,
                       description     VARCHAR(255)    NOT NULL,
                       capacity        INT             NOT NULL,
                       attending       INT             NOT NULL,
                       created         DATETIME        NOT NULL,
                       is_active       BOOLEAN         NOT NULL DEFAULT TRUE,
                       FOREIGN KEY (user_id) REFERENCES user (id)
                           ON DELETE CASCADE ON UPDATE CASCADE
);