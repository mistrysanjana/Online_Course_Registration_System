# Online Course Registration System (OCRS)

A complete, full-stack, production-ready Java Web application developed using **Java EE (JSP & Servlets)**, **JDBC**, and **MySQL**, adhering strictly to the **Model-View-Controller (MVC)** architectural design pattern.

---

## Table of Contents

1. [Project Overview] (#-project-overview)
2. [Key Features] (##-key-features)
   - [Student Features](### student-features)
   - [Admin Features](### admin-features)
   - [Core Business Rules](#core-business-rules)
3. [Technology Stack](#-technology-stack)
4. [Architecture & Design] (#-architecture--design)
5. [Project Directory Structure] (#-project-directory-structure)
6. [Prerequisites & System Requirements]
(#-prerequisites--system-requirements)
7. [Database Setup Guide](#-database-setup-guide)
8. [Configuration] (#-configuration)
9. [Running the Application] (#-running-the-application)
   - [Option A: Eclipse IDE (Enterprise Java / JEE)](#option-a-eclipse-ide-enterprise-java--jee)
   - [Option B: IntelliJ IDEA Ultimate](#option-b-intellij-idea-ultimate)
   - [Option C: Apache NetBeans](#option-c-apache-netbeans)
   - [Option D: Command Line / Maven CLI](#option-d-command-line--maven-cli)
10. [Default Login Credentials] (#-default-login-credentials)
11. [End-to-End Test Plan & Verification] (#-end-to-end-test-plan--verification)
12. [Common Errors & Troubleshooting] (#-common-errors--troubleshooting)
13. [GitHub Upload Instructions] (#-github-upload-instructions)
14. [Future Enhancements] (#-future-enhancements)
15. [Viva Voce Questions & Answers] (#-viva-voce-questions--answers)

---

## Project Overview

The **Online Course Registration System** is an enterprise-grade academic portal designed to automate student course enrollment, eliminate paper forms, manage class quotas in real-time, and provide university administrators with tools to oversee syllabus offerings, monitor quotas, and approve/reject registrations.

The application follows the **MVC (Model-View-Controller)** pattern:

- **Model**: Encapsulates data transfer objects (`Student`, `Course`, `Registration`, `Admin`).
- **View**: JSP pages utilizing HTML5, responsive CSS3, and JavaScript for client validation.
- **Controller**: Java Servlets handling HTTP requests, enforcing session-based authorization, and invoking the DAO layer.
- **DAO (Data Access Object)**: JDBC queries using `PreparedStatement` to prevent SQL injection and transaction handling for atomic seat reservations.

---

## Key Features

### Student Features

**Account Registration**: Secure sign-up with validations (unique Student ID, unique Email, password constraints, department, and academic year).

- **Authentication & Session Control**: Role-based session management (`/student/*`).
- **Student Dashboard**: Live summary metrics showing total enrolled courses, pending requests, approved courses, and available catalog count.
- **Course Catalog (`courses.jsp`)**:
  - Full listing of all university courses.
  - Search by course code, title, or instructor name.
  - Dropdown filters by academic Department and Semester.
  - Live seat indicator (`Available / Max Seats`).
  - Instant feedback badge: `Course Full` or `Registered`.
- **Course Details (`course-details.jsp`)**: Comprehensive view of course syllabus, instructor, credits, and seat capacity.
- **One-Click Course Registration**:
  - Prevents duplicate registrations.
  - Checks available seat count before booking.
  - Decrements available seats atomically inside a database transaction.
  - Sets status to `PENDING`.
- **My Courses (`my-courses.jsp`)**: List of all enrolled courses with the ability to **cancel** a pending registration (which automatically releases the reserved seat by +1).
- **Registration Status (`registration-status.jsp`)**: Color-coded badges for workflow stages (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`).
- **Secure Logout**: Clears session and destroys cookies safely.

### Admin Features

**Dedicated Admin Portal**: Separate login portal (`/admin/login`).

- **Admin Dashboard (`admin-dashboard.jsp`)**: Six core metric counters:
  1. Total Students
  2. Total Courses
  3. Available Courses
  4. Total Registrations
  5. Pending Registrations
  6. Approved Registrations
- **Course CRUD Management**:
  - **Add Course**: Form with validation for credits, max capacity, instructor, department, semester.
  - **Edit Course**: Modify existing course attributes, seat capacity, or active status.
  - **Delete Course**: Safely removes course with confirmation prompt.
- **Registration Management (`admin-registrations.jsp`)**:
  - Review all student submissions.
  - **Approve** registration (changes status to `APPROVED`).
  - **Reject** registration (changes status to `REJECTED` and restores +1 seat to the course quota).
  - Revert/Re-approve controls.

### Core Business Rules

1. **Duplicate Prevention**: A student cannot submit a duplicate registration for a course they are already actively enrolled or pending in.
2. **Quota Enforcement**: A student cannot register for any course with `available_seats = 0`.
3. **Atomic Transactions**: Seat decrement and registration creation happen inside a single JDBC transaction (`conn.setAutoCommit(false)`), preventing race conditions.
4. **Seat Restoration**: When a student cancels or an admin rejects a registration, the seat is returned (`available_seats + 1`).
5. **Access Control**: Role-based `AuthenticationFilter` prevents students from accessing admin URLs and unauthenticated guests from accessing student dashboards.

---

## 🛠 Technology Stack

| Layer | Technologies Used |
| :--- | :--- |
| **Backend Language** | Java (JDK 8 / 11 / 17 / 21+) |
| **Web Framework** | Java Servlets (Servlet API 4.0), JavaServer Pages (JSP 2.3), JSTL 1.2 |
| **Database** | MySQL Server 8.0+ |
| **Data Access** | JDBC (Java Database Connectivity) via MySQL Connector/J 8.3.0 |
| **Frontend** | HTML5, CSS3 (Custom Responsive Flexbox/Grid), Vanilla JavaScript (ES6) |
| **Application Server** | Apache Tomcat 9.0 (or Tomcat 8.5/10 with Jakarta) |
| **Build Tool** | Apache Maven 3.8+ |

---

## Architecture & Design

```text
[Client Web Browser]
        │
        ▼ (HTTP Requests: GET / POST)
[AuthenticationFilter] ──(Session & Role Validation)
        │
        ▼
[Java Servlets (Controllers)]
   ├── LoginServlet / LogoutServlet
   ├── StudentRegisterServlet / StudentDashboardServlet
   ├── CourseServlet / CourseDetailsServlet
   ├── RegisterCourseServlet / CancelRegistrationServlet / RegistrationStatusServlet
   └── AdminLoginServlet / AdminDashboardServlet / AddCourseServlet / EditCourseServlet / DeleteCourseServlet / UpdateRegistrationServlet
        │
        ▼
[DAO Layer (Data Access Objects)]
   ├── StudentDAO
   ├── CourseDAO
   ├── RegistrationDAO (with Transaction Management)
   └── AdminDAO
        │
        ▼
[DBConnection Utility] ──(MySQL JDBC Driver)
        │
        ▼
[MySQL Database: course_registration_db]
   ├── students
   ├── admins
   ├── courses
   └── registrations
```

---

## Project Directory Structure

```text
OnlineCourseRegistrationSystem/
│
├── database/
│   └── course_registration_db.sql            # Complete DB schema, constraints & sample data
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── courseregistration/
│       │           ├── controller/           # Servlet Controllers & Security Filters
│       │           │   ├── AddCourseServlet.java
│       │           │   ├── AdminCoursesServlet.java
│       │           │   ├── AdminDashboardServlet.java
│       │           │   ├── AdminLoginServlet.java
│       │           │   ├── AdminRegistrationsServlet.java
│       │           │   ├── AuthenticationFilter.java
│       │           │   ├── CancelRegistrationServlet.java
│       │           │   ├── CourseDetailsServlet.java
│       │           │   ├── CourseServlet.java
│       │           │   ├── DeleteCourseServlet.java
│       │           │   ├── EditCourseServlet.java
│       │           │   ├── LoginServlet.java
│       │           │   ├── LogoutServlet.java
│       │           │   ├── MyCoursesServlet.java
│       │           │   ├── RegisterCourseServlet.java
│       │           │   ├── RegistrationStatusServlet.java
│       │           │   ├── StudentDashboardServlet.java
│       │           │   ├── StudentRegisterServlet.java
│       │           │   └── UpdateRegistrationServlet.java
│       │           │
│       │           ├── dao/                  # Data Access Objects (JDBC queries)
│       │           │   ├── AdminDAO.java
│       │           │   ├── CourseDAO.java
│       │           │   ├── RegistrationDAO.java
│       │           │   └── StudentDAO.java
│       │           │
│       │           ├── model/                # Entity Data Models (POJOs)
│       │           │   ├── Admin.java
│       │           │   ├── Course.java
│       │           │   ├── Registration.java
│       │           │   └── Student.java
│       │           │
│       │           └── util/                 # Utility Classes
│       │               ├── DBConnection.java
│       │               └── PasswordUtil.java
│       │
│       └── webapp/                           # Presentation Layer (JSP, CSS, JS)
│           ├── css/
│           │   └── style.css                 # Responsive stylesheet
│           ├── js/
│           │   └── script.js                 # Validation and dynamic UI behavior
│           ├── WEB-INF/
│           │   ├── includes/
│           │   │   ├── footer.jsp            # Shared footer component
│           │   │   └── navbar.jsp            # Role-aware responsive navigation bar
│           │   └── web.xml                   # Deployment descriptor & servlet mappings
│           │
│           ├── add-course.jsp                # Admin add course page
│           ├── admin-courses.jsp             # Admin course management table
│           ├── admin-dashboard.jsp           # Admin metrics & statistics dashboard
│           ├── admin-login.jsp               # Admin login portal
│           ├── admin-registrations.jsp       # Admin approval/rejection interface
│           ├── course-details.jsp            # Detailed syllabus & enrollment view
│           ├── courses.jsp                   # Course catalog with search and filters
│           ├── edit-course.jsp               # Admin edit course page
│           ├── index.jsp                     # Public landing home page
│           ├── login.jsp                     # Student login page
│           ├── my-courses.jsp                # Student enrolled courses & cancellation
│           ├── registration-status.jsp       # Visual status breakdown page
│           └── student-dashboard.jsp         # Student dashboard
│
├── .gitignore
├── pom.xml                                   # Maven dependency & WAR build configuration
└── README.md                                 # Comprehensive project documentation
```

---

## Prerequisites & System Requirements

1. **Java Development Kit (JDK)**: JDK 8, 11, 17, or 21+ installed and configured in `JAVA_HOME`.
2. **Apache Tomcat**: Version 9.0.x recommended (or Tomcat 8.5).
3. **MySQL Server**: Version 8.0 or MySQL 5.7+ (or XAMPP / WampServer MySQL).
4. **IDE**: Eclipse IDE for Enterprise Java Developers, IntelliJ IDEA Ultimate, or NetBeans.
5. **Apache Maven**: (Optional if building via IDE) 3.8+.

---

## 🗄 Database Setup Guide

### 1. Open MySQL Command Line or MySQL Workbench / phpMyAdmin

Log into your MySQL instance:

```bash
mysql -u root -p
```

**(Enter your MySQL root password when prompted)*

### 2. Execute the Database Script

Run the provided SQL script:

```sql
SOURCE C:/path/to/OnlineCourseRegistrationSystem/database/course_registration_db.sql;
```

*Or open `database/course_registration_db.sql` in MySQL Workbench or phpMyAdmin and execute all queries.*

### 3. Verify Database Creation

Verify that all 4 tables and sample records were created:

```sql
USE course_registration_db;
SHOW TABLES;
SELECT COUNT(*) FROM courses;
SELECT COUNT(*) FROM students;
SELECT COUNT(*) FROM admins;
```

---

## Configuration

Open `src/main/java/com/courseregistration/util/DBConnection.java` to inspect or edit your database credentials:

```java
private static final String DEFAULT_URL = 
    "jdbc:mysql://localhost:3306/course_registration_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
private static final String DEFAULT_USER = "root";
private static final String DEFAULT_PASSWORD = "root"; // <-- Update with your local MySQL password
```

> **Tip:** If your local MySQL password is empty (common in XAMPP defaults), change `DEFAULT_PASSWORD` to `""`.

---

## Running the Application

### Option A: Eclipse IDE (Enterprise Java / JEE)

1. Open Eclipse and select **File > Import...**.
2. Select **Existing Maven Projects** (or **Existing Projects into Workspace**).
3. Browse to the `OnlineCourseRegistrationSystem` directory and click **Finish**.
4. Configure Apache Tomcat:
   - Go to the **Servers** tab at the bottom.
   - Right-click > **New > Server > Apache > Tomcat v9.0 Server**.
   - Browse to your local Tomcat installation directory.
5. Right-click on the `OnlineCourseRegistrationSystem` project > **Run As > Run on Server**.
6. Select your Tomcat 9 server and click **Finish**.
7. Access in your browser:
   `http://localhost:8080/OnlineCourseRegistrationSystem/`

---

### Option B: IntelliJ IDEA Ultimate

1. Open IntelliJ and select **Open**. Browse to the project folder.
2. IntelliJ will recognize `pom.xml` and download the dependencies.
3. Click **Run > Edit Configurations...**.
4. Click `+` and choose **Tomcat Server > Local**.
5. In the **Deployment** tab, click `+` and select **Artifact > OnlineCourseRegistrationSystem:war exploded**.
6. Set the Application Context to `/OnlineCourseRegistrationSystem` (or `/`).
7. Click **Apply** and **Run**.
8. Browser will automatically open `http://localhost:8080/OnlineCourseRegistrationSystem/`.

---

### Option C: Apache NetBeans

1. Open NetBeans > **File > Open Project**.
2. Select the `OnlineCourseRegistrationSystem` folder (indicated by a Maven icon).
3. Right-click the project > **Properties > Run**.
4. Select **Apache Tomcat** as the server.
5. Right-click the project > **Run**.

---

### Option D: Command Line / Maven CLI

1. Open terminal inside the project directory:

   ```bash
   mvn clean package
   ```

2. Maven will compile the Java classes and generate `target/OnlineCourseRegistrationSystem.war`.
3. Copy `OnlineCourseRegistrationSystem.war` into your Tomcat `webapps/` directory:

   ```bash
   cp target/OnlineCourseRegistrationSystem.war /path/to/tomcat/webapps/
   ```

4. Start Tomcat:

   ```bash
   # On Windows:
   bin\startup.bat

   # On Linux/macOS:
   bin/startup.sh
   ```

5. Open browser at `http://localhost:8080/OnlineCourseRegistrationSystem/`.

---

## Default Login Credentials

### 🛡️ Administrator Account

**URL:** `http://localhost:8080/OnlineCourseRegistrationSystem/admin-login.jsp`

- **Username:** `admin`

- **Password:** `admin123`

### Sample Student Accounts

**URL:** `http://localhost:8080/OnlineCourseRegistrationSystem/login.jsp`

| Student ID | Full Name | Email Address | Password | Department | Year |
| :--- | :--- | :--- | :--- | :--- | :---: |
| `STU1001` | Rahul Sharma | `rahul.sharma@example.com` | `student123` | Computer Science | 3 |
| `STU1002` | Priya Patel | `priya.patel@example.com` | `student123` | Information Technology | 2 |
| `STU1003` | Aman Verma | `aman.verma@example.com` | `student123` | Computer Science | 4 |
| `STU1004` | Sneha Reddy | `sneha.reddy@example.com` | `student123` | Electronics & Comm. | 2 |

---

## End-to-End Test Plan & Verification

| Test Case | Steps to Test | Expected Outcome | Result |
| :--- | :--- | :--- | :---: |
| **TC-01: Home Page Loading** | Visit `/` | Landing page opens with navigation links and action buttons. | PASS |
| **TC-02: Student Registration** | Fill `student-register.jsp` with new ID, Email, and Password. Submit. | Account created, redirected to `login.jsp` with green success banner. | PASS |
| **TC-03: Duplicate Email Check** | Attempt to register with `rahul.sharma@example.com`. | Registration halted with message: *"An account with email already exists."* | PASS |
| **TC-04: Student Login** | Log in with `rahul.sharma@example.com` / `student123`. | Session established; redirected to `student-dashboard.jsp`. | PASS |
| **TC-05: Course Catalog Filters** | In `courses.jsp`, filter by Department: *"Information Technology"*. | Only courses belonging to Information Technology appear. | PASS |
| **TC-06: Course Registration** | Click **Register** on an available course (e.g., CS301). | Redirects to `my-courses.jsp` with success message. Available seats decrease by 1. | PASS |
| **TC-07: Duplicate Registration** | Try registering for the same course again. | System displays error: *"You are already registered for this course."* | PASS |
| **TC-08: Course Full Condition** | Check course `CS303` (Artificial Intelligence, 0 seats remaining). | Register button is disabled; displays `Course Full` badge. | PASS |
| **TC-09: Cancel Registration** | In `my-courses.jsp`, click **Cancel** on a PENDING course. | Status updates to `CANCELLED`; available seats in course increment by 1. | PASS |
| **TC-10: Admin Authentication** | Visit `/admin-login.jsp` and log in with `admin` / `admin123`. | Admin dashboard loads with 6 metric cards and recent requests. | PASS |
| **TC-11: Admin Add Course** | Go to `/admin/add-course`, fill in new course parameters, submit. | Course appears in catalog immediately with configured seats. | PASS |
| **TC-12: Admin Edit Course** | In `/admin/courses`, click Edit on any course, alter credits or seats. | Course updates successfully in database. | PASS |
| **TC-13: Admin Registration Review** | In `/admin/registrations`, click **Approve** or **Reject** on a pending item. | Status updates; upon rejection, reserved seat is automatically released. | PASS |
| **TC-14: Security URL Guard** | While logged out, attempt to access `/student/dashboard` or `/admin/dashboard`. | Automatically redirected to respective login page. | PASS |
| **TC-15: Secure Logout** | Click **Logout**. | Session is invalidated; trying browser back button blocks protected views. | PASS |

---

## Common Errors & Troubleshooting

### 1. `CommunicationsException: Communications link failure`

**Cause**: MySQL server is stopped or not running on port 3306.

- **Fix**: Start MySQL via Windows Services (`services.msc`), XAMPP Control Panel, or command `net start mysql`.

### 2. `Access denied for user 'root'@'localhost'`

**Cause**: Password in `DBConnection.java` does not match your MySQL root password.

- **Fix**: Update `DEFAULT_PASSWORD` in `DBConnection.java` and recompile.

### 3. `ClassNotFoundException: com.mysql.cj.jdbc.Driver`

**Cause**: MySQL Connector JAR is missing from Tomcat's runtime classpath.

- **Fix**: When using Tomcat in Eclipse or manual deployment, copy `mysql-connector-j-8.3.0.jar` into your Tomcat `lib/` directory (`apache-tomcat-9.0.x/lib/`).

### 4. `HTTP 404 - Not Found`

**Cause**: Mismatched context path or missing WAR deployment.

- **Fix**: Ensure you access `http://localhost:8080/OnlineCourseRegistrationSystem/` (case-sensitive) or set Application Context to `/` in Tomcat server settings.

### 5. `Table 'course_registration_db.students' doesn't exist`

**Cause**: The SQL script was not imported before running the application.

- **Fix**: Run `database/course_registration_db.sql` in MySQL Workbench or CLI.

---

## GitHub Upload Instructions

Run the following commands in the project directory:

```bash
# 1. Initialize Git repository
git init

# 2. Add all project files
git add .

# 3. Commit files
git commit -m "Initial commit: Online Course Registration System complete MVC implementation"

# 4. Link to your remote GitHub repository
git branch -M main
git remote add origin https://github.com/your-username/OnlineCourseRegistrationSystem.git

# 5. Push to GitHub
git push -u origin main
```

---

## Future Enhancements

- **Email Notifications**: Integration with JavaMail API to send instant confirmation emails upon registration and admin approval.
- **Prerequisite Enforcement**: System checks whether a student has completed prerequisite courses (e.g., CS101 before CS102) before permitting registration.
- **Timetable Clash Detection**: Conflict checker to warn students if two selected courses share overlapping lecture slots.
- **PDF Fee Receipt & Admit Card Generation**: Integration with iText / Apache PDFBox for downloadable registration acknowledgments.
- **Password Reset Workflow**: Secure token-based password reset via email.

---

## Viva Voce Questions & Answers

**Q1: What is the architectural pattern used in this project?**  
> **A:** This project uses the **Model-View-Controller (MVC)** architectural pattern. Models represent data entities (`Student`, `Course`, `Registration`), Views are implemented using JSP for presentation, and Controllers are Java Servlets that handle HTTP requests and enforce business logic.

**Q2: How does the system prevent duplicate course registration?**  
> **A:** Duplicate prevention is implemented at two distinct levels:
>
> 1. In `RegistrationDAO.java`, a transactional query checks whether a record already exists with status `PENDING` or `APPROVED`.
> 2. At the database level, a `UNIQUE KEY uq_student_course (student_id, course_id)` constraint guarantees uniqueness.

**Q3: How are race conditions avoided during concurrent seat registration?**  
> **A:** In `RegistrationDAO.java`, registrations are executed within a database transaction using `conn.setAutoCommit(false)`. A `SELECT ... FOR UPDATE` clause locks the target course row, and the seat decrement query contains an atomic guard: `WHERE course_id = ? AND available_seats > 0`. If another thread claims the last seat simultaneously, the transaction rolls back safely.

**Q4: Why use `PreparedStatement` instead of `Statement`?**  
> **A:** `PreparedStatement` pre-compiles SQL templates and treats parameters strictly as data values rather than executable code, eliminating **SQL Injection** attacks. It also improves execution speed for repeated queries.

**Q5: How is session security handled?**  
> **A:** The system uses standard `HttpSession` management. The `AuthenticationFilter` intercepts incoming requests for protected URI patterns (`/student/*` and `/admin/*`) and verifies session attributes. If invalid, the user is redirected to the corresponding login page.

---
*Developed for College Academic Evaluation &amp; Software Engineering Lab Project Submission.*
