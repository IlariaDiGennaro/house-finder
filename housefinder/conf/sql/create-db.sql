CREATE DATABASE IF NOT EXISTS `housefinder`;

USE tutorialDb;

CREATE TABLE IF NOT EXISTS `casa` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`id_annuncio` VARCHAR(50) NOT NULL,
	`titolo` VARCHAR(255) NOT NULL,
	`link` VARCHAR(255) NOT NULL,
	`descrizione` VARCHAR(1024) NOT NULL,
	`prezzo` VARCHAR(255) NOT NULL,
	`num_locali` VARCHAR(10) NOT NULL,
	`metri_quadri` VARCHAR(10) NOT NULL,
	`num_bagni` VARCHAR(10) NULL,
	`piano` VARCHAR(10) NULL,
	`garantito` TINYINT NOT NULL DEFAULT 0,
	`agenzia` VARCHAR(255) NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_unicode_ci'
;
