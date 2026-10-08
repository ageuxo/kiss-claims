CREATE TABLE `users` (
  `id`   int(11) NOT NULL AUTO_INCREMENT,
  `uuid` uuid         NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_users_uuid` (`uuid`),
  UNIQUE KEY `uq_users_name` (`name`)
)