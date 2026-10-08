CREATE TABLE `groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '""',
  `owner` int(11) NOT NULL,
  `creator` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `modified_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_groups_name` (`name`),
  KEY `fk_groups_owner` (`owner`),
  KEY `fk_groups_creator` (`creator`),
  CONSTRAINT `fk_groups_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_groups_owner` FOREIGN KEY (`owner`) REFERENCES `users` (`id`) ON DELETE CASCADE
)