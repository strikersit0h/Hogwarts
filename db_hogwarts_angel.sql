-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 12, 2025 at 09:45 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_hogwarts_angel`
--

-- --------------------------------------------------------

--
-- Table structure for table `alumno_asignatura`
--

CREATE TABLE `alumno_asignatura` (
  `id_alumno` int(11) NOT NULL,
  `id_asignatura` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `alumno_hechizo`
--

CREATE TABLE `alumno_hechizo` (
  `id_alumno` int(11) NOT NULL,
  `id_hechizo` int(11) NOT NULL,
  `aprendido` tinyint(1) NOT NULL,
  `fecha_aprendizaje` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `asignaturas`
--

CREATE TABLE `asignaturas` (
  `id` int(11) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `asignaturas`
--

INSERT INTO `asignaturas` (`id`, `nombre`, `descripcion`) VALUES
(1, 'Prueba', 'Asignatura de prueba');

-- --------------------------------------------------------

--
-- Table structure for table `casas`
--

CREATE TABLE `casas` (
  `id` int(11) NOT NULL,
  `nombre` varchar(40) NOT NULL,
  `puntos` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `casas`
--

INSERT INTO `casas` (`id`, `nombre`, `puntos`) VALUES
(1, 'Gryffindor', 0),
(2, 'Ravenclaw', 0),
(3, 'Slytherin', 0),
(4, 'Hufflepuf', 0);

-- --------------------------------------------------------

--
-- Table structure for table `hechizos`
--

CREATE TABLE `hechizos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `puntos_experiencia` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hechizos`
--

INSERT INTO `hechizos` (`id`, `nombre`, `descripcion`, `puntos_experiencia`) VALUES
(2, 'Lux', 'Da luz', 5),
(3, 'Expelliarmus', 'Desarmar', 10),
(4, 'Expecto Patronum', 'Protege del desamparo', 20),
(5, 'Alohomora', 'Abrir cerraduras', 15),
(6, 'Accio', 'Convocar', 10),
(7, 'Wingardium Leviosa', 'Hacer levitar objetos', 15);

-- --------------------------------------------------------

--
-- Table structure for table `ingredientes`
--

CREATE TABLE `ingredientes` (
  `id` int(11) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `sanacion` int(11) NOT NULL,
  `analgesia` int(11) NOT NULL,
  `curativo` int(11) NOT NULL,
  `inflamatorio` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ingredientes`
--

INSERT INTO `ingredientes` (`id`, `nombre`, `sanacion`, `analgesia`, `curativo`, `inflamatorio`) VALUES
(1, 'Mandragora', 5, 10, 5, 2),
(2, 'Acónito', 2, 10, 0, 10);

-- --------------------------------------------------------

--
-- Table structure for table `pociones`
--

