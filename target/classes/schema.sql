-- =======================================================
--   SIM Activation Portal — MySQL Schema
--   Run this manually OR let spring.jpa.hibernate.ddl-auto=update
--   handle it automatically on first startup.
-- =======================================================

CREATE DATABASE IF NOT EXISTS sim_activation_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sim_activation_db;

-- -------------------------------------------------------
--  Customers
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS customers (
    customer_id   BIGINT          NOT NULL AUTO_INCREMENT,
    first_name    VARCHAR(50)     NOT NULL,
    last_name     VARCHAR(50)     NOT NULL,
    email         VARCHAR(100)    NOT NULL UNIQUE,
    date_of_birth DATE            NOT NULL,
    address       VARCHAR(255)    NOT NULL,
    phone_number  VARCHAR(15),
    kyc_status    ENUM('PENDING','VERIFIED','REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at    DATETIME,
    updated_at    DATETIME,
    PRIMARY KEY (customer_id),
    INDEX idx_customer_email (email)
) ENGINE=InnoDB;

-- -------------------------------------------------------
--  SIM Cards
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS sim_cards (
    sim_id        BIGINT          NOT NULL AUTO_INCREMENT,
    sim_iccid     VARCHAR(22)     NOT NULL UNIQUE,
    mobile_number VARCHAR(15)     NOT NULL UNIQUE,
    status        ENUM('INACTIVE','ACTIVE','BLOCKED','EXPIRED') NOT NULL DEFAULT 'INACTIVE',
    sim_type      ENUM('PREPAID','POSTPAID')                    NOT NULL DEFAULT 'PREPAID',
    operator_code VARCHAR(10),
    created_at    DATETIME,
    updated_at    DATETIME,
    PRIMARY KEY (sim_id),
    INDEX idx_sim_mobile (mobile_number)
) ENGINE=InnoDB;

-- -------------------------------------------------------
--  Activations
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS activations (
    activation_id     BIGINT       NOT NULL AUTO_INCREMENT,
    customer_id       BIGINT       NOT NULL,
    sim_id            BIGINT       NOT NULL,
    activation_status ENUM('PENDING','SUCCESS','FAILED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    plan_selected     VARCHAR(100),
    remarks           VARCHAR(255),
    activated_at      DATETIME,
    created_at        DATETIME,
    updated_at        DATETIME,
    PRIMARY KEY (activation_id),
    CONSTRAINT fk_activation_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT fk_activation_sim      FOREIGN KEY (sim_id)      REFERENCES sim_cards(sim_id),
    INDEX idx_activation_customer (customer_id),
    INDEX idx_activation_sim      (sim_id)
) ENGINE=InnoDB;

-- -------------------------------------------------------
--  Offers
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS offers (
    offer_id            BIGINT          NOT NULL AUTO_INCREMENT,
    offer_name          VARCHAR(100)    NOT NULL,
    description         VARCHAR(500),
    price               DECIMAL(10,2)   NOT NULL,
    validity_days       INT             NOT NULL,
    data_gb             DECIMAL(5,2),
    calling_minutes     INT,
    sms_count           INT,
    sim_type_eligible   ENUM('PREPAID','POSTPAID'),
    valid_from          DATE,
    valid_to            DATE,
    is_active           TINYINT(1)      NOT NULL DEFAULT 1,
    created_at          DATETIME,
    PRIMARY KEY (offer_id),
    INDEX idx_offer_active (is_active)
) ENGINE=InnoDB;

-- -------------------------------------------------------
--  Sample seed data (SIMs)
-- -------------------------------------------------------
INSERT IGNORE INTO sim_cards (sim_iccid, mobile_number, status, sim_type, operator_code)
VALUES
    ('8991101200003204510', '9876543210', 'INACTIVE', 'PREPAID',  'IND01'),
    ('8991101200003204511', '9876543211', 'INACTIVE', 'POSTPAID', 'IND01'),
    ('8991101200003204512', '9876543212', 'INACTIVE', 'PREPAID',  'IND02'),
    ('8991101200003204513', '9876543213', 'ACTIVE',   'PREPAID',  'IND02');

-- -------------------------------------------------------
--  Sample seed data (Offers)
-- -------------------------------------------------------
INSERT IGNORE INTO offers
    (offer_name, description, price, validity_days, data_gb, calling_minutes, sms_count,
     sim_type_eligible, valid_from, valid_to, is_active)
VALUES
    ('Starter Pack',      'Entry-level prepaid plan.',             99.00,  28,  1.00, 100,  100, 'PREPAID',  CURDATE() - INTERVAL 30 DAY, CURDATE() + INTERVAL 180 DAY, 1),
    ('Power User',        'High-data prepaid plan.',              299.00,  56, 50.00, 500,  200, 'PREPAID',  CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 365 DAY, 1),
    ('Business Unlimited','Unlimited postpaid plan.',             799.00,  30,100.00,NULL,  500, 'POSTPAID', CURDATE() - INTERVAL  5 DAY, CURDATE() + INTERVAL 365 DAY, 1),
    ('Weekend Special',   'Limited-time free weekend data offer.',149.00,  14, 10.00, 200,   50, 'PREPAID',  CURDATE() - INTERVAL  1 DAY, CURDATE() + INTERVAL  30 DAY, 1);
