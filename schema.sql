-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jul 20, 2016 at 04:03 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `revised_cws_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` varchar(8) NOT NULL,
  `address_id` int(11) NOT NULL,
  `status` varchar(45) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `account_type` varchar(45) DEFAULT NULL,
  `account_standing_balance` decimal(9,2) NOT NULL DEFAULT '0.00',
  `penalty` decimal(9,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number UNIQUE` (`number`),
  KEY `customer_idx` (`customer_id`),
  KEY `accountAddress_idx` (`address_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=32 ;

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE IF NOT EXISTS `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address_group` int(2) NOT NULL,
  `street` varchar(45) NOT NULL,
  `brgy` varchar(45) NOT NULL,
  `location_code` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `adress_group` (`address_group`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`id`, `address_group`, `street`, `brgy`, `location_code`) VALUES
(3, 1, 'Purok 1', 'Tugas', 1),
(4, 2, 'Purok 1', 'Poblacion', 1);

-- --------------------------------------------------------

--
-- Table structure for table `address_group`
--

CREATE TABLE IF NOT EXISTS `address_group` (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `account_prefix` varchar(2) CHARACTER SET utf8 NOT NULL,
  `accounts_count` int(5) NOT NULL,
  `due_day` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `address_group`
--

INSERT INTO `address_group` (`id`, `account_prefix`, `accounts_count`, `due_day`) VALUES
(1, '01', 8, 10),
(2, '02', 14, 15);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `birth_date` date NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `middle_name` varchar(45) NOT NULL,
  `gender` char(1) NOT NULL,
  `contact_number` varchar(50) DEFAULT NULL,
  `family_members_count` int(11) NOT NULL,
  `occupation` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=15 ;

-- --------------------------------------------------------

--
-- Table structure for table `device`
--

CREATE TABLE IF NOT EXISTS `device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(20) NOT NULL,
  `meter_code` varchar(45) NOT NULL,
  `brand` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  `last_reading` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `meter_code_UNIQUE` (`meter_code`),
  KEY `owner_idx` (`owner_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=28 ;


-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE IF NOT EXISTS `invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `reading_id` bigint(20) NOT NULL,
  `gross_charge` decimal(9,2) DEFAULT NULL,
  `net_charge` decimal(9,2) NOT NULL,
  `status` varchar(16) NOT NULL,
  `schedule_id` bigint(20) NOT NULL,
  `basic` decimal(9,2) NOT NULL,
  `dep_fund` decimal(9,2) NOT NULL,
  `sys_loss` decimal(9,2) NOT NULL,
  `arrears` decimal(9,2) NOT NULL,
  `penalty` decimal(9,2) NOT NULL,
  `others` decimal(9,2) NOT NULL,
  `due_date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_idx` (`account_id`),
  KEY `invoice_schedule_idx` (`schedule_id`),
  KEY `reading_idx` (`reading_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=59 ;

-- --------------------------------------------------------

--
-- Table structure for table `meter_reading`
--

CREATE TABLE IF NOT EXISTS `meter_reading` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `reading_value` int(11) NOT NULL,
  `consumption` int(11) NOT NULL,
  `schedule_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_idx` (`account_id`),
  KEY `reading_schedule_idx` (`schedule_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=55 ;


-- --------------------------------------------------------

--
-- Table structure for table `occupation`
--

CREATE TABLE IF NOT EXISTS `occupation` (
  `customer_id` bigint(20) NOT NULL,
  `position` varchar(16) NOT NULL,
  `industry` varchar(255) NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE IF NOT EXISTS `payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `invoice_id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `amount_paid` decimal(9,2) NOT NULL,
  `discount` decimal(9,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_payment` (`account_id`),
  KEY `invoice_payment` (`invoice_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=38 ;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `role_name`, `description`) VALUES
(1, 'CUSTOMER_ACCOUNTS', 'Customers and Accounts'),
(2, 'METER_READING', 'Meter Readings'),
(3, 'BILLS_REPORTS', 'Bills and Reports'),
(4, 'PAYMENTS', 'Payments'),
(5, 'SYSTEM_USERS', 'System Users');

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE IF NOT EXISTS `schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `month` int(2) NOT NULL,
  `year` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;



-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE IF NOT EXISTS `settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `system_loss` double(5,2) NOT NULL,
  `depreciation_fund` double(5,2) NOT NULL,
  `pes` double(5,2) NOT NULL,
  `basic` double(5,2) NOT NULL,
  `penalty` double(5,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`id`, `system_loss`, `depreciation_fund`, `pes`, `basic`, `penalty`) VALUES
(1, 0.50, 0.50, 5.00, 5.00, 0.10);

-- --------------------------------------------------------

--
-- Table structure for table `tax`
--

CREATE TABLE IF NOT EXISTS `tax` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `description` varchar(255) NOT NULL,
  `value` decimal(9,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `tax`
--

INSERT INTO `tax` (`id`, `code`, `description`, `value`) VALUES
(1, 'MRT', 'Meter Reading Tax', '0.00');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(45) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` varchar(100) NOT NULL,
  `type` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `full_name`, `username`, `password`, `type`, `status`) VALUES
(1, 'Jasper Bernales', 'jasper', '$2a$10$e2/kWwM1esM39yI1koc77Om6eJUrySd83r3aE0/UWmIBDN7coOkGO', 'SUPERUSER', 'ACTIVE'),--------------------------------------------

--
-- Table structure for table `usersandroles`
--

CREATE TABLE IF NOT EXISTS `usersandroles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `user_idx` (`user_id`),
  KEY `role_idx` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `usersandroles`
--

INSERT INTO `usersandroles` (`user_id`, `role_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `accountAddress` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `address`
--
ALTER TABLE `address`
  ADD CONSTRAINT `address_ibfk_1` FOREIGN KEY (`address_group`) REFERENCES `address_group` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `device`
--
ALTER TABLE `device`
  ADD CONSTRAINT `owner` FOREIGN KEY (`owner_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `account_invoice` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `invoice_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `reading_invoice` FOREIGN KEY (`reading_id`) REFERENCES `meter_reading` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `meter_reading`
--
ALTER TABLE `meter_reading`
  ADD CONSTRAINT `account_meter_reading` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `reading_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `occupation`
--
ALTER TABLE `occupation`
  ADD CONSTRAINT `customer_occupation` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `account_payment` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `invoice_payment` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `usersandroles`
--
ALTER TABLE `usersandroles`
  ADD CONSTRAINT `role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
