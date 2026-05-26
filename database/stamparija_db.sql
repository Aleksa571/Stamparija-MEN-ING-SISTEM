-- =====================================================================
--  Štamparija MEN-ING SISTEM DOO - SQL skripta za phpMyAdmin / MySQL
--  Završni projekat: Internet softverske arhitekture
--
--  Kako koristiti:
--    1. Otvorite phpMyAdmin
--    2. Kliknite na karticu "SQL"
--    3. Nalepite ceo sadržaj ove skripte
--    4. Kliknite "Go" / "Izvrši"
--
--  Skripta će:
--    - Kreirati bazu `stamparija_db` (ako ne postoji)
--    - Obrisati postojeće tabele (DROP IF EXISTS)
--    - Kreirati strukturu (9 tabela)
--    - Ubaciti početne podatke
-- =====================================================================

CREATE DATABASE IF NOT EXISTS `stamparija_db`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `stamparija_db`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `refresh_tokens`;
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `blog_posts`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `roles`;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================================
--  STRUKTURA TABELA
-- =====================================================================

-- ---------------------------------------------------------------------
--  Tabela: roles (ROLE_ADMIN, ROLE_USER)
-- ---------------------------------------------------------------------
CREATE TABLE `roles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_roles_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  Tabela: users (korisnički nalozi)
-- ---------------------------------------------------------------------
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `first_name` VARCHAR(50) DEFAULT NULL,
    `last_name` VARCHAR(50) DEFAULT NULL,
    `phone` VARCHAR(20) DEFAULT NULL,
    `address` VARCHAR(255) DEFAULT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `enabled` BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_users_username` (`username`),
    UNIQUE KEY `uk_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  Tabela: user_roles (ManyToMany veza User <-> Role)
-- ---------------------------------------------------------------------
CREATE TABLE `user_roles` (
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    KEY `fk_user_roles_role` (`role_id`),
    CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  Tabela: categories (kategorije proizvoda)
-- ---------------------------------------------------------------------
CREATE TABLE `categories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` VARCHAR(500) DEFAULT NULL,
    `image_url` VARCHAR(500) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_categories_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  Tabela: products (proizvodi)
-- ---------------------------------------------------------------------
CREATE TABLE `products` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(150) NOT NULL,
    `description` VARCHAR(2000) DEFAULT NULL,
    `price` DECIMAL(10,2) NOT NULL,
    `dimensions` VARCHAR(100) DEFAULT NULL,
    `image_url` VARCHAR(500) DEFAULT NULL,
    `stock` INT NOT NULL DEFAULT 0,
    `available` BIT(1) NOT NULL DEFAULT b'1',
    `created_at` DATETIME(6) NOT NULL,
    `category_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_products_category` (`category_id`),
    CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  Tabela: orders (porudžbine)
-- ---------------------------------------------------------------------
CREATE TABLE `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_date` DATETIME(6) NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'PRIMLJENA',
    `delivery_type` VARCHAR(30) NOT NULL,
    `delivery_address` VARCHAR(500) DEFAULT NULL,
    `contact_phone` VARCHAR(20) DEFAULT NULL,
    `note` VARCHAR(1000) DEFAULT NULL,
    `total_price` DECIMAL(10,2) NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_orders_user` (`user_id`),
    CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  Tabela: order_items (stavke porudžbine, ManyToMany Order <-> Product)
