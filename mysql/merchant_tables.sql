USE simulator_dev;


-- ============================================================
-- MERCHANT ID SEQUENCE
-- ============================================================

CREATE TABLE IF NOT EXISTS merchant_id_sequence (

    id BIGINT NOT NULL PRIMARY KEY,

    current_value BIGINT NOT NULL

);


INSERT INTO merchant_id_sequence (
    id,
    current_value
)

VALUES (
    1,
    54000000
)

ON DUPLICATE KEY UPDATE
    id = id;


-- ============================================================
-- CREATED MERCHANT RECORDS
-- ============================================================

CREATE TABLE IF NOT EXISTS merchant_id_created (

    id BIGINT NOT NULL AUTO_INCREMENT,

    account_number VARCHAR(50) NOT NULL,

    merchant_id VARCHAR(8) NOT NULL UNIQUE,

    merchant_name VARCHAR(150) NOT NULL,

    city VARCHAR(100) NOT NULL,

    country_code VARCHAR(20) NOT NULL,

    postal_code VARCHAR(30) NOT NULL,

    merchant_type VARCHAR(50) NOT NULL,

    mobile_number VARCHAR(20) NOT NULL,

    email_id VARCHAR(150) NOT NULL,

    charges DECIMAL(18,2) NOT NULL,

    qr_string TEXT NOT NULL,

    qr_image MEDIUMBLOB NOT NULL,

    created_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    UNIQUE KEY uk_account_merchant (
        account_number,
        merchant_id
    )

);