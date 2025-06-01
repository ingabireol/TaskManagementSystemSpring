package com.cloudnova.taskmanagement.repository;

import com.cloudnova.taskmanagement.model.Task;
import com.cloudnova.taskmanagement.model.TaskStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Task entity operations
 * Defines the contract for data access operations
 */
public interface TaskRepository {
    
    /**
     * Retrieve all tasks
     * @return List of all tasks
     */
    List<Task> findAll();
    
    /**
     * Find a task by its ID
     * @param id The task ID
     * @return Optional containing the task if found, empty otherwise
     */
    Optional<Task> findById(Long id);
    
    /**
     * Save a new task or update an existing one
     * @param task The task to save
     * @return The saved task with generated ID
     */
    Task save(Task task);
    
    /**
     * Delete a task by its ID
     * @param id The task ID to delete
     */
    void deleteById(Long id);
    
    /**
     * Check if a task exists by its ID
     * @param id The task ID to check
     * @return true if task exists, false otherwise
     */
    boolean existsById(Long id);
    
    /**
     * Find tasks by status
     * @param status The task status to filter by
     * @return List of tasks with the specified status
     */
    List<Task> findByStatus(TaskStatus status);
    
    /**
     * Count total number of tasks
     * @return Total count of tasks
     */
    long count();
    
    /**
     * Delete all tasks
     */
    void deleteAll();
}