package com.cloudnova.taskmanagement.repository.impl;

import com.cloudnova.taskmanagement.model.Task;
import com.cloudnova.taskmanagement.model.TaskStatus;
import com.cloudnova.taskmanagement.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TaskRepository
 * Uses ConcurrentHashMap for thread-safe operations
 */
@Repository
@Slf4j
public class TaskRepositoryImpl implements TaskRepository {
    
    // Thread-safe map for storing tasks
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    
    // Thread-safe ID generator
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Override
    public List<Task> findAll() {
        log.debug("Finding all tasks. Current count: {}", tasks.size());
        return new ArrayList<>(tasks.values());
    }
    
    @Override
    public Optional<Task> findById(Long id) {
        log.debug("Finding task by id: {}", id);
        if (id == null) {
            log.warn("Attempted to find task with null id");
            return Optional.empty();
        }
        
        Task task = tasks.get(id);
        if (task != null) {
            log.debug("Found task: {}", task.getTitle());
        } else {
            log.debug("Task not found with id: {}", id);
        }
        
        return Optional.ofNullable(task);
    }
    
    @Override
    public Task save(Task task) {
        if (task == null) {
            log.error("Attempted to save null task");
            throw new IllegalArgumentException("Task cannot be null");
        }
        
        // If task has no ID, generate one (new task)
        if (task.getId() == null) {
            task.setId(generateId());
            log.debug("Creating new task with id: {} and title: {}", task.getId(), task.getTitle());
        } else {
            log.debug("Updating existing task with id: {} and title: {}", task.getId(), task.getTitle());
        }
        
        // Update the timestamp
        task.touch();
        
        // Store the task
        tasks.put(task.getId(), task);
        
        log.info("Task saved successfully with id: {}", task.getId());
        return task;
    }
    
    @Override
    public void deleteById(Long id) {
        if (id == null) {
            log.warn("Attempted to delete task with null id");
            return;
        }
        
        Task removedTask = tasks.remove(id);
        if (removedTask != null) {
            log.info("Task deleted successfully with id: {}", id);
        } else {
            log.warn("Attempted to delete non-existent task with id: {}", id);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        
        boolean exists = tasks.containsKey(id);
        log.debug("Task existence check for id {}: {}", id, exists);
        return exists;
    }
    
    @Override
    public List<Task> findByStatus(TaskStatus status) {
        log.debug("Finding tasks by status: {}", status);
        
        if (status == null) {
            log.warn("Attempted to find tasks with null status");
            return new ArrayList<>();
        }
        
        List<Task> filteredTasks = tasks.values().stream()
                .filter(task -> status.equals(task.getStatus()))
                .collect(Collectors.toList());
        
        log.debug("Found {} tasks with status: {}", filteredTasks.size(), status);
        return filteredTasks;
    }
    
    @Override
    public long count() {
        long count = tasks.size();
        log.debug("Total task count: {}", count);
        return count;
    }
    
    @Override
    public void deleteAll() {
        int previousSize = tasks.size();
        tasks.clear();
        idGenerator.set(1); // Reset ID generator
        log.info("Deleted all tasks. Previous count: {}", previousSize);
    }

    private Long generateId() {
        return idGenerator.getAndIncrement();
    }
    
    /**
     * Get current repository statistics (for monitoring/debugging)
     * @return Map containing repository statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTasks", count());
        stats.put("nextId", idGenerator.get());
        stats.put("tasksByStatus", getTaskCountByStatus());
        
        log.debug("Repository statistics: {}", stats);
        return stats;
    }
    
    /**
     * Get count of tasks grouped by status
     * @return Map of status to count
     */
    private Map<TaskStatus, Long> getTaskCountByStatus() {
        return tasks.values().stream()
                .collect(Collectors.groupingBy(
                    Task::getStatus,
                    Collectors.counting()
                ));
    }
}