CREATE TABLE `pociones` (
  `id` int(11) NOT NULL,
  `nombre` int(11) NOT NULL,
  `fecha_creacion` date DEFAULT NULL,
  `fecha_modificacion` date DEFAULT NULL,
  `fecha_borrado` date DEFAULT NULL,
  `indicador_bien_mal` tinyint(1) NOT NULL,
  `id_creador` int(11) NOT NULL,
  `validada` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pocion_ingrediente`
--

CREATE TABLE `pocion_ingrediente` (
  `id_pocion` int(11) NOT NULL,
  `id_ingrediente` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `modo_preparacion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `preferencias_casa`
--

CREATE TABLE `preferencias_casa` (
  `id_usuario` int(11) NOT NULL,
  `id_casa` int(11) NOT NULL,
  `orden_preferencia` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `profesor_asignatura`
--

CREATE TABLE `profesor_asignatura` (
  `id_profesor` int(11) NOT NULL,
  `id_asignatura` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ranking_casas`
--

CREATE TABLE `ranking_casas` (
  `id_casa` int(11) NOT NULL,
  `puntos_totales` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `nombre`) VALUES
(1, 'Dumbledore'),
(2, 'Administrador'),
(3, 'Usuario'),
(4, 'Profesor');

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `contraseña` varchar(30) NOT NULL,
  `experiencia` int(100) NOT NULL,
  `nivel` int(10) DEFAULT NULL,
  `id_casa` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `email`, `contraseña`, `experiencia`, `nivel`, `id_casa`) VALUES
(1, 'Dumbledore', 'dumbledore@hogwarts.com', 'Dumbledore123', 400, 4, 1),
(4, 'prueba1', 'prueba1@hogwarts.com', 'Prueba123', 300, 2, 2),
(5, 'prueba2', 'prueba2@hogwarts.com', 'Prueba123', 0, 1, 3),
(6, 'prueba3', 'prueba3@hogwarts.com', 'Prueba123', 0, 1, 4),
(7, 'prueba4', 'prueba4@hogwarts.com', 'Prueba123', 0, 1, 4);

-- --------------------------------------------------------

--
-- Table structure for table `usuario_rol`
--

CREATE TABLE `usuario_rol` (
  `id_usuario` int(11) NOT NULL,
  `id_rol` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `usuario_rol`
--

INSERT INTO `usuario_rol` (`id_usuario`, `id_rol`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(4, 3),
(5, 3),
(6, 3),
(7, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alumno_asignatura`
--
ALTER TABLE `alumno_asignatura`
  ADD PRIMARY KEY (`id_alumno`,`id_asignatura`),
  ADD KEY `fk_asignatura_alumno` (`id_asignatura`);

--
-- Indexes for table `alumno_hechizo`
--
ALTER TABLE `alumno_hechizo`
  ADD PRIMARY KEY (`id_alumno`,`id_hechizo`),
  ADD KEY `fk_hechizo_alumno` (`id_hechizo`);

--
-- Indexes for table `asignaturas`
--
ALTER TABLE `asignaturas`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `casas`
--
ALTER TABLE `casas`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `hechizos`
--
ALTER TABLE `hechizos`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ingredientes`
--
ALTER TABLE `ingredientes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pociones`
--
ALTER TABLE `pociones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_pocion_usuario` (`id_creador`);

--
-- Indexes for table `pocion_ingrediente`
--
ALTER TABLE `pocion_ingrediente`
  ADD PRIMARY KEY (`id_pocion`,`id_ingrediente`),
  ADD KEY `fk_ingrediente_pocion` (`id_ingrediente`);

--
-- Indexes for table `preferencias_casa`
--
ALTER TABLE `preferencias_casa`
  ADD PRIMARY KEY (`id_usuario`,`id_casa`),
  ADD KEY `fk_casa` (`id_casa`);

--
-- Indexes for table `profesor_asignatura`
--
ALTER TABLE `profesor_asignatura`
  ADD PRIMARY KEY (`id_profesor`,`id_asignatura`),
  ADD KEY `fk_asignatura` (`id_asignatura`);

--
-- Indexes for table `ranking_casas`
--
ALTER TABLE `ranking_casas`
  ADD PRIMARY KEY (`id_casa`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_casa_usuarios` (`id_casa`);

--
-- Indexes for table `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD PRIMARY KEY (`id_usuario`,`id_rol`),
  ADD KEY `fk_rol_rol` (`id_rol`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `asignaturas`
--
ALTER TABLE `asignaturas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `casas`
--
ALTER TABLE `casas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `hechizos`
--
ALTER TABLE `hechizos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `ingredientes`
--
ALTER TABLE `ingredientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `pociones`
--
ALTER TABLE `pociones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `alumno_asignatura`
--
ALTER TABLE `alumno_asignatura`
  ADD CONSTRAINT `fk_alumno_asignatura` FOREIGN KEY (`id_alumno`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `fk_asignatura_alumno` FOREIGN KEY (`id_asignatura`) REFERENCES `asignaturas` (`id`);

--
-- Constraints for table `alumno_hechizo`
--
ALTER TABLE `alumno_hechizo`
  ADD CONSTRAINT `fk_alumno_hechizo` FOREIGN KEY (`id_alumno`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `fk_hechizo_alumno` FOREIGN KEY (`id_hechizo`) REFERENCES `hechizos` (`id`);

--
-- Constraints for table `pociones`
--
ALTER TABLE `pociones`
  ADD CONSTRAINT `fk_pocion_usuario` FOREIGN KEY (`id_creador`) REFERENCES `usuarios` (`id`);

--
-- Constraints for table `pocion_ingrediente`
--
ALTER TABLE `pocion_ingrediente`
  ADD CONSTRAINT `fk_ingrediente_pocion` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingredientes` (`id`),
  ADD CONSTRAINT `fk_pocion_ingrediente` FOREIGN KEY (`id_pocion`) REFERENCES `pociones` (`id`);

--
-- Constraints for table `preferencias_casa`
--
ALTER TABLE `preferencias_casa`
  ADD CONSTRAINT `fk_casa` FOREIGN KEY (`id_casa`) REFERENCES `casas` (`id`),
  ADD CONSTRAINT `fk_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`);

--
-- Constraints for table `profesor_asignatura`
--
ALTER TABLE `profesor_asignatura`
  ADD CONSTRAINT `fk_asignatura` FOREIGN KEY (`id_asignatura`) REFERENCES `asignaturas` (`id`),
  ADD CONSTRAINT `fk_profesor` FOREIGN KEY (`id_profesor`) REFERENCES `usuarios` (`id`);

--
-- Constraints for table `ranking_casas`
--
ALTER TABLE `ranking_casas`
  ADD CONSTRAINT `fk_casa_puntos` FOREIGN KEY (`id_casa`) REFERENCES `casas` (`id`);

--
-- Constraints for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `fk_casa_usuarios` FOREIGN KEY (`id_casa`) REFERENCES `casas` (`id`);

--
-- Constraints for table `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD CONSTRAINT `fk_rol_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `fk_rol_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
