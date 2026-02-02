package com.yalchenko.task_manager.controller;

import com.yalchenko.task_manager.dto.task.TaskRequest;
import com.yalchenko.task_manager.dto.task.TaskResponse;
import com.yalchenko.task_manager.entity.User;
import com.yalchenko.task_manager.service.TaskService;
import com.yalchenko.task_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal
    ) {
        User author = userService.findByEmail(principal.getUsername());
        return ResponseEntity.ok(taskService.createTask(request, author));
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User user = userService.findByEmail(principal.getUsername());
        return ResponseEntity.ok(taskService.updateTask(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User user = userService.findByEmail(principal.getUsername());
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@RequestParam Optional<String> status,
                                                       @RequestParam Optional<Long> assigneeId,
                                                       @RequestParam Optional<Long> authorId) {
        List<TaskResponse> tasks = taskService.getTasks(status, assigneeId, authorId);
        return ResponseEntity.ok(tasks);
    }
}
