-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 13, 2024 at 04:54 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `animelibrary`
--
DROP DATABASE IF EXISTS `animelibrary`;
CREATE DATABASE IF NOT EXISTS `animelibrary` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `animelibrary`;

-- --------------------------------------------------------

--
-- Table structure for table `animemovies`
--

DROP TABLE IF EXISTS `animemovies`;
CREATE TABLE `animemovies` (
  `MovieID` int(11) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `ReleaseDate` date DEFAULT NULL,
  `Duration` int(11) DEFAULT NULL,
  `Genre` varchar(100) DEFAULT NULL,
  `Description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `animeseries`
--

DROP TABLE IF EXISTS `animeseries`;
CREATE TABLE `animeseries` (
  `SeriesID` int(11) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `ReleaseDate` date DEFAULT NULL,
  `TotalEpisodes` int(11) DEFAULT NULL,
  `Genre` varchar(100) DEFAULT NULL,
  `Description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `ReviewID` int(11) NOT NULL,
  `WatchListID` int(11) NOT NULL,
  `Review` text DEFAULT NULL,
  `Rating` decimal(3,1) NOT NULL,
  `ReviewDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `watchlist`
--

DROP TABLE IF EXISTS `watchlist`;
CREATE TABLE `watchlist` (
  `WatchListID` int(11) NOT NULL,
  `SeriesID` int(11) DEFAULT NULL,
  `MovieID` int(11) DEFAULT NULL,
  `CurrentEpisode` int(11) NOT NULL,
  `Complete` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `animemovies`
--
ALTER TABLE `animemovies`
  ADD PRIMARY KEY (`MovieID`),
  ADD UNIQUE KEY `animemovies_title_idx` (`Title`);

--
-- Indexes for table `animeseries`
--
ALTER TABLE `animeseries`
  ADD PRIMARY KEY (`SeriesID`),
  ADD UNIQUE KEY `animeseries_title_idx` (`Title`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`ReviewID`),
  ADD KEY `review_watchlistid_fk` (`WatchListID`);

--
-- Indexes for table `watchlist`
--
ALTER TABLE `watchlist`
  ADD PRIMARY KEY (`WatchListID`),
  ADD KEY `watchlist_seriesid_fk` (`SeriesID`),
  ADD KEY `watchlist_movieid_fk` (`MovieID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `animemovies`
--
ALTER TABLE `animemovies`
  MODIFY `MovieID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `animeseries`
--
ALTER TABLE `animeseries`
  MODIFY `SeriesID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT for table `review`
--
ALTER TABLE `review`
  MODIFY `ReviewID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `watchlist`
--
ALTER TABLE `watchlist`
  MODIFY `WatchListID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `review_ibfk_1` FOREIGN KEY (`WatchListID`) REFERENCES `watchlist` (`WatchListID`);

--
-- Constraints for table `watchlist`
--
ALTER TABLE `watchlist`
  ADD CONSTRAINT `watchlist_ibfk_1` FOREIGN KEY (`SeriesID`) REFERENCES `animeseries` (`SeriesID`),
  ADD CONSTRAINT `watchlist_ibfk_2` FOREIGN KEY (`MovieID`) REFERENCES `animemovies` (`MovieID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
