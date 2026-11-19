# Student Profile and Skill Development Management System

A Spring Boot 3.3.5 Maven application (Java 17) for managing student profiles, academic scores, and technical skills. The system applies business logic using Java Collections (Lists, Maps, Streams), persists data in MySQL using Spring Data JPA, and exposes standard RESTful endpoints.

---

## 🛠️ Technology Stack & Dependencies

- **Java Version:** 17
- **Framework:** Spring Boot 3.3.5
- **Build Tool:** Maven
- **Database:** MySQL 8.x
- **Dependencies (in `pom.xml`):**
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `mysql-connector-j`
  *(No Lombok used)*

---

## 📁 Project Structure

```
Skill-Development-Management-System/
├── pom.xml
├── schema.sql
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── kiet/
        │           └── student/
        │               ├── StudentApplication.java
        │               ├── controller/
        │               │   └── StudentController.java
        │               ├── exception/
        │               │   ├── DuplicateStudentIdException.java
        │               │   ├── ErrorResponse.java
        │               │   ├── GlobalExceptionHandler.java
        │               │   ├── InvalidScoreException.java
        │               │   └── StudentNotFoundException.java
        │               ├── model/
        │               │   └── Student.java
        │               ├── repository/
        │               │   └── StudentRepository.java
        │               └── service/
        │                   └── StudentService.java
        └── resources/
            └── application.properties
```

---

## 🗄️ Database Configuration & Setup

### 1. `application.properties`
Located at `src/main/resources/application.properties`:
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/student_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
> **Note:** Update `YOUR_PASSWORD` with your local MySQL password.

### 2. `schema.sql`
You can execute `schema.sql` directly in MySQL Workbench or via MySQL CLI to initialize the database and load the 6 sample students and their skills:
```bash
mysql -u root -p < schema.sql
```

---

## ☕ How to Import and Run in Eclipse

1. Open **Eclipse IDE**.
2. Select **File** > **Import...**
3. Choose **Maven** > **Existing Maven Projects** and click **Next**.
4. Browse to the workspace root directory:
   `c:\Users\Hp\Skill Development Management\Skill-Development-Management-System`
5. Ensure `pom.xml` is checked and click **Finish**.
6. Wait for Eclipse to download Maven dependencies and build the project.
7. Open `src/main/resources/application.properties` and ensure `spring.datasource.password` matches your local MySQL password.
8. Right-click on `StudentApplication.java` > **Run As** > **Spring Boot App** (or **Java Application**).
9. The server starts on port `8080`.

---

## 📋 Business Logic & Java Collections Usage

1. **Average Score:** Calculated dynamically as `(programmingScore + mathematicsScore) / 2.0`.
2. **Performance Category:**
   - `>= 85.0`: `"Excellent"`
   - `70.0` to `84.99`: `"Good"`
   - `50.0` to `69.99`: `"Satisfactory"`
   - `< 50.0`: `"Needs Improvement"`
3. **Stronger Subject:** Returns `"Programming"`, `"Mathematics"`, or `"Equal"`.
4. **Search by ID, Email, or Course:** Filtered using Java Stream API on `List<Student>`.
5. **Merit List:** Sorted in descending order of average score using `Comparator.comparingDouble(Student::getAverageScore).reversed()`.
6. **Shared Technical Skills:** Grouped in a `Map<String, List<Student>>` using a case-insensitive `TreeMap<>(String.CASE_INSENSITIVE_ORDER)`, filtered using streams to return only skills shared by 2 or more students.
7. **Students Needing Improvement:** Filters students with `programmingScore < 50` and `mathematicsScore >= 75` using Stream API.
8. **Validation & Exceptions:**
   - Duplicate `studentId` returns HTTP `409 Conflict`.
   - Scores outside `[0, 100]` return HTTP `400 Bad Request`.
   - Non-existent ID returns HTTP `404 Not Found`.
   - All errors formatted uniformly as JSON: `{"message": "..."}` via `@RestControllerAdvice`.

---

## 🚀 REST API Endpoints & Request / Response Samples

Base URL: `http://localhost:8080/students`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/students` | Add a new student |
| `GET` | `/students` | Get all students |
| `GET` | `/students/{id}` | Get student by ID (numeric) |
| `PUT` | `/students/{id}` | Update scores & skills |
| `DELETE` | `/students/{id}` | Delete student by ID |
| `GET` | `/students/merit-list` | Get students sorted by average descending |
| `GET` | `/students/search?id=&email=&course=` | Search students with optional parameters |
| `GET` | `/students/same-skills` | Group students sharing skills (>= 2 students) |
| `GET` | `/students/needs-improvement` | Students with prog < 50 and math >= 75 |

---

### 1. Sample JSON Body for `POST /students`

**Endpoint:** `POST http://localhost:8080/students`  
**Headers:** `Content-Type: application/json`

```json
{
  "studentId": 107,
  "name": "Rahul Verma",
  "email": "rahul.verma@kiet.edu",
  "courseName": "Computer Science",
  "programmingScore": 88.5,
  "mathematicsScore": 91.0,
  "technicalSkills": [
    "Java",
    "Spring Boot",
    "Docker",
    "Git"
  ]
}
```

