package com.kiet.student.repository;

import com.kiet.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Find student by email
    Optional<Student> findByEmail(String email);

    // Find students by course name (case-insensitive)
    List<Student> findByCourseNameIgnoreCase(String courseName);
}

