-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 06, 2021 at 08:36 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lab2`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int(11) NOT NULL,
  `login` varchar(200) CHARACTER SET armscii8 NOT NULL,
  `password` varchar(200) CHARACTER SET armscii8 NOT NULL,
  `email` varchar(200) CHARACTER SET armscii8 NOT NULL,
  `phone_number` int(10) NOT NULL,
  `course_is` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `login`, `password`, `email`, `phone_number`, `course_is`) VALUES
(1, 'admin', 'admin', 'admin', 45, 1),
(2, 'admin2', 'admin2', 'admin2@admin2.com', 86457654, 2);

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `id` int(11) NOT NULL,
  `course_name` varchar(200) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `admin_id` int(11) NOT NULL,
  `course_price` double NOT NULL,
  `course_is` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`id`, `course_name`, `start_date`, `end_date`, `admin_id`, `course_price`, `course_is`) VALUES
(9, 'fizika', '2021-04-01', '2021-05-08', 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `course_is`
--

CREATE TABLE `course_is` (
  `id` int(11) NOT NULL,
  `name` varchar(200) CHARACTER SET armscii8 NOT NULL,
  `date_created` date NOT NULL DEFAULT current_timestamp(),
  `version` varchar(15) CHARACTER SET armscii8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `course_is`
--

INSERT INTO `course_is` (`id`, `name`, `date_created`, `version`) VALUES
(1, 'Java', '2021-03-01', '1.0');

-- --------------------------------------------------------

--
-- Table structure for table `folder`
--

CREATE TABLE `folder` (
  `id` int(11) NOT NULL,
  `folder_name` varchar(200) NOT NULL,
  `date_modified` date NOT NULL,
  `course_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Triggers `folder`
--
DELIMITER $$
CREATE TRIGGER `log_folder_delete` AFTER DELETE ON `folder` FOR EACH ROW DELETE FROM folder_files WHERE folder_files.folder_id = old.id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `folder_files`
--

CREATE TABLE `folder_files` (
  `id` int(11) NOT NULL,
  `file_name` varchar(200) NOT NULL,
  `date_added` date NOT NULL,
  `file_path` varchar(200) NOT NULL,
  `folder_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` int(11) NOT NULL,
  `login` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `student_name` varchar(200) NOT NULL,
  `surname` varchar(200) NOT NULL,
  `acc_number` int(10) NOT NULL,
  `course_is` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Triggers `students`
--
DELIMITER $$
CREATE TRIGGER `log_student_delete` AFTER DELETE ON `students` FOR EACH ROW DELETE FROM student_enroll_course
    WHERE student_enroll_course.student_id = old.id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `student_enroll_course`
--

CREATE TABLE `student_enroll_course` (
  `id` int(10) NOT NULL,
  `student_id` int(10) NOT NULL,
  `course_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `course_is`
--
ALTER TABLE `course_is`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `folder`
--
ALTER TABLE `folder`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `folder_files`
--
ALTER TABLE `folder_files`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `student_enroll_course`
--
ALTER TABLE `student_enroll_course`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `course`
--
ALTER TABLE `course`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `course_is`
--
ALTER TABLE `course_is`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `folder`
--
ALTER TABLE `folder`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `folder_files`
--
ALTER TABLE `folder_files`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `student_enroll_course`
--
ALTER TABLE `student_enroll_course`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
