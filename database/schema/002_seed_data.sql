INSERT INTO roles (id, code, name, description) VALUES
('00000000-0000-0000-0000-000000000101', 'CUSTOMER', 'Customer', 'Marketplace customer'),
('00000000-0000-0000-0000-000000000102', 'SHOP_OWNER', 'Shop Owner', 'Business owner and seller'),
('00000000-0000-0000-0000-000000000103', 'DELIVERY_PARTNER', 'Delivery Partner', 'Delivery operator'),
('00000000-0000-0000-0000-000000000104', 'ADMIN', 'Admin', 'Platform administrator');

INSERT INTO permissions (id, code, name, description) VALUES
('00000000-0000-0000-0000-000000000201', 'SHOP_APPROVE', 'Approve shops', 'Approve or reject business registrations'),
('00000000-0000-0000-0000-000000000202', 'PRODUCT_MANAGE', 'Manage products', 'Create, update, and delete products'),
('00000000-0000-0000-0000-000000000203', 'SERVICE_MANAGE', 'Manage services', 'Create, update, and delete services'),
('00000000-0000-0000-0000-000000000204', 'ORDER_MANAGE', 'Manage orders', 'Accept, reject, and update orders'),
('00000000-0000-0000-0000-000000000205', 'BOOKING_MANAGE', 'Manage bookings', 'Accept, reject, and update bookings'),
('00000000-0000-0000-0000-000000000206', 'ADMIN_DASHBOARD', 'Admin dashboard', 'View platform analytics and settings');

INSERT INTO role_permissions (role_id, permission_id) VALUES
('00000000-0000-0000-0000-000000000104', '00000000-0000-0000-0000-000000000201'),
('00000000-0000-0000-0000-000000000104', '00000000-0000-0000-0000-000000000206'),
('00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000202'),
('00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000203'),
('00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000204'),
('00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000205');

