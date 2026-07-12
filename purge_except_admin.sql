-- ====================================================================
-- LocalHub Database Purge Script (Except Admin User)
-- ====================================================================
-- This SQL script removes all transactional, catalog, provider, and customer 
-- data from the PostgreSQL database while preserving the core platform administrator 
-- login account and system permission settings.
-- ====================================================================

BEGIN;

-- 1. Purge Chat Messages
DELETE FROM chat_messages;

-- 2. Purge Bookings 
DELETE FROM bookings;

-- 3. Purge Wallet Ledgers (keeping only Admin wallet if exists)
DELETE FROM wallet_transactions;
DELETE FROM wallets WHERE user_id <> '00000000-0000-0000-0000-000000001001';

-- 4. Purge Inventory & Product Catalogs
DELETE FROM inventory;
DELETE FROM product_variants;
DELETE FROM products;

-- 5. Purge Service Offerings & Availability
DELETE FROM service_availability;
    DELETE FROM services;

-- 6. Purge Provider Profiles & Locations
DELETE FROM business_locations;
DELETE FROM business_category_map;
DELETE FROM businesses;

-- 7. Purge Marketing Banner campaigns & Promo Coupons
DELETE FROM banners;
DELETE FROM coupons;

-- 8. Purge OTP verification tokens
DELETE FROM otp_verifications;

-- 9. Purge All User Accounts & Roles
DELETE FROM user_roles;
DELETE FROM wallets;
DELETE FROM users;

-- 10. Recreate your Custom Administrator Account
-- Replace 'YourFirstName', 'YourLastName', 'your_email@localhub.test', and '+919000000001' with your own details.
-- Note: Generate a secure BCrypt password hash for the password_hash column (e.g. using online BCrypt generator).
INSERT INTO users (id, first_name, last_name, email, phone, password_hash, email_verified, phone_verified) VALUES
('00000000-0000-0000-0000-000000001001', 'YourFirstName', 'YourLastName', 'your_email@localhub.test', '+919000000001', '$2a$10$change-this-admin-hash-to-yours', TRUE, TRUE);

-- Map the new user to the ADMIN role ('00000000-0000-0000-0000-000000000104')
INSERT INTO user_roles (user_id, role_id) VALUES
('00000000-0000-0000-0000-000000001001', '00000000-0000-0000-0000-000000000104');

COMMIT;
