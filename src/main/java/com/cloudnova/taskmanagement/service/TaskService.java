package com.cloudnova.taskmanagement.service;

import com.cloudnova.taskmanagement.model.Task;
import com.cloudnova.taskmanagement.model.TaskStatus;
import com.cloudnova.taskmanagement.exception.TaskNotFoundException;

import java.util.List;

/**
 * Service interface for Task business operations
 * Defines the contract for business logic operations
 */
public interface TaskService {
    
    /**
     * Retrieve all tasks
     * @return List of all tasks
     */
    List<Task> getAllTasks();
    
    /**
     * Get a task by its ID
     * @param id The task ID
     * @return The task if found
     * @throws TaskNotFoundException if task not found
     */
    Task getTaskById(Long id);
    
    /**
     * Create a new task
     * @param task The task to create
     * @return The created task with generated ID
     */
    Task createTask(Task task);
    
    /**
     * Update an existing task
     * @param id The task ID to update
     * @param taskUpdate The updated task data
     * @return The updated task
     * @throws TaskNotFoundException if task not found
     */
    Task updateTask(Long id, Task taskUpdate);
    
    /**
     * Delete a task by its ID
     * @param id The task ID to delete
     * @throws TaskNotFoundException if task not found
     */
    void deleteTask(Long id);
    
    /**
     * Get tasks filtered by status
     * @param status The status to filter by
     * @return List of tasks with the specified status
     */
    List<Task> getTasksByStatus(TaskStatus status);
    
    /**
     * Get total count of tasks
     * @return Total number of tasks
     */
    long getTaskCount();
    
    /**
     * Delete all tasks
     */
    void deleteAllTasks();
}