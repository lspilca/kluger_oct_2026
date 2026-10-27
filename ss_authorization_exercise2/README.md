# Training Room Booking API — Spring Security Authorization Exercise

A small REST API where employees reserve training rooms. Controllers, services, sample data and
**authentication (HTTP Basic)** are already done. **Authorization is not implemented yet. Adding it is
your task.** The requirements are in the handout `Spring_Security_Authorization_Exercise2.pdf`.

> Until you finish the exercise, every request is permitted. For example, any user (or nobody) can read any
> reservation or the admin report. That is expected.

Stack: Java 21, Spring Boot 3.5, Spring Security 6, Spring Web, Spring Data JPA, H2 (in memory), Maven.

---

## 1. Starting the application

```bash
# Linux / macOS / Git Bash
./mvnw spring-boot:run

# Windows (cmd / PowerShell)
mvnw.cmd spring-boot:run
```

The API runs on <http://localhost:8080>. The database is in memory and is **recreated with the same data on
every start**, so restart the application to undo your changes.

Run the tests:

```bash
./mvnw test
```

---

## 2. Users

All users have the password **`password`**. They are defined in memory in `config/UserConfig.java`.

| Username | Role          | Extra authority | Granted authorities              |
|----------|---------------|-----------------|----------------------------------|
| `alice`  | EMPLOYEE      | –               | `ROLE_EMPLOYEE`                  |
| `bob`    | EMPLOYEE      | –               | `ROLE_EMPLOYEE`                  |
| `emma`   | EMPLOYEE      | ROOM_APPROVE    | `ROLE_EMPLOYEE`, `ROOM_APPROVE`  |
| `carl`   | COORDINATOR   | –               | `ROLE_COORDINATOR`               |
| `cora`   | COORDINATOR   | ROOM_APPROVE    | `ROLE_COORDINATOR`, `ROOM_APPROVE` |
| `admin`  | ADMIN         | –               | `ROLE_ADMIN`                     |

Notes:

- Roles are stored as authorities with the `ROLE_` prefix. `ROOM_APPROVE` is a plain authority with no prefix.
- `ROOM_APPROVE` is granted **independently of the role**. An employee (`emma`) has it, while a coordinator
  (`carl`) and the admin (`admin`) do not.
- `alice` and `bob` are both employees who own different reservations, which lets you test ownership checks.

---

## 3. Endpoints

| Method   | Path                              | Purpose                                   | Service method called                         |
|----------|-----------------------------------|-------------------------------------------|-----------------------------------------------|
| `GET`    | `/api/rooms`                      | List available rooms                      | `RoomService.listRooms()`                     |
| `POST`   | `/api/reservations`               | Create a reservation (owner = caller)     | `ReservationService.createReservation(...)`   |
| `GET`    | `/api/reservations/{id}`          | View reservation details                  | `ReservationService.getReservation(id)`       |
| `DELETE` | `/api/reservations/{id}`          | Cancel a reservation (status → CANCELLED) | `ReservationService.cancelAnyReservation(id)` |
| `GET`    | `/api/admin/usage`                | Room utilization report                   | `UsageReportService.getUsageReport()`         |
| `POST`   | `/api/reservations/{id}/approve`  | Approve a PENDING reservation             | `ReservationService.approveReservation(id)`   |
| `PATCH`  | `/api/reservations/{id}/status`   | Change the status of a reservation        | `ReservationService.changeReservationStatus(id, status)` |

The last two endpoints are helpers. They let you call `approveReservation()` (Requirement 4) and
`changeReservationStatus()` (Bonus) over HTTP. They are `/api/**` endpoints like the others.

Reservation statuses: `PENDING`, `CONFIRMED`, `COMPLETED`, `REJECTED`, `CANCELLED`.

Business responses: `404` for an unknown room or reservation, `400` for invalid input (bad time range or
unknown status), `409` for conflicts (overlapping booking, approving a non-PENDING reservation, or cancelling
a reservation that is already CANCELLED or COMPLETED).

### Example requests (curl)

```bash
# List rooms
curl http://localhost:8080/api/rooms

# Create a reservation as alice
curl -u alice:password -H "Content-Type: application/json" \
     -d '{"roomId": 2, "startTime": "2026-12-01T09:00:00", "endTime": "2026-12-01T10:00:00"}' \
     http://localhost:8080/api/reservations

# Read a reservation
curl -u alice:password http://localhost:8080/api/reservations/1

# Cancel a reservation
curl -u carl:password -X DELETE http://localhost:8080/api/reservations/3

# Approve a reservation
curl -u cora:password -X POST http://localhost:8080/api/reservations/1/approve

# Change a reservation's status
curl -u carl:password -X PATCH -H "Content-Type: application/json" \
     -d '{"status": "COMPLETED"}' http://localhost:8080/api/reservations/1/status

# Usage report
curl -u admin:password http://localhost:8080/api/admin/usage

# Wrong password -> 401 (authentication already works)
curl -i -u alice:wrong http://localhost:8080/api/rooms
```

