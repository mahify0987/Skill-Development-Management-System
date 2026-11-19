package com.kiet.student.controller;

import com.kiet.student.model.Student;
import com.kiet.student.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing student management and analytics endpoints.
 * Base path: /students
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * POST /students : Add a new student profile.
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student createdStudent = studentService.addStudent(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    /**
     * GET /students : Retrieve all student profiles.
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    /**
     * GET /students/merit-list : Retrieve all students sorted by average score descending.
     * Defined before /{id} to ensure literal path resolution.
     */
    @GetMapping("/merit-list")
    public ResponseEntity<List<Student>> getMeritList() {
        return ResponseEntity.ok(studentService.getMeritList());
    }

    /**
     * GET /students/search?id=&email=&course= : Search students by optional criteria.
     * Defined before /{id} to ensure literal path resolution.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "course", required = false) String course) {
        return ResponseEntity.ok(studentService.searchStudents(id, email, course));
    }

    /**
     * GET /students/same-skills : Group students sharing technical skills (shared by >= 2 students).
     * Defined before /{id} to ensure literal path resolution.
     */
    @GetMapping("/same-skills")
    public ResponseEntity<Map<String, List<Student>>> getStudentsSharingSkills() {
        return ResponseEntity.ok(studentService.getStudentsSharingSkills());
    }

    /**
     * GET /students/needs-improvement : Retrieve students with programmingScore < 50 and mathematicsScore >= 75.
     * Defined before /{id} to ensure literal path resolution.
     */
    @GetMapping("/needs-improvement")
    public ResponseEntity<List<Student>> getStudentsNeedingImprovement() {
        return ResponseEntity.ok(studentService.getStudentsNeedingImprovement());
    }

    /**
     * GET /students/{id} : Retrieve a student by ID.
     * Uses regex constraint {id:[0-9]+} to prevent conflict with literal paths.
     */
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") int id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    /**
     * PUT /students/{id} : Update scores and skills of an existing student.
     * Uses regex constraint {id:[0-9]+} to prevent conflict with literal paths.
     */
    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") int id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    /**
     * DELETE /students/{id} : Delete a student by ID.
     * Uses regex constraint {id:[0-9]+} to prevent conflict with literal paths.
     */
    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Student deleted successfully"));
    }
}

