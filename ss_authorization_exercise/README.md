# Spring Security Authorization Exercise: Order Management API

A small Spring Boot REST API with **authentication fully configured** and **authorization deliberately
left unfinished**. Your job is to add endpoint-level (HTTP) and method-level (service) authorization.

- Java 25, Spring Boot 4.1.2, Spring Security 7, Maven (wrapper included)
- Servlet stack (Spring MVC), in-memory storage, no database
- Compiled with `-parameters`, so security expressions can refer to method parameters by name

## Build and run

```bash
./mvnw clean verify          # compile and run the tests (Windows: mvnw.cmd clean verify)
./mvnw spring-boot:run       # start on http://localhost:8080
```

Or run the jar: `java -jar target/ss_authorization_exercise-0.0.1-SNAPSHOT.jar`.

All data lives in memory and is reset to the same seed data on every restart.

Use [`requests.http`](requests.http) (IntelliJ HTTP Client) to send requests manually. With curl, use
`curl -u alice:password ...`.

## Demo accounts

All passwords are `password`.

| Username       | Stored authorities              |
|----------------|---------------------------------|
| `alice`        | `ROLE_CUSTOMER`                 |
| `bob`          | `ROLE_CUSTOMER`                 |
| `manager`      | `ROLE_MANAGER`                  |
| `admin`        | `ROLE_ADMIN`                    |
| `refund-agent` | `ROLE_CUSTOMER`, `ORDER_REFUND` |

- Roles use the standard `ROLE_` prefix. `ORDER_REFUND` is a plain authority without the prefix.
- `admin` does **not** have `ORDER_REFUND`.
- No role hierarchy is configured, so `ADMIN` does not imply `MANAGER`.
- Spring Security 7 may add extra authorities to the authenticated user, such as a password-factor
  authority. You can ignore them for this exercise.

### Authentication setup (provided, not part of the exercise)

`security/UserConfig.java` defines an `InMemoryUserDetailsManager` with BCrypt-encoded passwords
(via the delegating `PasswordEncoder`). `SecurityConfig` enables **HTTP Basic** with a **stateless**
session policy: credentials are sent on every request and no session cookie is created. **CSRF
protection is disabled** because this API is not used from a browser session. Without cookies there
is nothing for CSRF to exploit, and you can send POST, PATCH and DELETE requests without tokens and
focus on authorization.

## Seed data

Products:

| id | name        | price   |
|----|-------------|---------|
| 1  | Laptop      | 1299.00 |
| 2  | Headphones  | 199.99  |
| 3  | USB-C Cable | 12.50   |

Orders:

| id  | owner   | product | quantity | status     |
|-----|---------|---------|----------|------------|
| 101 | `alice` | 1       | 1        | CREATED    |
| 102 | `bob`   | 2       | 2        | CREATED    |
| 103 | `alice` | 3       | 3        | PROCESSING |

New orders get IDs from 1001 upward. Order statuses are `CREATED`, `PROCESSING`, `SHIPPED`, `CANCELLED`.

## Endpoints

| Method | Path                      | Delegates to                         | Success |
|--------|---------------------------|--------------------------------------|---------|
| GET    | `/api/products`           | `ProductService.listProducts()`      | 200     |
| POST   | `/api/orders`             | `OrderService.createOrder(...)`      | 201     |
| GET    | `/api/orders/{id}`        | `OrderService.getOrder(id)`          | 200     |
| DELETE | `/api/orders/{id}`        | `OrderService.cancelAnyOrder(id)`    | 204     |
| GET    | `/api/admin/reports`      | `OrderService.generateReport()`      | 200     |
| POST   | `/api/orders/{id}/refund` | `OrderService.refundOrder(id)`       | 204     |
| PATCH  | `/api/orders/{id}/status` | `OrderService.updateOrderStatus(..)` | 204     |

> **Supporting additions:** `POST /api/orders/{id}/refund` and `PATCH /api/orders/{id}/status`
> are not part of the original exercise specification. They exist only so that you can call
> `refundOrder()` and `updateOrderStatus()` manually.

Request bodies:

```json
POST  /api/orders              {"productId": 1, "quantity": 2}
PATCH /api/orders/{id}/status  {"status": "PROCESSING"}
```

- Cancelling sets the status to `CANCELLED`; the order is not removed.
- The order owner always comes from the authenticated user. Clients cannot set `ownerUsername`, and
  the field is ignored if they send it.
- A missing order returns **404**. Invalid input returns **400**: validation errors, an unknown
  product or status, malformed JSON, or a non-numeric ID.

## ⚠️ Initial state: intentionally insecure

The starter `SecurityFilterChain` **permits every request**, including anonymous ones, and method
security is **not** enabled. Right now:

- anyone can read the admin report, read anyone's order, cancel, refund and change statuses;
- an **anonymous** `POST /api/orders` succeeds, and the order is attributed to the placeholder user
  `guest`. This fallback in `OrderController` is **insecure starter scaffolding**. Once order
  creation requires authentication, the fallback can no longer be reached.

No role or authority checks exist anywhere in the Java code. Adding them is your job.

## Student checklist

TODOs are in `src/main/java/com/example/ss_authorization_exercise/security/SecurityConfig.java` and
`src/main/java/com/example/ss_authorization_exercise/service/OrderService.java`.

**HTTP authorization (`SecurityConfig`)**

- [ ] Replace the permit-all rule.
- [ ] `GET /api/products` is public.
- [ ] `/api/admin/**` requires the `ADMIN` role.
- [ ] `POST /api/orders` requires authentication.
- [ ] All remaining `/api/**` requests require authentication.
- [ ] All other requests are denied.
- [ ] Enable method security.

**Method authorization (`OrderService`)**

- [ ] `cancelAnyOrder(Long orderId)`: `MANAGER` or `ADMIN`.
- [ ] `refundOrder(Long orderId)`: requires the `ORDER_REFUND` authority, regardless of role.
- [ ] `getOrder(Long orderId)`: the order owner or `ADMIN`. Use the provided `orderSecurity` bean
      (`OrderSecurity#isOwner(Long, Authentication)`) for the ownership part.

For `getOrder()`, the HTTP layer only requires authentication. The service layer decides whether the
caller may see *this particular* order.


### Reflection

_Explain in 2–4 sentences why HTTP authorization and service-method authorization solve different
problems:_

> _Your answer here._

## Optional bonus

- [ ] `updateOrderStatus(Long orderId, String status)`:
  - `MANAGER` may set `PROCESSING` or `SHIPPED`;
  - `ADMIN` may set `PROCESSING`, `SHIPPED` or `CANCELLED`;
  - nobody else may change a status.

  Express the **entire rule in a single `@PreAuthorize` annotation**. The `status` parameter is a
  `String` on purpose.
