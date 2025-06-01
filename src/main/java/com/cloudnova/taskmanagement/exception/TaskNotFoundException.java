
package com.cloudnova.taskmanagement.exception;

/**
 * Custom exception thrown when a task is not found
 */
public class TaskNotFoundException extends RuntimeException {
    
    public TaskNotFoundException(String message) {
        super(message);
    }
    
    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }
    
    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}