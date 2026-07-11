# LocalHub Project Structure

LocalHub is organized as a modular monolith. Each business capability owns its controller, DTOs, entities, repositories, services, mappers, validation, exceptions, and tests.

## Backend

Base package: `com.sai.geoLocation.localhub`

Modules:

- `authentication`
- `user`
- `shop`
- `category`
- `product`
- `servicecatalog`
- `booking`
- `cart`
- `order`
- `payment`
- `notification`
- `review`
- `search`
- `admin`
- `delivery`
- `wishlist`
- `coupon`
- `analytics`

Shared infrastructure lives under `common`, including security, configuration, validation, pagination, caching, audit, logging, storage, and web exception handling.

## Frontend

- `frontend/admin-dashboard`: ReactJS admin dashboard.
- `frontend/mobile-app`: React Native customer/shop-owner/mobile workflows.

## Database And Deployment

- `database/schema`: PostgreSQL schema artifacts.
- `database/migrations`: database migration scripts.
- `database/diagrams`: ER diagrams.
- `docker`: Docker build/runtime assets.
- `deploy`: cloud and local deployment configuration.
