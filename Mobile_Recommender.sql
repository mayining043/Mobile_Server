-- MySQL Script generated by MySQL Workbench
-- 03/10/17 15:23:13
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema MobileRecommenderDB
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `MobileRecommenderDB` ;

-- -----------------------------------------------------
-- Schema MobileRecommenderDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `MobileRecommenderDB` ;
USE `MobileRecommenderDB` ;

-- -----------------------------------------------------
-- Table `MobileRecommenderDB`.`itemInfo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MobileRecommenderDB`.`itemInfo` ;

CREATE TABLE IF NOT EXISTS `MobileRecommenderDB`.`itemInfo` (
  `item_id` INT NOT NULL AUTO_INCREMENT,
  `item_name` VARCHAR(45) NOT NULL,
  `latitude` DOUBLE NULL,
  `longtitude` DOUBLE NULL,
  `item_address` VARCHAR(45) NULL,
  `item_type` VARCHAR(45) NULL,
  `avg_price` DOUBLE NULL,
  `avg_rating` DOUBLE NULL,
  PRIMARY KEY (`item_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MobileRecommenderDB`.`userinfo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MobileRecommenderDB`.`userinfo` ;

CREATE TABLE IF NOT EXISTS `MobileRecommenderDB`.`userinfo` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC),
  UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MobileRecommenderDB`.`ratings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MobileRecommenderDB`.`ratings` ;

CREATE TABLE IF NOT EXISTS `MobileRecommenderDB`.`ratings` (
  `user_id` INT NOT NULL,
  `item_id` INT NOT NULL,
  `rating` INT NULL,
  `latitude` DOUBLE NULL,
  `longtitude` DOUBLE NULL,
  `time` DATETIME NULL,
  `weather` VARCHAR(45) NULL,
  `season` VARCHAR(45) NULL,
  `daytype` VARCHAR(45) NULL,
  PRIMARY KEY (`user_id`, `item_id`),
  INDEX `fk_ratings_itemInfo1_idx` (`item_id` ASC),
  CONSTRAINT `fk_ratings_itemInfo1`
    FOREIGN KEY (`item_id`)
    REFERENCES `MobileRecommenderDB`.`itemInfo` (`item_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ratings_userinfo1`
    FOREIGN KEY (`user_id`)
    REFERENCES `MobileRecommenderDB`.`userinfo` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;