package com.cloudnova.taskmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing the possible states of a task
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {
    TODO("To Do"),
    IN_PROGRESS("In Progress"), 
    COMPLETED("Completed");
    
    private final String displayName;

}