# Toucan Payments - Customer Transactions Service

## Problem Understanding
This application provides a RESTful microservice to record, lookup, and transition customer transaction statuses. It enforces clean domain boundaries, input validation, unique transaction identifiers, and terminal status immutability.

---

## Validation & Business Rules
* **`transactionId`**: Required (`@NotBlank`), must be unique.
* **`customerId`**: Required (`@NotBlank`).
* **`amount`**: Required (`@NotNull`), strictly positive (`@Positive`).
* **`currency`**: Required (`@NotBlank`).
* **`transactionType`**: Required (`@NotNull`) (`PAYMENT`, `REFUND`, `TRANSFER`).
* **Status Lifecycle**:
  * Transactions initialize as `PENDING`.
  * `PENDING` transactions can move to `SUCCESS` or `FAILED`.
  * `SUCCESS` and `FAILED` are terminal states and cannot be modified.

---

## API Endpoints
* `POST /api/transactions` - Create transaction
* `GET /api/transactions/{transactionId}` - Get single transaction
* `GET /api/transactions/customer/{customerId}` - Get customer transactions
* `GET /api/transactions` - Get all transactions
* `PATCH /api/transactions/{transactionId}/status?status=SUCCESS` - Update status

---

## Testing Approach
Automated testing covers:
1. Successful transaction creation (`TransactionServiceTest`)
2. Validation rejection on blank/invalid fields (`TransactionControllerTest`)
3. Duplicate transaction ID rejection (`TransactionServiceTest`)
4. Exception throwing for non-existent IDs (`TransactionServiceTest`)
5. Prevention of status updates on terminal states (`TransactionServiceTest`)

---

## AI Usage Disclosure
* **Tools Used**: Gemini
* **Purpose**: Code scaffolding, setting up unit test assertions, exception mapping, and constructing an embedded dashboard UI.
* **Refining / Fixes**: Handled constructor mismatches across DTOs and entities when adding `TransactionType`, defined unique constraint exceptions, and ensured terminal status constraints throw HTTP 400.
* **Verification**: Ran full `./mvnw clean test` build and tested end-to-end flows via the web UI dashboard at `http://localhost:8082`.

---

## Limitations & Future Improvements
* Database is currently using an in-memory H2 database; production deployment would integrate PostgreSQL/MySQL.
* Authentication and authorization (JWT/OAuth2) can be integrated for secure access control.

## Test Run Output
(base) krishnatejam@Venkatas-MacBook-Air toucan-payments % ./mvnw clean test 
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< com.toucan:toucan-payments >---------------------
[INFO] Building toucan-payments 0.0.1-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.3.2:clean (default-clean) @ toucan-payments ---
[INFO] Deleting /Users/krishnatejam/Downloads/toucan-payments/target
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ toucan-payments ---
[INFO] Copying 1 resource from src/main/resources to target/classes
[INFO] Copying 1 resource from src/main/resources to target/classes
[INFO] 
[INFO] --- compiler:3.11.0:compile (default-compile) @ toucan-payments ---
[INFO] Changes detected - recompiling the module! :source
[INFO] Compiling 15 source files with javac [debug release 17] to target/classes
[INFO] 
[INFO] --- resources:3.3.1:testResources (default-testResources) @ toucan-payments ---
[INFO] skip non existing resourceDirectory /Users/krishnatejam/Downloads/toucan-payments/src/test/resources
[INFO] 
[INFO] --- compiler:3.11.0:testCompile (default-testCompile) @ toucan-payments ---
[INFO] Changes detected - recompiling the module! :dependency
[INFO] Compiling 3 source files with javac [debug release 17] to target/test-classes
[INFO] 
[INFO] --- surefire:3.1.2:test (default-test) @ toucan-payments ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.toucan.payments.TransactionControllerTest
14:57:21.616 [main] INFO org.springframework.test.context.support.AnnotationConfigContextLoaderUtils -- Could not detect default configuration classes for test class [com.toucan.payments.TransactionControllerTest]: TransactionControllerTest does not declare any static, non-private, non-final, nested classes annotated with @Configuration.
14:57:21.663 [main] INFO org.springframework.boot.test.context.SpringBootTestContextBootstrapper -- Found @SpringBootConfiguration com.toucan.payments.ToucanPaymentsApplication for test class com.toucan.payments.TransactionControllerTest

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.5)

