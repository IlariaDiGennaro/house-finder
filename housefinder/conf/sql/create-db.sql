CREATE DATABASE IF NOT EXISTS `housefinder`;

USE housefinder;

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
	`piano` VARCHAR(10) NULL DEFAULT NULL,
	`garantito` TINYINT NOT NULL DEFAULT 0,
	`agenzia` VARCHAR(255) NULL DEFAULT NULL,
	`last_analyze` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_unicode_ci'
;

CREATE TABLE `z_casa` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`cancellato` TINYINT(4) NOT NULL DEFAULT '0',
	`scartato` TINYINT(4) NOT NULL DEFAULT '0',
	`motivazione` VARCHAR(255) NULL DEFAULT NULL,
	`id_annuncio` VARCHAR(50) NOT NULL,
	`titolo` VARCHAR(255) NOT NULL,
	`link` VARCHAR(255) NOT NULL,
	`descrizione` VARCHAR(1024) NOT NULL,
	`prezzo` VARCHAR(255) NOT NULL,
	`num_locali` VARCHAR(10) NOT NULL,
	`metri_quadri` VARCHAR(10) NOT NULL,
	`num_bagni` VARCHAR(10) NULL DEFAULT NULL,
	`piano` VARCHAR(10) NULL DEFAULT NULL,
	`garantito` TINYINT(4) NOT NULL DEFAULT '0',
	`agenzia` VARCHAR(255) NULL DEFAULT NULL,
	`last_analyze` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_unicode_ci'
;

ALTER TABLE `casa`
	ADD COLUMN `new_annuncio` TINYINT(4) NOT NULL DEFAULT '0' AFTER `last_analyze`,
	ADD COLUMN `new_datetime` DATETIME NULL DEFAULT NULL AFTER `new_annuncio`;

	
CREATE TABLE IF NOT EXISTS `selected_house` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`id_annuncio` VARCHAR(50) NOT NULL,
	`titolo` VARCHAR(255) NOT NULL,
	`link` VARCHAR(255) NOT NULL,
	`descrizione` VARCHAR(1024) NOT NULL,
	`prezzo` VARCHAR(255) NOT NULL,
	`num_locali` VARCHAR(10) NOT NULL,
	`metri_quadri` VARCHAR(10) NOT NULL,
	`num_bagni` VARCHAR(10) NULL,
	`piano` VARCHAR(10) NULL DEFAULT NULL,
	`garantito` TINYINT NOT NULL DEFAULT 0,
	`agenzia` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_unicode_ci'
;

CREATE TABLE `z_selected_house` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`id_annuncio` VARCHAR(50) NOT NULL,
	`titolo` VARCHAR(255) NOT NULL,
	`link` VARCHAR(255) NOT NULL,
	`descrizione` VARCHAR(1024) NOT NULL,
	`prezzo` VARCHAR(255) NOT NULL,
	`num_locali` VARCHAR(10) NOT NULL,
	`metri_quadri` VARCHAR(10) NOT NULL,
	`num_bagni` VARCHAR(10) NULL DEFAULT NULL,
	`piano` VARCHAR(10) NULL DEFAULT NULL,
	`garantito` TINYINT NOT NULL DEFAULT 0,
	`agenzia` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_unicode_ci'
;