### OAS Lite – Authentication and Authorization Security Assessment

### 1\. Document Information

| Field              | Details                                  |
| ------------------ | ---------------------------------------- |
| Project            | OAS Lite                                 |
| Assessment Type    | Authentication and Authorization Testing |
| Application        | Employee Attendance Management System    |
| Backend Framework  | Spring Boot                              |
| Security Framework | Spring Security                          |
| Database           | H2                                       |
| Assessment Status  | Remediated and Re-tested                 |
| Git Branch         | security/baseline-assessment             |
| Commit             | 71fb316                                  |

### 2\. Assessment Objective

The objective of this assessment is to evaluate the authentication and authorization controls implemented in OAS Lite.

The assessment focuses on verifying that:

* Unauthenticated users cannot access protected resources.

* Administrators can access administrative resources.

* Employees cannot access resources restricted to administrators.

* Access control rules are enforced by the server.

### 3\. Application Roles

The application currently defines two roles:

| Role     | Description                                                    |
| -------- | -------------------------------------------------------------- |
| ADMIN    | Can access employee management and attendance management APIs. |
| EMPLOYEE | Has restricted access to administrative APIs.                  |

### 4\. Security Configuration Reviewed

The following authorization rules were reviewed in `SecurityConfig.java`:

```java
.requestMatchers("/api/employees/**").hasRole("ADMIN")
.requestMatchers("/api/attendance/**").hasRole("ADMIN")
.anyRequest().authenticated()
```

Password handling was also reviewed:

* Passwords are read from environment variables.

* BCrypt is used through `BCryptPasswordEncoder`.

* User accounts are currently managed using `InMemoryUserDetailsManager`.

### 4.1 Remediation Applied

Before remediation, the application did not enforce authentication or role-based authorization on the tested API endpoints. Unauthenticated requests could access employee and attendance data and receive `200 OK`.

The remediation was applied by adding the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

After adding Spring Security, authentication and authorization rules were configured in `SecurityConfig.java`.

These rules require users to authenticate and restrict `/api/employees/**` and `/api/attendance/**` endpoints to users with the `ADMIN` role.

The results of the post-remediation tests are documented in Section 5.

### 5\. Test Results

### Test Results Summary

| Test ID   | Result | Before Fix                            | After Fix     |
| --------- | ------ | ------------------------------------- | ------------- |
| AUTH-001  | PASS   | 200 OK                                | 302 Found     |
| AUTHZ-001 | PASS   | No authentication or ADMIN role check | 200 OK        |
| AUTHZ-002 | PASS   | 200 OK                                | 403 Forbidden |
| AUTHZ-003 | PASS   | 200 OK                                | 403 Forbidden |
| AUTHZ-004 | PASS   | No authentication or ADMIN role check | 200 OK        |
| AUTH-002  | PASS   | No authentication required            | 302 Found     |

### AUTH-001 — Unauthenticated Access to Attendance API

| Field                              | Details                                                                                                                           |
| ---------------------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| Test ID                            | AUTH-001                                                                                                                          |
| Endpoint                           | GET /api/attendance                                                                                                               |
| User Role                          | Unauthenticated                                                                                                                   |
| Pre-Remediation State / Before Fix | The application had no authentication. An unauthenticated user could access the endpoint and receive 200 OK with attendance data. |
| Actual Result / After Fix          | 302 Found — the application redirected the unauthenticated request to /login.                                                     |
| Result                             | PASS                                                                                                                              |
| Evidence                           | [AUTH-001.jpg](evidence/AUTH-001.jpg)                                                                                                                      |

### AUTHZ-001 — ADMIN Access to Attendance API

| Field                              | Details                                                                                                                                       |
| ---------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| Test ID                            | AUTHZ-001                                                                                                                                     |
| Endpoint                           | GET /api/attendance                                                                                                                           |
| User Role                          | ADMIN                                                                                                                                         |
| Pre-Remediation State / Before Fix | Authentication and role-based authorization were not implemented. The endpoint was accessible without login, and no ADMIN role check existed. |
| Actual Result / After Fix          | 200 OK — the ADMIN account successfully accessed the attendance API.                                                                          |
| Result                             | PASS                                                                                                                                          |
| Evidence                           | [AUTHZ-001.jpg](evidence/AUTHZ-001.jpg)                                                                                                                                 |

### AUTHZ-002 — EMPLOYEE Access to Attendance API

