package com.yalchenko.task_manager.service.impl;

import com.yalchenko.task_manager.dto.task.TaskRequest;
import com.yalchenko.task_manager.dto.task.TaskResponse;
import com.yalchenko.task_manager.entity.Task;
import com.yalchenko.task_manager.entity.User;
import com.yalchenko.task_manager.exception.ResourceNotFoundException;
import com.yalchenko.task_manager.exception.UnauthorizedException;
import com.yalchenko.task_manager.repository.TaskRepository;
import com.yalchenko.task_manager.repository.UserRepository;
import com.yalchenko.task_manager.service.TaskService;
import com.yalchenko.task_manager.mapper.TaskMapper;
import com.yalchenko.task_manager.validator.TaskAccessValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final TaskAccessValidator accessValidator;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper, TaskAccessValidator accessValidator) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.accessValidator = accessValidator;
    }

    @Override
    public TaskResponse createTask(TaskRequest taskRequest, User author) {
        User assignee = null;
        if (taskRequest.getAssigneeId() != null) {
            assignee = userRepository.findById(taskRequest.getAssigneeId())
                    .orElseThrow(() -> new UnauthorizedException("Assignee not found"));        }
        Task task = taskMapper.toEntity(taskRequest, author, assignee);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return taskMapper.toDto(task);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest taskRequest, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        accessValidator.validate(currentUser, task);

        User assignee = null;
        if (taskRequest.getAssigneeId() != null) {
            assignee = userRepository.findById(taskRequest.getAssigneeId())
                    .orElseThrow(() -> new UnauthorizedException("Assignee not found"));
        }

        taskMapper.updateEntity(task, taskRequest, assignee);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        accessValidator.validate(currentUser, task);
        taskRepository.delete(task);
    }

    @Override
    public List<TaskResponse> getTasks(Optional<String> status, Optional<Long> assigneeId, Optional<Long> authorId) {
        return taskRepository.findAll().stream()
                .filter(task -> status.map(s -> task.getStatus().name().equals(s)).orElse(true))
                .filter(task -> assigneeId.map(aid -> task.getAssignee() != null && task.getAssignee().getId().equals(aid)).orElse(true))
                .filter(task -> authorId.map(aid -> task.getAuthor().getId().equals(aid)).orElse(true))
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }
}
