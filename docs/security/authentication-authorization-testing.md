OAS Lite – Authentication and Authorization Security Assessment

1. Document Information

Field| Details
Project| OAS Lite
Assessment Type| Authentication and Authorization Testing
Application| Employee Attendance Management System
Backend Framework| Spring Boot
Security Framework| Spring Security
Database| H2
Assessment Status| Initial Assessment
Git Branch| "security/baseline-assessment"
Commit| "71fb316"

2. Assessment Objective

The objective of this assessment is to evaluate the authentication and authorization controls implemented in OAS Lite.

The assessment focuses on verifying that:

- Unauthenticated users cannot access protected resources.
- Administrators can access administrative resources.
- Employees cannot access resources restricted to administrators.
- Access control rules are enforced by the server.

3. Application Roles

The application currently defines two roles:

Role| Description
ADMIN| Can access employee management and attendance management APIs.
EMPLOYEE| Has restricted access to administrative APIs.

4. Security Configuration Reviewed

The following authorization rules were reviewed in "SecurityConfig.java":

.requestMatchers("/api/employees/**").hasRole("ADMIN")
.requestMatchers("/api/attendance/**").hasRole("ADMIN")
.anyRequest().authenticated()

Password handling was also reviewed:

- Passwords are read from environment variables.
- BCrypt is used through "BCryptPasswordEncoder".
- User accounts are currently managed using "InMemoryUserDetailsManager".

5. Test Results

AUTH-001: Unauthenticated Access to Attendance API

Objective: Verify that an unauthenticated user cannot access attendance records.

Field| Result
Endpoint| "GET /api/attendance"
Authentication| No authenticated session
Actual HTTP Status| "302 Found"
Redirect| "/login"
Expected Behavior| Redirect unauthenticated user to login
Result| PASS (based on observed response)

![AUTH-001 Evidence]
(evidence/AUTH-001.jpg)

Observation:

After removing the session identifier from the request in Burp Suite Repeater, the server redirected the request to the login page.

No attendance records were disclosed in the observed response.

AUTHZ-001: Administrator Access to Attendance API

Objective: Verify that an administrator can access attendance records.

Field| Result
Endpoint| "GET /api/attendance"
Role| ADMIN
Actual HTTP Status| "200 OK"
Expected Behavior| Allow access
Result| PASS (based on observed response)

![AUTHZ-001 Evidence]
(evidence/AUTHZ-001.jpg)

Observation:

The administrator successfully retrieved attendance records in JSON format.

AUTHZ-002: Employee Access to Attendance API

Objective: Verify that an employee cannot access the administrative attendance API.

Field| Result
Endpoint| "GET /api/attendance"
Role| EMPLOYEE
Actual HTTP Status| "403 Forbidden"
Expected Behavior| Deny access
Result| PASS (based on observed response)

![AUTHZ-002 Evidence]
(evidence/AUTHZ-002.jpg)


Observation:

The server denied the employee's request. The attendance records were not returned.

AUTHZ-003: Employee Access to Employee Management API

Objective: Verify that an employee cannot access the employee management API.

Field| Result
Endpoint| "GET /api/employees"
Role| EMPLOYEE
Actual HTTP Status| "403 Forbidden"
Expected Behavior| Deny access
Result| PASS (based on observed response)

![AUTHZ-003 Evidence]
(evidence/AUTHZ-003.jpg)


Observation:

The server rejected the request from the employee account. The employee list was not returned.

AUTHZ-004: Administrator Access to Employee Management API

Objective: Verify that an administrator can access the employee management API.

Field| Result
Endpoint| "GET /api/employees"
Role| ADMIN
Actual HTTP Status| "200 OK"
Expected Behavior| Allow access
Result| PASS (based on observed response)

![AUTHZ-004 Evidence]
(evidence/AUTHZ-004.jpg)


Observation:

The administrator successfully retrieved the employee list.

AUTH-002: Unauthenticated Access to Employee Status Endpoint

Objective: Verify that an unauthenticated user cannot access the employee status update endpoint.

Field| Result
Endpoint| "PUT /api/employees/1/status"
Authentication| No authenticated session
Actual HTTP Status| "302 Found"
Redirect| "/login"
Expected Behavior| Deny unauthenticated access
Result| PASS (based on observed response)

![AUTH-002 Evidence]
(evidence/AUTH-002.jpg)


Observation:

After removing the session identifier, the request was redirected to the login page.

No successful status update was confirmed.

6. Tests Not Yet Performed

The following security areas remain outside the completed assessment:

- Broken Object Level Authorization (BOLA / IDOR).
- Broken Function Level Authorization (BFLA) beyond the tested endpoints.
- Employee ownership validation for attendance records.
- CSRF protection testing.
- Session management and session expiration.
- Input validation and injection testing.
- API rate limiting.
- Biometric device authentication and communication security.

These areas must not be considered tested or passed.

7. Important Assessment Limitations

1. The current employee account is managed separately from employee records in the H2 database.
2. The current security configuration restricts all "/api/attendance/**" endpoints to ADMIN.
3. Employee-specific attendance access has not yet been implemented or tested.
4. No physical fingerprint device has been integrated.
5. The results documented here are based on observed HTTP responses reported during testing. Supporting screenshots must be retained as evidence.

8. Preliminary Conclusion

The initial assessment confirmed that the tested authentication and role-based authorization rules produced the expected access-control responses.

However, this assessment does not establish that the entire application is secure.

Further testing is required, particularly for employee-specific attendance access, object ownership validation, API security, and biometric device integration.

Assessment Status: Initial Authentication and Authorization Testing Completed.

Project: OAS Lite
Branch: "security/baseline-assessment"
