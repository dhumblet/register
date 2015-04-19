-- phpMyAdmin SQL Dump
-- version 4.1.9
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Generation Time: Jan 11, 2015 at 08:11 PM
-- Server version: 5.5.34
-- PHP Version: 5.5.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: cash.register
--

-- --------------------------------------------------------

--
-- Table structure for table category
--

CREATE TABLE category (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  shortcut varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table merchant
--

CREATE TABLE merchant (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table product
--


CREATE TABLE product (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  category bigint(20) NOT NULL,
  name varchar(256) NOT NULL,
  price decimal(20,2) NOT NULL,
  serial varchar(256) DEFAULT NULL,
  merchantId bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table right
--

CREATE TABLE rights (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table role
--

CREATE TABLE role (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table role_right
--

CREATE TABLE role_rights (
  roleId bigint(20) NOT NULL,
  rightId bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table transaction
--

CREATE TABLE transaction (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  date datetime NOT NULL,
  price decimal(10,2) NOT NULL,
  received decimal(10,2) DEFAULT NULL,
  returned decimal(10,2) DEFAULT NULL,
  payment int(4) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id (id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table transaction_detail
--

CREATE TABLE transaction_detail (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  transaction bigint(20) NOT NULL,
  category bigint(20) NOT NULL,
  product bigint(20) DEFAULT NULL,
  amount int(20) NOT NULL DEFAULT '1',
  price decimal(10,2) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id (id),
  KEY transaction_foreign_key (transaction)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table user
--

CREATE TABLE user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  login varchar(100) NOT NULL,
  password varchar(100) DEFAULT NULL,
  merchantId bigint(20) NOT NULL,
  roleId bigint(20) DEFAULT NULL,
  locked BIT(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