INSERT INTO users (id, first_name, last_name, email, phone, password_hash, email_verified, phone_verified) VALUES
('00000000-0000-0000-0000-000000001001', 'Admin', 'User', 'admin@localhub.test', '+919000000001', '$2a$10$change-this-admin-hash', TRUE, TRUE),
('00000000-0000-0000-0000-000000001002', 'Sai', 'Customer', 'customer@localhub.test', '+919000000002', '$2a$10$change-this-customer-hash', TRUE, TRUE),
('00000000-0000-0000-0000-000000001003', 'Anita', 'Owner', 'owner@localhub.test', '+919000000003', '$2a$10$change-this-owner-hash', TRUE, TRUE),
('00000000-0000-0000-0000-000000001004', 'Dev', 'Delivery', 'delivery@localhub.test', '+919000000004', '$2a$10$change-this-delivery-hash', TRUE, TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES
('00000000-0000-0000-0000-000000001001', '00000000-0000-0000-0000-000000000104'),
('00000000-0000-0000-0000-000000001002', '00000000-0000-0000-0000-000000000101'),
('00000000-0000-0000-0000-000000001003', '00000000-0000-0000-0000-000000000102'),
('00000000-0000-0000-0000-000000001004', '00000000-0000-0000-0000-000000000103');

INSERT INTO business_categories (id, code, name, description, display_order) VALUES
('00000000-0000-0000-0000-000000002001', 'GROCERY', 'Grocery', 'Grocery stores and supermarkets', 1),
('00000000-0000-0000-0000-000000002002', 'CLOTHING', 'Clothing', 'Apparel and fashion stores', 2),
('00000000-0000-0000-0000-000000002003', 'ELECTRONICS', 'Electronics', 'Electronics and gadgets', 3),
('00000000-0000-0000-0000-000000002004', 'PHARMACY', 'Pharmacy', 'Medical stores and pharmacies', 4),
('00000000-0000-0000-0000-000000002005', 'RESTAURANTS', 'Restaurants', 'Restaurants, cafes, and food outlets', 5),
('00000000-0000-0000-0000-000000002006', 'HOME_SERVICES', 'Home Services', 'Home repair and maintenance services', 6),
('00000000-0000-0000-0000-000000002007', 'REPAIR_SHOPS', 'Repair Shops', 'Repair and maintenance shops', 7),
('00000000-0000-0000-0000-000000002008', 'BEAUTY_SALON', 'Beauty Salon', 'Salon and beauty services', 8),
('00000000-0000-0000-0000-000000002009', 'LAUNDRY', 'Laundry', 'Laundry and dry cleaning', 9),
('00000000-0000-0000-0000-000000002010', 'PET_SHOP', 'Pet Shop', 'Pet supplies and care', 10),
('00000000-0000-0000-0000-000000002011', 'FURNITURE', 'Furniture', 'Furniture stores and services', 11),
('00000000-0000-0000-0000-000000002012', 'HARDWARE', 'Hardware', 'Hardware and building supplies', 12);

INSERT INTO product_categories (id, code, name, description, display_order) VALUES
('00000000-0000-0000-0000-000000003001', 'FOOD_STAPLES', 'Food Staples', 'Rice, pulses, oils, and staples', 1),
('00000000-0000-0000-0000-000000003002', 'MOBILE_ACCESSORIES', 'Mobile Accessories', 'Chargers, cables, cases, and accessories', 2),
('00000000-0000-0000-0000-000000003003', 'MEDICINES', 'Medicines', 'OTC and prescription medicines', 3),
('00000000-0000-0000-0000-000000003004', 'CLOTHING_MENS', 'Mens Clothing', 'Mens apparel', 4);

INSERT INTO service_categories (id, code, name, description, display_order) VALUES
('00000000-0000-0000-0000-000000004001', 'ELECTRICIAN', 'Electrician', 'Electrical repair services', 1),
('00000000-0000-0000-0000-000000004002', 'PLUMBER', 'Plumber', 'Plumbing services', 2),
('00000000-0000-0000-0000-000000004003', 'AC_REPAIR', 'AC Repair', 'Air conditioner service and repair', 3),
('00000000-0000-0000-0000-000000004004', 'SALON_SERVICES', 'Salon Services', 'Hair, beauty, and grooming services', 4);

INSERT INTO businesses (id, owner_id, name, legal_name, owner_name, phone, email, gst_number, business_type, approval_status, verification_status, approved_by, approved_at, provides_products, provides_services, commission_rate) VALUES
('00000000-0000-0000-0000-000000005001', '00000000-0000-0000-0000-000000001003', 'Anita Local Mart', 'Anita Local Mart Pvt Ltd', 'Anita Owner', '+919000000103', 'shop@localhub.test', '29ABCDE1234F1Z5', 'BOTH', 'APPROVED', 'VERIFIED', '00000000-0000-0000-0000-000000001001', now(), TRUE, TRUE, 8.50);

INSERT INTO business_category_map (business_id, category_id) VALUES
('00000000-0000-0000-0000-000000005001', '00000000-0000-0000-0000-000000002001'),
('00000000-0000-0000-0000-000000005001', '00000000-0000-0000-0000-000000002006');

INSERT INTO business_locations (id, business_id, name, phone, line1, city, state, postal_code, latitude, longitude, opening_time, closing_time, service_radius_km, is_primary) VALUES
('00000000-0000-0000-0000-000000006001', '00000000-0000-0000-0000-000000005001', 'Main Branch', '+919000000103', 'MG Road', 'Bengaluru', 'Karnataka', '560001', 12.9716000, 77.5946000, '09:00', '21:00', 8.00, TRUE);

INSERT INTO products (id, business_id, category_id, name, slug, brand, description, base_price, discount_price, status) VALUES
('00000000-0000-0000-0000-000000007001', '00000000-0000-0000-0000-000000005001', '00000000-0000-0000-0000-000000003001', 'Premium Rice 5kg', 'premium-rice-5kg', 'LocalHub Fresh', 'Long grain rice pack', 450.00, 420.00, 'PUBLISHED');

INSERT INTO product_variants (id, product_id, sku, barcode, variant_name, attributes, price, discount_price) VALUES
('00000000-0000-0000-0000-000000008001', '00000000-0000-0000-0000-000000007001', 'RICE-5KG-001', '8900000000011', '5 kg pack', '{"weight":"5kg"}'::jsonb, 450.00, 420.00);

INSERT INTO inventory (business_location_id, product_variant_id, stock_quantity, reserved_quantity, reorder_level) VALUES
('00000000-0000-0000-0000-000000006001', '00000000-0000-0000-0000-000000008001', 100, 0, 10);

INSERT INTO services (id, business_id, category_id, name, slug, description, duration_minutes, price, home_visit, in_shop_service, status) VALUES
('00000000-0000-0000-0000-000000009001', '00000000-0000-0000-0000-000000005001', '00000000-0000-0000-0000-000000004001', 'Basic Electrical Visit', 'basic-electrical-visit', 'Inspection and small electrical fixes', 60, 299.00, TRUE, FALSE, 'PUBLISHED');

INSERT INTO service_availability (service_id, business_location_id, day_of_week, start_time, end_time, slot_capacity) VALUES
('00000000-0000-0000-0000-000000009001', '00000000-0000-0000-0000-000000006001', 1, '10:00', '18:00', 2),
('00000000-0000-0000-0000-000000009001', '00000000-0000-0000-0000-000000006001', 2, '10:00', '18:00', 2);

INSERT INTO wallets (id, user_id, balance, currency) VALUES
('00000000-0000-0000-0000-000000010001', '00000000-0000-0000-0000-000000001002', 0.00, 'INR');

INSERT INTO coupons (id, code, name, description, discount_type, discount_value, max_discount_amount, min_order_amount, starts_at, ends_at, usage_limit, per_user_limit) VALUES
('00000000-0000-0000-0000-000000011001', 'WELCOME50', 'Welcome Offer', 'Flat 50 off on first order', 'FIXED', 50.00, 50.00, 299.00, now(), now() + interval '365 days', 10000, 1);

INSERT INTO banners (title, subtitle, image_url, target_type, target_id, placement, display_order, starts_at, ends_at) VALUES
('LocalHub Launch', 'Find shops and services near you', 'https://cdn.localhub.test/banners/launch.jpg', 'CATEGORY', '00000000-0000-0000-0000-000000002001', 'HOME_TOP', 1, now(), now() + interval '90 days');