-- ---------------------------------------------------------------------
CREATE TABLE `order_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `quantity` INT NOT NULL,
    `unit_price` DECIMAL(10,2) NOT NULL,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_items_order` (`order_id`),
    KEY `fk_items_product` (`product_id`),
    CONSTRAINT `fk_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  Tabela: blog_posts (blog objave)
-- ---------------------------------------------------------------------
CREATE TABLE `blog_posts` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL,
    `content` TEXT NOT NULL,
    `image_url` VARCHAR(500) DEFAULT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `author_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_blog_author` (`author_id`),
    CONSTRAINT `fk_blog_author` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  Tabela: refresh_tokens (JWT refresh tokeni)
-- ---------------------------------------------------------------------
CREATE TABLE `refresh_tokens` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `token` VARCHAR(500) NOT NULL,
    `expiry_date` DATETIME(6) NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_refresh_token` (`token`),
    UNIQUE KEY `uk_refresh_user` (`user_id`),
    CONSTRAINT `fk_refresh_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================================
--  POČETNI PODACI
-- =====================================================================

-- ---------------------------------------------------------------------
--  Role
-- ---------------------------------------------------------------------
INSERT INTO `roles` (`id`, `name`) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER');

-- ---------------------------------------------------------------------
--  Korisnici
--  Lozinke (BCrypt):
--    admin / admin123
--    kupac / kupac123
-- ---------------------------------------------------------------------
INSERT INTO `users` (`id`, `username`, `email`, `password`, `first_name`, `last_name`, `phone`, `address`, `created_at`, `enabled`) VALUES
    (1, 'admin', 'admin@meningsistem.rs',
        '$2a$10$hzxe/UqnApOhsv.qM8/EcudviG52Yzu45zUhV0uQ/pmZdbLXKkN7K',
        'Vlasnik', 'Stamparije', '026/123-456', 'Smederevo', NOW(6), b'1'),
    (2, 'kupac', 'kupac@example.com',
        '$2a$10$1dyalKh9.QvyoDYVypdxzefVCCHXnpgNAdhA1jz5AfWaK3ENKSOve',
        'Petar', 'Petrovic', '064/123-456', 'Beograd, Knez Mihailova 1', NOW(6), b'1');

-- ---------------------------------------------------------------------
--  User-Role veze (ManyToMany)
--    admin ima role ROLE_ADMIN i ROLE_USER
--    kupac ima samo ROLE_USER
-- ---------------------------------------------------------------------
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
    (1, 1),
    (1, 2),
    (2, 2);

-- ---------------------------------------------------------------------
--  Kategorije
-- ---------------------------------------------------------------------
INSERT INTO `categories` (`id`, `name`, `description`, `image_url`) VALUES
    (1, 'Kutije za roštilj', 'Kartonske kutije sa štampom za pakovanje pečenog mesa i roštilja. Idealne za mesare, restorane i prodavnice gotove hrane.', '/uploads/IMG_1062.JPG'),
    (2, 'Kutije za pomfrit i hranu', 'Praktične srebrne kutije za pomfrit, čevape i drugu toplu hranu sa unutrašnjom aluminijumskom oblogom.', '/uploads/IMG_1064.JPG'),
    (3, 'Kutije za kolače', 'Bele kutije za kolače sa providnim prozorom – pravougaone i kvadratne, različitih dimenzija.', '/uploads/IMG_1065.JPG'),
    (4, 'Podmetači za piće', 'Papirni i kartonski podmetači za piće sa štampom po želji.', '/uploads/IMG_1065.JPG'),
    (5, 'Kalendari', 'Zidni i stoni kalendari ofset štampe.', '/uploads/IMG_1065.JPG'),
    (6, 'Blokovska roba', 'Otpremnice, računi, reversi – sve vrste blokovske robe za firme.', '/uploads/IMG_1066.JPG');

-- ---------------------------------------------------------------------
--  Proizvodi
-- ---------------------------------------------------------------------
INSERT INTO `products` (`id`, `name`, `description`, `price`, `dimensions`, `image_url`, `stock`, `available`, `created_at`, `category_id`) VALUES
    (1, 'Kartonska kutija za roštilj - velika (Jonas)', 'Štampana kartonska kutija za roštilj sa motivom mesa, dimenzije 30x20x5cm. Praktična za pakovanje roštilja, kao na primer Prerada mesa Jonas iz Kovačice.', 45.00, '30x20x5cm', '/uploads/IMG_1062.JPG', 500, b'1', NOW(6), 1),
    (2, 'Kartonska kutija za roštilj - srednja', 'Štampana kartonska kutija za pojedinačne porcije roštilja, dimenzije 25x15x5cm.', 35.00, '25x15x5cm', '/uploads/IMG_1068.JPG', 800, b'1', NOW(6), 1),
    (3, 'Srebrna kutija za pomfrit/hranu', 'Kartonska kutija sa srebrnom aluminijumskom unutrašnjom oblogom, idealna za pakovanje pomfrita, čevapa i tople hrane. Zadržava temperaturu.', 28.00, '20x12x5cm', '/uploads/IMG_1064.JPG', 1000, b'1', NOW(6), 2),
    (4, 'Srebrna kutija - velika porcija', 'Veća srebrna kutija za veće porcije hrane, sa unutrašnjom aluminijumskom oblogom.', 38.00, '25x15x6cm', '/uploads/IMG_1067.JPG', 600, b'1', NOW(6), 2),
    (5, 'Kvadratna kutija za kolače sa prozorom', 'Bela kartonska kutija za kolače sa providnim PVC prozorom i ukrasnom ivicom, dimenzije 22x22x6cm.', 65.00, '22x22x6cm', '/uploads/IMG_1065.JPG', 400, b'1', NOW(6), 3),
    (6, 'Pravougaona kutija za kolače sa prozorom', 'Manja pravougaona kutija za sitnije kolače, sa providnim PVC prozorom, dimenzije 18x12x5cm.', 45.00, '18x12x5cm', '/uploads/IMG_1066.JPG', 500, b'1', NOW(6), 3),
    (7, 'Kvadratna kutija za kolače - veliki format', 'Veća kvadratna kutija za torte i veće porcije kolača, sa providnim prozorom.', 85.00, '28x28x8cm', '/uploads/IMG_1070.JPG', 300, b'1', NOW(6), 3),
    (8, 'Papirni podmetač za piće - okrugli', 'Apsorbujući papirni podmetač za piće, prečnik 9cm, štampa po želji. Pakovanje od 100 komada.', 450.00, 'Ø9cm / 100kom', '/uploads/IMG_1065.JPG', 200, b'1', NOW(6), 4),
    (9, 'Zidni kalendar A3 (12 listova)', 'Zidni kalendar formata A3, 12 listova, ofset štampa u punom koloru, sa spiralom.', 550.00, 'A3 / 12 listova', '/uploads/IMG_1065.JPG', 150, b'1', NOW(6), 5),
    (10, 'Stoni kalendar', 'Stoni kalendar sa postoljem, 12 mesečnih listova, ofset štampa.', 380.00, '21x15cm', '/uploads/IMG_1065.JPG', 200, b'1', NOW(6), 5),
    (11, 'Blok računa A5 (NCR papir)', 'Blok računa, NCR papir (samokopirajući), 50 listova u dva primerka (original + kopija).', 220.00, 'A5 / 50x2', '/uploads/IMG_1066.JPG', 300, b'1', NOW(6), 6),
    (12, 'Otpremnica A4', 'Blok otpremnica A4 format, NCR papir, 100 listova u tri primerka.', 320.00, 'A4 / 100x3', '/uploads/IMG_1066.JPG', 250, b'1', NOW(6), 6),
    (13, 'Revers A5', 'Blok reversa A5, 50 listova u dva primerka.', 200.00, 'A5 / 50x2', '/uploads/IMG_1066.JPG', 280, b'1', NOW(6), 6);

-- ---------------------------------------------------------------------
--  Primer porudžbine (kupac je naručio 2 proizvoda)
-- ---------------------------------------------------------------------
INSERT INTO `orders` (`id`, `order_date`, `status`, `delivery_type`, `delivery_address`, `contact_phone`, `note`, `total_price`, `user_id`) VALUES
    (1, NOW(6), 'PRIMLJENA', 'DOSTAVA_NA_ADRESU', 'Knez Mihailova 1, Beograd', '064/123-456', 'Molim hitno isporučiti, potrebno za vikend.', 1850.00, 2);

INSERT INTO `order_items` (`id`, `quantity`, `unit_price`, `order_id`, `product_id`) VALUES
    (1, 10, 45.00, 1, 1),
    (2, 50, 28.00, 1, 3);

-- ---------------------------------------------------------------------
--  Blog objave (admin kao autor)
-- ---------------------------------------------------------------------
INSERT INTO `blog_posts` (`id`, `title`, `content`, `image_url`, `created_at`, `updated_at`, `author_id`) VALUES
    (1, 'Dobrodošli na zvaničnu stranicu Štamparije MEN-ING SISTEM DOO',
        'Smederevo, već dugi niz godina ponosno postoji 12 štamparija, a mi smo jedna od dve koje su preostale na tržištu. Naša štamparija MEN-ING SISTEM DOO specijalizovana je za proizvodnju kartonske ambalaže – kutija za roštilj, kutija za pomfrit i toplu hranu, kutija za kolače, podmetača za piće, kalendara, kao i blokovske robe (otpremnice, računi, reversi). Posvećenost kvalitetu, brzina isporuke i prilagodljivost potrebama klijenata su naše glavne karakteristike. Hvala vam što ste uz nas!',
        '/uploads/IMG_1062.JPG', NOW(6), NOW(6), 1),
    (2, 'Nova ponuda kutija za kolače',
        'Sa zadovoljstvom predstavljamo proširenu ponudu kutija za kolače sa providnim prozorom. Sada u ponudi imamo kvadratne i pravougaone kutije u tri različite veličine. Idealne su za poslastičarnice, kafiće i privatne narudžbine. Dostupne su odmah na zalihama.',
        '/uploads/IMG_1070.JPG', NOW(6), NOW(6), 1),
    (3, 'Načini dostave – sada i kurirska služba',
        'Pored ličnog preuzimanja u našem pogonu u Smederevu i dostave na vašu adresu, od ove godine omogućili smo i slanje robe putem kurirskih službi širom Srbije. Pri kreiranju porudžbine možete izabrati željeni način dostave.',
        NULL, NOW(6), NOW(6), 1);

-- =====================================================================
--  PROVERA
-- =====================================================================
SELECT 'Skripta izvrsena uspesno!' AS Status;
SELECT
    (SELECT COUNT(*) FROM `roles`) AS `Role`,
    (SELECT COUNT(*) FROM `users`) AS `Korisnika`,
    (SELECT COUNT(*) FROM `user_roles`) AS `User-Role veza`,
    (SELECT COUNT(*) FROM `categories`) AS `Kategorija`,
    (SELECT COUNT(*) FROM `products`) AS `Proizvoda`,
    (SELECT COUNT(*) FROM `orders`) AS `Porudzbina`,
    (SELECT COUNT(*) FROM `order_items`) AS `Stavki porudzbine`,
    (SELECT COUNT(*) FROM `blog_posts`) AS `Blog objava`;