> **Windows PowerShell:** use `curl.exe` instead of `curl`, and escape the JSON quotes, or use Postman or the
> IntelliJ HTTP client.

---

## 4. Sample data

### Rooms

| ID | Name              | Capacity | Location               |
|----|-------------------|----------|------------------------|
| 1  | Ada Lovelace      | 12       | Building A, Floor 1    |
| 2  | Alan Turing       | 20       | Building A, Floor 2    |
| 3  | Grace Hopper      | 8        | Building B, Floor 1    |
| 4  | Barbara Liskov    | 30       | Building B, Auditorium |
| 5  | Margaret Hamilton | 6        | Building C, Floor 3    |

### Reservations

| ID | Owner   | Room                  | Start            | End              | Status    |
|----|---------|-----------------------|------------------|------------------|-----------|
| 1  | `alice` | 1 – Ada Lovelace      | 2026-11-02 09:00 | 2026-11-02 12:00 | PENDING   |
| 2  | `alice` | 2 – Alan Turing       | 2026-11-05 10:00 | 2026-11-05 12:00 | CONFIRMED |
| 3  | `bob`   | 1 – Ada Lovelace      | 2026-11-03 09:00 | 2026-11-03 17:00 | CONFIRMED |
| 4  | `bob`   | 3 – Grace Hopper      | 2026-11-06 09:00 | 2026-11-06 11:00 | PENDING   |
| 5  | `emma`  | 4 – Barbara Liskov    | 2026-10-20 09:00 | 2026-10-20 17:00 | COMPLETED |
| 6  | `carl`  | 5 – Margaret Hamilton | 2026-11-09 14:00 | 2026-11-09 16:00 | CONFIRMED |
| 7  | `alice` | 3 – Grace Hopper      | 2026-11-06 09:00 | 2026-11-06 11:00 | REJECTED  |
| 8  | `bob`   | 2 – Alan Turing       | 2026-11-05 10:00 | 2026-11-05 12:00 | CANCELLED |
| 9  | `cora`  | 1 – Ada Lovelace      | 2026-11-04 13:00 | 2026-11-04 17:00 | PENDING   |
| 10 | `emma`  | 5 – Margaret Hamilton | 2026-11-10 09:00 | 2026-11-10 10:00 | PENDING   |

Reservations you create get IDs starting at 11. The data is loaded by `config/DataInitializer.java`.

---

## 5. H2 console

Open <http://localhost:8080/h2-console> and connect with:

| Field    | Value                       |
|----------|-----------------------------|
| JDBC URL | `jdbc:h2:mem:roombooking`   |
| User     | `sa`                        |
| Password | *(empty)*                   |

The console has its own security filter chain (`h2ConsoleSecurityFilterChain`) that is matched first, so the
rules you write for the API do not affect it.

---

## 6. Your tasks: where the TODOs are

Search the project for `TODO EXERCISE` and `TODO BONUS` (in IntelliJ: *View → Tool Windows → TODO*).

| TODO                 | File                                    | What to do                                         |
|----------------------|-----------------------------------------|----------------------------------------------------|
| `TODO EXERCISE 1`    | `config/SecurityConfig.java`            | Replace the temporary permit-all rule with endpoint-level rules |
| `TODO EXERCISE 2`    | `config/SecurityConfig.java`            | Enable method-level authorization                  |
| `TODO EXERCISE 3`    | `service/ReservationService.java`       | `cancelAnyReservation()`: COORDINATOR and ADMIN only |
| `TODO EXERCISE 4`    | `service/ReservationService.java`       | `approveReservation()`: ROOM_APPROVE authority only |
| `TODO EXERCISE 5`    | `service/ReservationService.java`       | `getReservation()`: owner or ADMIN, using `ReservationSecurity` |
| `TODO EXERCISE 6`    | `controller/ReservationController.java` | HTTP check and service check (see below)           |
| `TODO BONUS`         | `service/ReservationService.java`       | `changeReservationStatus()`: role and status policy in one expression |

Useful classes:

- `security/ReservationSecurity.java` is a bean named **`reservationSecurity`** with
  `boolean isOwner(Long reservationId, Authentication authentication)`. It is already implemented, but nothing
  calls it yet. Calling it from your authorization expression is part of Requirement 5.
- `config/UserConfig.java` defines the users and their authorities.

### Checking your solution

- `src/test/java/.../AuthorizationExerciseTest.java` contains **disabled** tests based on the handout's
  "Suggested checks". After you finish an exercise, remove the matching `@Disabled` annotations and run
  `./mvnw test`.
- `BusinessFunctionalityIntegrationTest` must stay green. It only uses callers that are allowed under the
  exercise rules.
- You can also check by hand with the curl requests above. Before you start, `curl -u bob:password
  http://localhost:8080/api/reservations/1` returns alice's reservation. After Requirement 5 it should return
  `403`.

### Requirement 6: your answer

Write your 2–4 sentence answer here:

> *Why are both the HTTP check and the service check useful? What does each one protect, and what happens if
> another component calls the service directly?*
>
> _Your answer:_