**Success Response (`201 Created`):**
```json
{
  "studentId": 107,
  "name": "Rahul Verma",
  "email": "rahul.verma@kiet.edu",
  "courseName": "Computer Science",
  "programmingScore": 88.5,
  "mathematicsScore": 91.0,
  "technicalSkills": [
    "Java",
    "Spring Boot",
    "Docker",
    "Git"
  ],
  "averageScore": 89.75,
  "performanceCategory": "Excellent",
  "strongerSubject": "Mathematics"
}
```

---

### 2. Sample JSON Body for `PUT /students/{id}`

**Endpoint:** `PUT http://localhost:8080/students/107`  
**Headers:** `Content-Type: application/json`

```json
{
  "programmingScore": 95.0,
  "mathematicsScore": 93.5,
  "technicalSkills": [
    "Java",
    "Spring Boot",
    "Docker",
    "Kubernetes",
    "AWS"
  ]
}
```

**Success Response (`200 OK`):**
```json
{
  "studentId": 107,
  "name": "Rahul Verma",
  "email": "rahul.verma@kiet.edu",
  "courseName": "Computer Science",
  "programmingScore": 95.0,
  "mathematicsScore": 93.5,
  "technicalSkills": [
    "Java",
    "Spring Boot",
    "Docker",
    "Kubernetes",
    "AWS"
  ],
  "averageScore": 94.25,
  "performanceCategory": "Excellent",
  "strongerSubject": "Programming"
}
```

---

### 3. Sample Response for `DELETE /students/{id}`

**Endpoint:** `DELETE http://localhost:8080/students/107`

**Response (`200 OK`):**
```json
{
  "message": "Student deleted successfully"
}
```

---

### 4. Sample Response for `GET /students/same-skills`

**Endpoint:** `GET http://localhost:8080/students/same-skills`

**Response (`200 OK`):**
```json
{
  "Docker": [
    {
      "studentId": 102,
      "name": "Bhavna Patel",
      "email": "bhavna.patel@kiet.edu",
      "courseName": "Information Technology",
      "programmingScore": 45.0,
      "mathematicsScore": 82.0,
      "technicalSkills": ["Python", "Java", "Docker"],
      "averageScore": 63.5,
      "performanceCategory": "Satisfactory",
      "strongerSubject": "Mathematics"
    },
    {
      "studentId": 104,
      "name": "Divya Nair",
      "email": "divya.nair@kiet.edu",
      "courseName": "Data Science",
      "programmingScore": 85.0,
      "mathematicsScore": 85.0,
      "technicalSkills": ["Spring Boot", "AWS", "Docker"],
      "averageScore": 85.0,
      "performanceCategory": "Excellent",
      "strongerSubject": "Equal"
    }
  ],
  "Java": [
    {
      "studentId": 101,
      "name": "Aarav Sharma",
      "email": "aarav.sharma@kiet.edu",
      "courseName": "Computer Science",
      "programmingScore": 92.0,
      "mathematicsScore": 88.0,
      "technicalSkills": ["Java", "Spring Boot", "MySQL"],
      "averageScore": 90.0,
      "performanceCategory": "Excellent",
      "strongerSubject": "Programming"
    },
    {
      "studentId": 102,
      "name": "Bhavna Patel",
      "email": "bhavna.patel@kiet.edu",
      "courseName": "Information Technology",
      "programmingScore": 45.0,
      "mathematicsScore": 82.0,
      "technicalSkills": ["Python", "Java", "Docker"],
      "averageScore": 63.5,
      "performanceCategory": "Satisfactory",
      "strongerSubject": "Mathematics"
    },
    {
      "studentId": 103,
      "name": "Chirag Verma",
      "email": "chirag.verma@kiet.edu",
      "courseName": "Computer Science",
      "programmingScore": 74.0,
      "mathematicsScore": 78.0,
      "technicalSkills": ["Java", "Python", "React"],
      "averageScore": 76.0,
      "performanceCategory": "Good",
      "strongerSubject": "Mathematics"
    },
    {
      "studentId": 106,
      "name": "Fatima Khan",
      "email": "fatima.khan@kiet.edu",
      "courseName": "Information Technology",
      "programmingScore": 68.0,
      "mathematicsScore": 62.0,
      "technicalSkills": ["Java", "SQL", "Spring Boot"],
      "averageScore": 65.0,
      "performanceCategory": "Satisfactory",
      "strongerSubject": "Programming"
    }
  ]
}
```

---

### 5. Sample Error Responses (`{"message": "..."}`)

- **Student Not Found (`404 Not Found`):**
  ```json
  {
    "message": "Student not found"
  }
  ```

- **Duplicate Student ID (`409 Conflict`):**
  ```json
  {
    "message": "Student with ID 101 already exists."
  }
  ```

- **Invalid Score (`400 Bad Request`):**
  ```json
  {
    "message": "Programming score must be between 0 and 100. Given: 110.0"
  }
  ```
