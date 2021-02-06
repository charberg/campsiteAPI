CREATE TABLE IF NOT EXISTS `CAMPSITES`(
    `id`          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(80) NOT NULL,
    `description`      VARCHAR(200)
);

INSERT INTO CAMPSITES (name, description) VALUES ('volcano island', 'dont worry about the volcano');

CREATE TABLE IF NOT EXISTS `RESERVATIONS`(
    `id`          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `campsiteId`       INTEGER NOT NULL,
    `name`      VARCHAR(40) NOT NULL,
    `email`      VARCHAR(40) NOT NULL,
    `startDate`		DATE NOT NULL,
    `endDate`		DATE NOT NULL,
    FOREIGN KEY(campsiteId) REFERENCES CAMPSITES(id) ON DELETE CASCADE
);