package com.cloudnova.taskmanagement.service.impl;

import com.cloudnova.taskmanagement.exception.TaskNotFoundException;
import com.cloudnova.taskmanagement.model.Task;
import com.cloudnova.taskmanagement.model.TaskStatus;
import com.cloudnova.taskmanagement.repository.TaskRepository;
import com.cloudnova.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of TaskService
 * Contains business logic for task operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepository taskRepository;
    
    @Override
    public List<Task> getAllTasks() {
        log.debug("Retrieving all tasks");
        List<Task> tasks = taskRepository.findAll();
        log.info("Retrieved {} tasks", tasks.size());
        return tasks;
    }
    @Override
    public Task getTaskById(Long id) {
        log.debug("Retrieving task with id: {}", id);
        validateId(id);
        
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", id);
                    return new TaskNotFoundException(id);
                });
    }
    
    @Override
    public Task createTask(Task task) {
        log.debug("Creating new task with title: {}", task != null ? task.getTitle() : "null");
        
        // Validate the task
        validateTaskForCreation(task);
        
        // Ensure ID is null for new tasks
        task.setId(null);
        
        // Save the task
        Task savedTask = taskRepository.save(task);
        log.info("Created new task with id: {} and title: {}", savedTask.getId(), savedTask.getTitle());
        
        return savedTask;
    }
    
    @Override
    public Task updateTask(Long id, Task taskUpdate) {
        log.debug("Updating task with id: {}", id);
        validateId(id);
        validateTaskForUpdate(taskUpdate);
        
        // Check if task exists
        Task existingTask = getTaskById(id);
        
        // Update fields
        updateTaskFields(existingTask, taskUpdate);
        
        // Save updated task
        Task updatedTask = taskRepository.save(existingTask);
        log.info("Updated task with id: {} and title: {}", updatedTask.getId(), updatedTask.getTitle());
        
        return updatedTask;
    }
    
    @Override
    public void deleteTask(Long id) {
        log.debug("Deleting task with id: {}", id);
        validateId(id);
        
        // Check if task exists before deletion
        if (!taskRepository.existsById(id)) {
            log.error("Attempted to delete non-existent task with id: {}", id);
            throw new TaskNotFoundException(id);
        }
        
        taskRepository.deleteById(id);
        log.info("Deleted task with id: {}", id);
    }
    
    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        log.debug("Retrieving tasks with status: {}", status);
        validateStatus(status);
        
        List<Task> tasks = taskRepository.findByStatus(status);
        log.info("Retrieved {} tasks with status: {}", tasks.size(), status);
        return tasks;
    }
    
    @Override
    public long getTaskCount() {
        log.debug("Getting total task count");
        long count = taskRepository.count();
        log.debug("Total task count: {}", count);
        return count;
    }
    
    @Override
    public void deleteAllTasks() {
        log.debug("Deleting all tasks");
        long previousCount = taskRepository.count();
        taskRepository.deleteAll();
        log.info("Deleted all tasks. Previous count: {}", previousCount);
    }
    
    /**
     * Validate task ID
     * @param id The ID to validate
     */
    private void validateId(Long id) {
        if (id == null) {
            log.error("Task ID cannot be null");
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        if (id <= 0) {
            log.error("Task ID must be positive, received: {}", id);
            throw new IllegalArgumentException("Task ID must be positive");
        }
    }
    
    /**
     * Validate task for creation
     * @param task The task to validate
     */
    private void validateTaskForCreation(Task task) {
        if (task == null) {
            log.error("Task cannot be null");
            throw new IllegalArgumentException("Task cannot be null");
        }
        
        validateTaskFields(task);
    }
    
    /**
     * Validate task for update
     * @param task The task to validate
     */
    private void validateTaskForUpdate(Task task) {
        if (task == null) {
            log.error("Task update data cannot be null");
            throw new IllegalArgumentException("Task update data cannot be null");
        }
        
        validateTaskFields(task);
    }
    
    /**
     * Validate common task fields
     * @param task The task to validate
     */
    private void validateTaskFields(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            log.error("Task title cannot be null or empty");
            throw new IllegalArgumentException("Task title is required");
        }
        
        if (task.getTitle().length() > 100) {
            log.error("Task title too long: {} characters", task.getTitle().length());
            throw new IllegalArgumentException("Task title must not exceed 100 characters");
        }
        
        if (task.getDescription() != null && task.getDescription().length() > 500) {
            log.error("Task description too long: {} characters", task.getDescription().length());
            throw new IllegalArgumentException("Task description must not exceed 500 characters");
        }
        
        if (task.getStatus() == null) {
            log.warn("Task status is null, setting default to TODO");
            task.setStatus(TaskStatus.TODO);
        }
    }
    
    /**
     * Validate task status
     * @param status The status to validate
     */
    private void validateStatus(TaskStatus status) {
        if (status == null) {
            log.error("Task status cannot be null");
            throw new IllegalArgumentException("Task status cannot be null");
        }
    }
    
    /**
     * Update task fields from update data
     * @param existingTask The existing task to update
     * @param taskUpdate The update data
     */
    private void updateTaskFields(Task existingTask, Task taskUpdate) {
        // Update title if provided
        if (taskUpdate.getTitle() != null && !taskUpdate.getTitle().trim().isEmpty()) {
            existingTask.setTitle(taskUpdate.getTitle());
        }
        
        // Update description (can be set to null/empty)
        existingTask.setDescription(taskUpdate.getDescription());
        
        // Update status if provided
        if (taskUpdate.getStatus() != null) {
            existingTask.setStatus(taskUpdate.getStatus());
        }
        
        log.debug("Updated task fields for task id: {}", existingTask.getId());
    }
}