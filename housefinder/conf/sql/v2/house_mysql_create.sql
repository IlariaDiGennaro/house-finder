CREATE TABLE `house_tmp` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`ad_id` varchar(10) NOT NULL,
	`title` varchar(120) NOT NULL,
	`link` varchar(255) NOT NULL,
	`description` varchar(5000) NOT NULL,
	`price` varchar(20) NOT NULL,
	`rooms` varchar(20) NOT NULL,
	`mq` varchar(20) NOT NULL,
	`wcs` varchar(20),
	`floor` varchar(20),
	`agency` varchar(120),
	`phone_numbers_agency` varchar(255) NOT NULL,
	`ad_rif` varchar(100) NOT NULL,
	`ad_date` varchar(20) NOT NULL,
	primary key (id)
);

CREATE TABLE `house` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`ad_id` varchar(10) NOT NULL,
	`title` varchar(120) NOT NULL,
	`link` varchar(255) NOT NULL,
	`description` varchar(5000) NOT NULL,
	`price` varchar(20) NOT NULL,
	`rooms` varchar(20) NOT NULL,
	`mq` varchar(20) NOT NULL,
	`wcs` varchar(20),
	`floor` varchar(20),
	`agency` varchar(120),
	`phone_numbers_agency` varchar(255) NOT NULL,
	`ad_rif` varchar(100) NOT NULL,
	`ad_date` varchar(20) NOT NULL,
	`note` varchar(5000),
	`resub_counter` INT NOT NULL DEFAULT '0',
	`house_status` enum('SELECTED','REJECTED','DELETED') NOT NULL,
	primary key (id)
);

CREATE TABLE `house_image` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`url` varchar(255) NOT NULL,
	`house_image_type` enum('PLANIMETRY','HOUSE') NOT NULL,
	`house_tmp_id` INT,
	`house_id` INT,
	primary key (id)
);

CREATE TABLE `house_history` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`house_id` INT NOT NULL,
	`house_status_old` enum('NEW','SELECTED','REJECTED','DELETED') NOT NULL,
	`house_status_new` enum('SELECTED','REJECTED','DELETED') NOT NULL,
	`change_datetime` DATETIME NOT NULL,
	primary key (id)
);

ALTER TABLE `house_image` ADD CONSTRAINT `house_image_fk0` FOREIGN KEY (`house_tmp_id`) REFERENCES `house_tmp`(`id`);

ALTER TABLE `house_image` ADD CONSTRAINT `house_image_fk1` FOREIGN KEY (`house_id`) REFERENCES `house`(`id`);

ALTER TABLE `house_history` ADD CONSTRAINT `house_history_fk0` FOREIGN KEY (`house_id`) REFERENCES `house`(`id`);

