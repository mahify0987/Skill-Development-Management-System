package com.kiet.student.service;

import com.kiet.student.exception.DuplicateStudentIdException;
import com.kiet.student.exception.InvalidScoreException;
import com.kiet.student.exception.StudentNotFoundException;
import com.kiet.student.model.Student;
import com.kiet.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Service class handling all business logic and collection manipulations for Students.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Validates that scores fall within the permitted range of 0 to 100.
     * Throws InvalidScoreException (HTTP 400) if invalid.
     */
    public void validateScores(double programmingScore, double mathematicsScore) {
        if (programmingScore < 0.0 || programmingScore > 100.0) {
            throw new InvalidScoreException("Programming score must be between 0 and 100. Given: " + programmingScore);
        }
        if (mathematicsScore < 0.0 || mathematicsScore > 100.0) {
            throw new InvalidScoreException("Mathematics score must be between 0 and 100. Given: " + mathematicsScore);
        }
    }

    /**
     * Add a new student record.
     * Rejects duplicate studentId with DuplicateStudentIdException (HTTP 409).
     * Rejects scores outside 0 to 100 with InvalidScoreException (HTTP 400).
     */
    public Student addStudent(Student student) {
        if (studentRepository.existsById(student.getStudentId())) {
            throw new DuplicateStudentIdException("Student with ID " + student.getStudentId() + " already exists.");
        }
        validateScores(student.getProgrammingScore(), student.getMathematicsScore());
        return studentRepository.save(student);
    }

    /**
     * Retrieve all students.
     * Collection use: List<Student> is used to return the ordered sequence of all student entities from the database.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Retrieve a student by ID.
     * Throws StudentNotFoundException (HTTP 404) if not found.
     */
    public Student getStudentById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    /**
     * Update scores and skills of an existing student.
     * Validates scores are between 0 and 100.
     * Collection use: List<String> technicalSkills is updated with the new skills list.
     */
    public Student updateStudent(int id, Student updatedData) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        validateScores(updatedData.getProgrammingScore(), updatedData.getMathematicsScore());

        existing.setProgrammingScore(updatedData.getProgrammingScore());
        existing.setMathematicsScore(updatedData.getMathematicsScore());

        if (updatedData.getTechnicalSkills() != null) {
            existing.setTechnicalSkills(updatedData.getTechnicalSkills());
        }
        if (updatedData.getName() != null && !updatedData.getName().isBlank()) {
            existing.setName(updatedData.getName());
        }
        if (updatedData.getEmail() != null && !updatedData.getEmail().isBlank()) {
            existing.setEmail(updatedData.getEmail());
        }
        if (updatedData.getCourseName() != null && !updatedData.getCourseName().isBlank()) {
            existing.setCourseName(updatedData.getCourseName());
        }

        return studentRepository.save(existing);
    }

    /**
     * Delete a student by ID.
     * Throws StudentNotFoundException (HTTP 404) if not found.
     */
    public void deleteStudent(int id) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        studentRepository.delete(existing);
    }

    /**
     * Q2.4: Search students by optional ID, email, or course name.
     * Collection use: Java Stream API pipeline filters the List<Student> dynamically based on non-null parameters.
     */
    public List<Student> searchStudents(Integer id, String email, String course) {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .filter(s -> id == null || s.getStudentId() == id)
                .filter(s -> email == null || email.trim().isEmpty() || s.getEmail().equalsIgnoreCase(email.trim()))
                .filter(s -> course == null || course.trim().isEmpty() || s.getCourseName().equalsIgnoreCase(course.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Q2.5: Merit list sorted by average score descending.
     * Collection use: Stream API with Comparator.comparingDouble on List<Student> to sort elements in descending order.
     */
    public List<Student> getMeritList() {
        return studentRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Q2.6: Group students who share the same technical skill.
     * Keyed by skill (case-insensitive).
     * Returns only skills shared by 2 or more students.
     * Collection use: Map<String, List<Student>> using a case-insensitive TreeMap for grouping,
     * followed by Stream entrySet filtering to retain skills shared by >= 2 students, collected into a LinkedHashMap.
     */
    public Map<String, List<Student>> getStudentsSharingSkills() {
        List<Student> students = studentRepository.findAll();
        // Case-insensitive TreeMap ensures skills like "Java", "java", "JAVA" map to the same key
        Map<String, List<Student>> skillMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Student student : students) {
            if (student.getTechnicalSkills() != null) {
                for (String rawSkill : student.getTechnicalSkills()) {
                    if (rawSkill != null && !rawSkill.trim().isEmpty()) {
                        String skill = rawSkill.trim();
                        skillMap.computeIfAbsent(skill, k -> new ArrayList<>());
                        List<Student> studentList = skillMap.get(skill);
                        if (!studentList.contains(student)) {
                            studentList.add(student);
                        }
                    }
                }
            }
        }

        // Stream filter: retain only skills shared by 2 or more students
        return skillMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    /**
     * Q2.7: Retrieve students with programmingScore < 50 and mathematicsScore >= 75.
     * Collection use: Stream filter predicate applied on List<Student> to isolate students matching specific multi-attribute conditions.
     */
    public List<Student> getStudentsNeedingImprovement() {
        return studentRepository.findAll().stream()
                .filter(s -> s.getProgrammingScore() < 50.0 && s.getMathematicsScore() >= 75.0)
                .collect(Collectors.toList());
    }
}

