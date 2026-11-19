package com.kiet.student.exception;

/**
 * Exception thrown when student scores are outside the valid range [0, 100].
 * Results in HTTP 400 Bad Request.
 */
public class InvalidScoreException extends RuntimeException {
    public InvalidScoreException(String message) {
        super(message);
    }
}

