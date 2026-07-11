# LocalHub Database Relationships

LocalHub uses a normalized PostgreSQL schema with UUID primary keys on every table. All domain tables include `created_at`, `updated_at`, `created_by`, `updated_by`, `is_active`, `is_deleted`, and `version` so the application can support soft deletes, auditing, and optimistic locking.

## Identity And Access

- `users` is the root identity table for customers, shop owners, delivery partners, and admins.
- `roles` and `permissions` are lookup tables.
- `user_roles` and `role_permissions` are many-to-many join tables for role-based authorization.
- `auth_identities` stores email, phone OTP, and Google login identities for a user.
- `refresh_tokens`, `otp_verifications`, and `password_reset_tokens` support JWT refresh, mobile OTP, and forgot-password workflows.
- `user_addresses` stores multiple saved addresses per user, including latitude and longitude.

## Business Registration And Approval

- `businesses` belongs to a shop owner in `users`.
- `businesses.business_type` supports `PRODUCTS`, `SERVICES`, or `BOTH`; `provides_products` and `provides_services` make filtering fast.
- `business_locations` supports multiple shop branches and service locations.
- `business_documents` stores verification document metadata and review status.
- `business_categories` is hierarchical, so categories like Grocery, Pharmacy, Restaurant, and future categories can be added without schema changes.
- `business_category_map` allows one business to belong to multiple categories.

## Products

- `product_categories` is a separate hierarchy for catalog classification.
- `products` belongs to one `business` and one `product_category`.
- `product_images` stores multiple images per product.
- `product_variants` stores SKU/barcode-level variants.
- `inventory` stores branch-level stock per product variant and business location.

## Services And Bookings

- `service_categories` is a separate hierarchy for service classification.
- `services` belongs to one `business` and one `service_category`.
- `service_availability` stores recurring availability rules by location and day of week.
- `booking_slots` stores concrete bookable slots.
- `bookings` links a customer, business, location, service, and optional slot.

## Cart, Orders, Payments

- `carts` belongs to a user and stores active/abandoned/checked-out state.
- `cart_items` supports either a product variant or a service through a check constraint.
- `orders` belongs to a customer and business, with optional shipping address and coupon.
- `order_items` supports product and service line items, optionally linked to a booking.
- `order_status_history` records lifecycle changes for order tracking.
- `payments` can pay for an order or booking and supports Razorpay, COD, wallet, and refunds.

## Engagement, Delivery, Wallet, Audit

- `coupons` and `coupon_redemptions` support platform and business promotions.
- `reviews` stores written review content for businesses, products, or services.
- `ratings` stores standalone numeric ratings for businesses, products, or services.
- `favorites` lets users save businesses, products, or services.
- `notifications` supports push, SMS, email, and in-app messages.
- `delivery_partners`, `delivery_assignments`, and `delivery_tracking` support delivery partner assignment and GPS tracking.
- `wallets` and `wallet_transactions` support wallet balance and transaction history.
- `offers` and `banners` power marketing surfaces.
- `audit_logs` records actor, action, entity, and JSON snapshots for compliance.

## Scale Notes

- UUIDs avoid sequence coordination in distributed deployments.
- Foreign keys preserve consistency while indexes cover high-volume lookup paths.
- Soft deletes keep historical orders, bookings, payments, and audits intact.
- Latitude/longitude are indexed for bounding-box search; PostGIS can be added later.
