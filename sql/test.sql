CREATE DATABASE IF NOT EXISTS test_db DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

use test_db

CREATE TABLE IF NOT EXISTS `order`(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL
);


INSERT INTO `order`(id, order_number, status) value(1, '201804241200000001', 'completed');
INSERT INTO `order`(id, order_number, status) value(2, '201804241200000002', 'unpaid');
INSERT INTO `order`(id, order_number, status) value(3, '201804241200000003', 'unpaid');
INSERT INTO `order`(id, order_number, status) value(4, '201804241200000004', 'paid');
INSERT INTO `order`(id, order_number, status) value(5, '201804241200000005', "unpaid");

