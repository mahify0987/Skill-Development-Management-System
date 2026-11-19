package com.kiet.student.exception;

/**
 * Exception thrown when attempting to create a student with an ID that already exists.
 * Results in HTTP 409 Conflict.
 */
public class DuplicateStudentIdException extends RuntimeException {
    public DuplicateStudentIdException(String message) {
        super(message);
    }
}

