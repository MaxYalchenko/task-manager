package com.yalchenko.task_manager.service;

import com.yalchenko.task_manager.dto.task.TaskRequest;
import com.yalchenko.task_manager.dto.task.TaskResponse;
import com.yalchenko.task_manager.entity.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    TaskResponse createTask(TaskRequest taskRequest, User author);

    TaskResponse getTaskById(Long id);

    TaskResponse updateTask(Long id, TaskRequest taskRequest, User currentUser);

    void deleteTask(Long id, User currentUser);

    List<TaskResponse> getTasks(Optional<String> status, Optional<Long> assigneeId, Optional<Long> authorId);
}