| Field                              | Details                                                                                                                                                                                    |
| ---------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Test ID                            | AUTHZ-002                                                                                                                                                                                  |
| Endpoint                           | GET /api/attendance                                                                                                                                                                        |
| User Role                          | EMPLOYEE                                                                                                                                                                                   |
| Pre-Remediation State / Before Fix | The application had no authentication or role-based authorization. An unauthenticated user, including an EMPLOYEE user, could access the endpoint and receive 200 OK with attendance data. |
| Actual Result / After Fix          | 403 Forbidden — the authenticated EMPLOYEE account was denied access because the endpoint requires the ADMIN role.                                                                         |
| Result                             | PASS                                                                                                                                                                                       |
| Evidence                           | [AUTHZ-002.jpg](evidene/AUTHZ-002.jpg)                                                                                                                                                                              |

### AUTHZ-003 — EMPLOYEE Access to Employees API

| Field                              | Details                                                                                                                                                                                  |
| ---------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Test ID                            | AUTHZ-003                                                                                                                                                                                |
| Endpoint                           | GET /api/employees                                                                                                                                                                       |
| User Role                          | EMPLOYEE                                                                                                                                                                                 |
| Pre-Remediation State / Before Fix | The application had no authentication or role-based authorization. An unauthenticated user, including an EMPLOYEE user, could access the endpoint and receive 200 OK with employee data. |
| Actual Result / After Fix          | 403 Forbidden — the authenticated EMPLOYEE account was denied access because the endpoint requires the ADMIN role.                                                                       |
| Result                             | PASS                                                                                                                                                                                     |
| Evidence                           | [AUTHZ-003.jpg](evidence/AUTHZ-003.jpg)                                                                                                                                                                            |

### AUTHZ-004 — ADMIN Access to Employees API

| Field                              | Details                                                                                                                                       |
| ---------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| Test ID                            | AUTHZ-004                                                                                                                                     |
| Endpoint                           | GET /api/employees                                                                                                                            |
| User Role                          | ADMIN                                                                                                                                         |
| Pre-Remediation State / Before Fix | Authentication and role-based authorization were not implemented. The endpoint was accessible without login, and no ADMIN role check existed. |
| Actual Result / After Fix          | 200 OK — the ADMIN account successfully accessed the employees API.                                                                           |
| Result                             | PASS                                                                                                                                          |
| Evidence                           | [AUTHZ-004.jpg](evidence/AUTHZ-004.jpg)                                                                                                                                 |

### AUTH-002 — Unauthenticated Access to Employee Status Endpoint

| Field                              | Details                                                                                                                                                                                                           |
| ---------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Test ID                            | AUTH-002                                                                                                                                                                                                          |
| Endpoint                           | PUT /api/employees/1/status                                                                                                                                                                                       |
| User Role                          | Unauthenticated                                                                                                                                                                                                   |
| Pre-Remediation State / Before Fix | The application had no authentication. An unauthenticated user could send requests to the employee status endpoint without being required to log in. The endpoint was not protected by an authentication control. |
| Actual Result / After Fix          | 302 Found — the unauthenticated request was redirected to /login. The status update was not confirmed.                                                                                                            |
| Result                             | PASS                                                                                                                                                                                                              |
| Evidence                           | [AUTH-002.jpg](evidence/AUTH-002.jpg)                                                                                                                                                                                                      |

### 6\. Tests Not Yet Performed

The following security areas remain outside the completed assessment:

* Broken Object Level Authorization (BOLA / IDOR).

* Broken Function Level Authorization (BFLA) beyond the tested endpoints.

* Employee ownership validation for attendance records.

* CSRF protection testing.

* Session management and session expiration.

* Input validation and injection testing.

* API rate limiting.

* Biometric device authentication and communication security.

These areas must not be considered tested or passed.

### 7\. Important Assessment Limitations

* The current employee account is managed separately from employee records in the H2 database.

* The current security configuration restricts all `/api/attendance/**` endpoints to ADMIN.

* Employee-specific attendance access has not yet been implemented or tested.

* No physical fingerprint device has been integrated.

* The results documented here are based on observed HTTP responses reported during testing. Supporting screenshots must be retained as evidence.

### 8\. Conclusion

The post-remediation assessment confirmed that the tested authentication and role-based authorization rules produced the expected access-control responses.

However, this assessment does not establish that the entire application is secure.

Further testing is required, particularly for employee-specific attendance access, object ownership validation, API security, and biometric device integration.

Assessment Status: Remediated and Re-tested.

Project: OAS Lite Branch: `security/baseline-assessment`