2026-09-01T14:57:21.799+05:30  INFO 6706 --- [toucan-payments] [           main] c.t.payments.TransactionControllerTest   : Starting TransactionControllerTest using Java 17.0.12 with PID 6706 (started by krishnatejam in /Users/krishnatejam/Downloads/toucan-payments)
2026-09-01T14:57:21.800+05:30  INFO 6706 --- [toucan-payments] [           main] c.t.payments.TransactionControllerTest   : No active profile set, falling back to 1 default profile: "default"
Java HotSpot(TM) 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2026-09-01T14:57:22.524+05:30  INFO 6706 --- [toucan-payments] [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [static/index.html]
2026-09-01T14:57:22.660+05:30  INFO 6706 --- [toucan-payments] [           main] o.s.b.t.m.w.SpringBootMockServletContext : Initializing Spring TestDispatcherServlet ''
2026-09-01T14:57:22.660+05:30  INFO 6706 --- [toucan-payments] [           main] o.s.t.web.servlet.TestDispatcherServlet  : Initializing Servlet ''
2026-09-01T14:57:22.661+05:30  INFO 6706 --- [toucan-payments] [           main] o.s.t.web.servlet.TestDispatcherServlet  : Completed initialization in 0 ms
2026-09-01T14:57:22.671+05:30  INFO 6706 --- [toucan-payments] [           main] c.t.payments.TransactionControllerTest   : Started TransactionControllerTest in 0.985 seconds (process running for 1.359)
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.275 s -- in com.toucan.payments.TransactionControllerTest
[INFO] Running com.toucan.payments.TransactionServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.073 s -- in com.toucan.payments.TransactionServiceTest
[INFO] Running com.toucan.payments.ToucanPaymentsApplicationTests
2026-09-01T14:57:22.907+05:30  INFO 6706 --- [toucan-payments] [           main] t.c.s.AnnotationConfigContextLoaderUtils : Could not detect default configuration classes for test class [com.toucan.payments.ToucanPaymentsApplicationTests]: ToucanPaymentsApplicationTests does not declare any static, non-private, non-final, nested classes annotated with @Configuration.
2026-09-01T14:57:22.909+05:30  INFO 6706 --- [toucan-payments] [           main] .b.t.c.SpringBootTestContextBootstrapper : Found @SpringBootConfiguration com.toucan.payments.ToucanPaymentsApplication for test class com.toucan.payments.ToucanPaymentsApplicationTests

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.5)

2026-09-01T14:57:22.925+05:30  INFO 6706 --- [toucan-payments] [           main] c.t.p.ToucanPaymentsApplicationTests     : Starting ToucanPaymentsApplicationTests using Java 17.0.12 with PID 6706 (started by krishnatejam in /Users/krishnatejam/Downloads/toucan-payments)
2026-09-01T14:57:22.925+05:30  INFO 6706 --- [toucan-payments] [           main] c.t.p.ToucanPaymentsApplicationTests     : No active profile set, falling back to 1 default profile: "default"
2026-09-01T14:57:23.045+05:30  INFO 6706 --- [toucan-payments] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2026-09-01T14:57:23.061+05:30  INFO 6706 --- [toucan-payments] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 13 ms. Found 1 JPA repository interface.
2026-09-01T14:57:23.167+05:30  INFO 6706 --- [toucan-payments] [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2026-09-01T14:57:23.188+05:30  INFO 6706 --- [toucan-payments] [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.4.4.Final
2026-09-01T14:57:23.201+05:30  INFO 6706 --- [toucan-payments] [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2026-09-01T14:57:23.242+05:30  INFO 6706 --- [toucan-payments] [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2026-09-01T14:57:23.253+05:30  INFO 6706 --- [toucan-payments] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2026-09-01T14:57:23.329+05:30  INFO 6706 --- [toucan-payments] [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:transactions user=SA
2026-09-01T14:57:23.330+05:30  INFO 6706 --- [toucan-payments] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2026-09-01T14:57:23.341+05:30  WARN 6706 --- [toucan-payments] [           main] org.hibernate.orm.deprecation            : HHH90000025: H2Dialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2026-09-01T14:57:23.625+05:30  INFO 6706 --- [toucan-payments] [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
Hibernate: create table transactions (id bigint generated by default as identity, amount numeric(38,2) not null, created_at timestamp(6), currency varchar(255) not null, customer_id varchar(255) not null, status varchar(255) not null check (status in ('PENDING','SUCCESS','FAILED')), transaction_id varchar(255) not null, transaction_type varchar(255) not null check (transaction_type in ('PAYMENT','REFUND','TRANSFER')), primary key (id))
Hibernate: alter table if exists transactions drop constraint if exists UK_6plyfbm3wy6ds7hongoml5xbk
Hibernate: alter table if exists transactions add constraint UK_6plyfbm3wy6ds7hongoml5xbk unique (transaction_id)
2026-09-01T14:57:23.650+05:30  INFO 6706 --- [toucan-payments] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2026-09-01T14:57:23.810+05:30  WARN 6706 --- [toucan-payments] [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2026-09-01T14:57:23.816+05:30  INFO 6706 --- [toucan-payments] [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [static/index.html]
2026-09-01T14:57:23.878+05:30  INFO 6706 --- [toucan-payments] [           main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:transactions'
2026-09-01T14:57:23.900+05:30  INFO 6706 --- [toucan-payments] [           main] c.t.p.ToucanPaymentsApplicationTests     : Started ToucanPaymentsApplicationTests in 0.989 seconds (process running for 2.588)
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.998 s -- in com.toucan.payments.ToucanPaymentsApplicationTests
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.908 s
[INFO] Finished at: 2026-09-01T14:57:23+05:30
[INFO] ------------------------------------------------------------------------
