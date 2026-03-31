-- ============================================================
--  SIM Activation Portal — Database Schema
--  MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS sim_portal
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE sim_portal;

-- ─── Customer ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS customer (
  customer_id  BIGINT       NOT NULL AUTO_INCREMENT,
  first_name   VARCHAR(100) NOT NULL,
  last_name    VARCHAR(100) NOT NULL,
  email        VARCHAR(150) NOT NULL UNIQUE,
  dob          DATE         NOT NULL,
  address      VARCHAR(500) NOT NULL,
  id_proof     VARCHAR(100) NOT NULL,
  PRIMARY KEY (customer_id),
  INDEX idx_customer_email (email)
) ENGINE=InnoDB;

-- ─── Offers ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS offers (
  offer_id    BIGINT         NOT NULL AUTO_INCREMENT,
  plan_name   VARCHAR(100)   NOT NULL,
  price       DECIMAL(10,2)  NOT NULL,
  validity    VARCHAR(50)    NOT NULL,
  description VARCHAR(300),
  data_limit  VARCHAR(50),
  calls       VARCHAR(50),
  sms         VARCHAR(50),
  PRIMARY KEY (offer_id)
) ENGINE=InnoDB;

-- ─── SIM ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS sim (
  sim_id      BIGINT      NOT NULL AUTO_INCREMENT,
  sim_number  VARCHAR(20) NOT NULL UNIQUE,
  status      ENUM('AVAILABLE','ACTIVE','INACTIVE','BLOCKED') NOT NULL DEFAULT 'AVAILABLE',
  customer_id BIGINT,
  PRIMARY KEY (sim_id),
  INDEX idx_sim_number (sim_number),
  CONSTRAINT fk_sim_customer
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
    ON DELETE SET NULL
) ENGINE=InnoDB;

-- ─── Seed Offers ────────────────────────────────────────────
INSERT INTO offers (plan_name, price, validity, description, data_limit, calls, sms) VALUES
  ('Basic',    99.00,  '28 Days', 'Great for light users',          '1 GB/day',  '100 min/day', '100 SMS/day'),
  ('Standard', 199.00, '28 Days', 'Best value for daily users',     '2 GB/day',  'Unlimited',   '100 SMS/day'),
  ('Premium',  399.00, '56 Days', 'Ultimate plan for power users',  '3 GB/day',  'Unlimited',   'Unlimited');

-- ─── Seed Sample SIM Cards ──────────────────────────────────
INSERT INTO sim (sim_number, status) VALUES
  ('8901260123456789012', 'AVAILABLE'),
  ('8901260123456789013', 'AVAILABLE'),
  ('8901260123456789014', 'AVAILABLE'),
  ('8901260123456789015', 'AVAILABLE'),
  ('8901260123456789016', 'AVAILABLE');
