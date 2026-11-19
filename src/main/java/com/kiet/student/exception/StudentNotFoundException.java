package com.kiet.student.exception;

/**
 * Exception thrown when a requested student is not found by ID.
 * Results in HTTP 404 Not Found.
 */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

