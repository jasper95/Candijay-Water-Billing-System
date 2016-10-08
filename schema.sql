-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 08, 2016 at 02:25 AM
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
  `purok` int(1) NOT NULL,
  `status` varchar(45) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `account_type` varchar(45) DEFAULT NULL,
  `account_standing_balance` decimal(9,2) NOT NULL DEFAULT '0.00',
  `penalty` decimal(9,2) NOT NULL,
  `status_updated` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number UNIQUE` (`number`),
  KEY `customer_idx` (`customer_id`),
  KEY `accountAddress_idx` (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE IF NOT EXISTS `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brgy` varchar(45) NOT NULL,
  `location_code` int(11) NOT NULL,
  `account_prefix` varchar(2) NOT NULL,
  `accounts_count` int(5) NOT NULL,
  `due_day` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`id`, `brgy`, `location_code`, `account_prefix`, `accounts_count`, `due_day`) VALUES
(1, 'Cogtong', 1, '01', 572, 20),
(2, 'Tawid', 1, '02', 199, 20),
(3, 'Can-olin', 1, '03', 428, 19),
(4, 'Cadapdapan', 2, '04', 117, 16),
(5, 'Tambongan', 2, '05', 327, 16),
(6, 'Abihilan', 2, '06', 119, 17),
(7, 'La Union', 3, '07', 257, 15),
(8, 'Panadtaran', 3, '08', 172, 15),
(9, 'Poblacion', 4, '09', 897, 10),
(10, 'Boyoan', 4, '10', 42, 10),
(11, 'Pagahat', 5, '11', 111, 15);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `middle_name` varchar(45) NOT NULL,
  `gender` char(1) NOT NULL,
  `contact_number` varchar(50) DEFAULT NULL,
  `family_members_count` int(11) NOT NULL,
  `occupation` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

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
  KEY `owner_idx` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `expense`
--

CREATE TABLE IF NOT EXISTS `expense` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(3) NOT NULL,
  `schedule_id` bigint(20) NOT NULL,
  `amount` decimal(9,2) NOT NULL,
  `creation_time` datetime NOT NULL,
  `created_by_user` varchar(45) NOT NULL,
  `modified_by_user` varchar(45) NOT NULL,
  `modification_time` datetime NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `schedule_id` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

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
  `discount` decimal(9,2) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

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
  `creation_time` datetime NOT NULL,
  `created_by_user` varchar(45) NOT NULL,
  `modification_time` datetime NOT NULL,
  `modified_by_user` varchar(45) NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_idx` (`account_id`),
  KEY `reading_schedule_idx` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `modified_expense`
--

CREATE TABLE IF NOT EXISTS `modified_expense` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `expense_id` bigint(20) NOT NULL,
  `schedule_id` bigint(20) NOT NULL,
  `type` int(3) NOT NULL,
  `amount` decimal(9,2) NOT NULL,
  `creation_time` datetime NOT NULL,
  `created_by_user` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `expense_id` (`expense_id`),
  KEY `schedule_id` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `modified_payment`
--

CREATE TABLE IF NOT EXISTS `modified_payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `payment_id` bigint(20) NOT NULL,
  `or_number` varchar(20) NOT NULL,
  `amount_paid` decimal(9,2) NOT NULL,
  `date` date NOT NULL,
  `creation_time` datetime NOT NULL,
  `created_by_user` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `payment_id` (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `modified_reading`
--

CREATE TABLE IF NOT EXISTS `modified_reading` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reading_id` bigint(20) NOT NULL,
  `schedule_id` bigint(20) NOT NULL,
  `consumption` int(11) NOT NULL,
  `reading_value` int(11) NOT NULL,
  `creation_time` datetime NOT NULL,
  `created_by_user` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `reading_orig_idx` (`reading_id`),
  KEY `reading_orig_schedule_idx` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE IF NOT EXISTS `payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `invoice_id` bigint(20) NOT NULL,
  `schedule_id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `amount_paid` decimal(9,2) NOT NULL,
  `or_number` varchar(20) NOT NULL,
  `creation_time` datetime NOT NULL,
  `created_by_user` varchar(45) NOT NULL,
  `modified_by_user` varchar(45) NOT NULL,
  `version` bigint(20) NOT NULL,
  `modification_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_payment` (`account_id`),
  KEY `invoice_payment` (`invoice_id`),
  KEY `payment_schedule` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

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
(1, 'ACCOUNTS', 'Customers and Accounts'),
(2, 'READINGS', 'Meter Readings'),
(3, 'REPORTS', 'Bills and Reports'),
(4, 'TRANSACTIONS', 'Payments'),
(5, 'SYSTEM', 'System Users');

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE IF NOT EXISTS `schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `month` int(2) NOT NULL,
  `year` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE IF NOT EXISTS `settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `system_loss_rate` double(5,2) NOT NULL,
  `depreciation_fund_rate` double(5,2) NOT NULL,
  `pes` double(5,2) NOT NULL,
  `basic_rate` double(5,2) NOT NULL,
  `min_system_loss` double(5,2) NOT NULL,
  `min_depreciation_fund` double(5,2) NOT NULL,
  `min_basic` double(5,2) NOT NULL,
  `penalty` double(5,2) NOT NULL,
  `debts_allowed` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`id`, `system_loss_rate`, `depreciation_fund_rate`, `pes`, `basic_rate`, `min_system_loss`, `min_depreciation_fund`, `min_basic`, `penalty`, `debts_allowed`) VALUES
(1, 0.50, 0.50, 5.00, 10.00, 2.50, 2.50, 50.00, 0.10, 3);

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
  `creation_time` datetime NOT NULL,
  `modification_time` datetime NOT NULL,
  `created_by_user` varchar(45) NOT NULL,
  `modified_by_user` varchar(45) NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `full_name`, `username`, `password`, `type`, `status`, `creation_time`, `modification_time`, `created_by_user`, `modified_by_user`, `version`) VALUES
(1, 'Jasper Bernales', 'developer', '$2a$10$LvDVKusRSmMloHpVEC76yezPitaVReTXBRexN8KJvNUVwurvLhnVy', 'SUPERUSER', 'ACTIVE', '2016-08-01 00:00:00', '2016-10-02 13:27:30', 'developer', 'developer', 5);

-- --------------------------------------------------------

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
(1, 2),
(1, 5),
(1, 1),
(1, 3),
(1, 4);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `accountAddress` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `device`
--
ALTER TABLE `device`
  ADD CONSTRAINT `owner` FOREIGN KEY (`owner_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `expense`
--
ALTER TABLE `expense`
  ADD CONSTRAINT `expense_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `account_invoice` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `invoice_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `reading_invoice` FOREIGN KEY (`reading_id`) REFERENCES `meter_reading` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `meter_reading`
--
ALTER TABLE `meter_reading`
  ADD CONSTRAINT `account_meter_reading` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `reading_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `modified_expense`
--
ALTER TABLE `modified_expense`
  ADD CONSTRAINT `modifiedExpenseSched` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `originalExpense` FOREIGN KEY (`expense_id`) REFERENCES `expense` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `modified_payment`
--
ALTER TABLE `modified_payment`
  ADD CONSTRAINT `modified_payment_copy` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `modified_reading`
--
ALTER TABLE `modified_reading`
  ADD CONSTRAINT `reading_original` FOREIGN KEY (`reading_id`) REFERENCES `meter_reading` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `reading_original_shcedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `account_payment` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `invoice_payment` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `payment_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `usersandroles`
--
ALTER TABLE `usersandroles`
  ADD CONSTRAINT `role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
