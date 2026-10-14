CREATE TABLE `levels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '""',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_levels_name` (`name`)
)