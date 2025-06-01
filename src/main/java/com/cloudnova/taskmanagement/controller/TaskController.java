package com.cloudnova.taskmanagement.controller;

import com.cloudnova.taskmanagement.model.Task;
import com.cloudnova.taskmanagement.model.TaskStatus;
import com.cloudnova.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Task Management operations
 * Provides RESTful endpoints for CRUD operations on tasks
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    
    private final TaskService taskService;
    
    /**
     * Get all tasks
     * GET /api/tasks
     * 
     * @return List of all tasks
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("GET /api/tasks - Retrieving all tasks");
        
        List<Task> tasks = taskService.getAllTasks();
        
        log.info("GET /api/tasks - Successfully retrieved {} tasks", tasks.size());
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Get a specific task by ID
     * GET /api/tasks/{id}
     * 
     * @param id The task ID
     * @return The task if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        log.info("GET /api/tasks/{} - Retrieving task by ID", id);
        
        Task task = taskService.getTaskById(id);
        
        log.info("GET /api/tasks/{} - Successfully retrieved task: {}", id, task.getTitle());
        return ResponseEntity.ok(task);
    }
    
    /**
     * Create a new task
     * POST /api/tasks
     * 
     * @param task The task to create
     * @return The created task with generated ID
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        log.info("POST /api/tasks - Creating new task with title: {}", 
                task != null ? task.getTitle() : "null");
        
        Task createdTask = taskService.createTask(task);
        
        log.info("POST /api/tasks - Successfully created task with ID: {} and title: {}", 
                createdTask.getId(), createdTask.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    
    /**
     * Update an existing task
     * PUT /api/tasks/{id}
     * 
     * @param id The task ID to update
     * @param task The updated task data
     * @return The updated task
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, 
                                         @Valid @RequestBody Task task) {
        log.info("PUT /api/tasks/{} - Updating task with title: {}", 
                id, task != null ? task.getTitle() : "null");
        
        Task updatedTask = taskService.updateTask(id, task);
        
        log.info("PUT /api/tasks/{} - Successfully updated task: {}", 
                id, updatedTask.getTitle());
        return ResponseEntity.ok(updatedTask);
    }
    
    /**
     * Delete a task
     * DELETE /api/tasks/{id}
     * 
     * @param id The task ID to delete
     * @return No content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("DELETE /api/tasks/{} - Deleting task", id);
        
        taskService.deleteTask(id);
        
        log.info("DELETE /api/tasks/{} - Successfully deleted task", id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get tasks filtered by status
     * GET /api/tasks?status={status}
     * 
     * @param status The status to filter by
     * @return List of tasks with the specified status
     */
    @GetMapping(params = "status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam TaskStatus status) {
        log.info("GET /api/tasks?status={} - Retrieving tasks by status", status);
        
        List<Task> tasks = taskService.getTasksByStatus(status);
        
        log.info("GET /api/tasks?status={} - Successfully retrieved {} tasks", 
                status, tasks.size());
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Get total count of tasks
     * GET /api/tasks/count
     * 
     * @return Total number of tasks
     */
    @GetMapping("/count")
    public ResponseEntity<TaskCountResponse> getTaskCount() {
        log.info("GET /api/tasks/count - Retrieving task count");
        
        long count = taskService.getTaskCount();
        TaskCountResponse response = new TaskCountResponse(count);
        
        log.info("GET /api/tasks/count - Total tasks: {}", count);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete all tasks
     * DELETE /api/tasks
     * 
     * @return No content response
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllTasks() {
        log.info("DELETE /api/tasks - Deleting all tasks");
        
        taskService.deleteAllTasks();
        
        log.info("DELETE /api/tasks - Successfully deleted all tasks");
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Response DTO for task count endpoint
     */
    public static class TaskCountResponse {
        private final long count;
        
        public TaskCountResponse(long count) {
            this.count = count;
        }
        
        public long getCount() {
            return count;
        }
    }
}