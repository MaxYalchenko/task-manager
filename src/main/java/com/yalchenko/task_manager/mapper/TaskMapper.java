package com.yalchenko.task_manager.mapper;

import com.yalchenko.task_manager.dto.task.TaskRequest;
import com.yalchenko.task_manager.dto.task.TaskResponse;
import com.yalchenko.task_manager.entity.Task;
import com.yalchenko.task_manager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest request, User author, User assignee) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setAuthor(author);
        task.setAssignee(assignee);
        return task;
    }

    public TaskResponse toDto(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setAuthorUsername(task.getAuthor().getUsername());
        response.setAssigneeUsername(task.getAssignee() != null ? task.getAssignee().getUsername() : null);
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdateAt());
        return response;
    }

    public void updateEntity(Task task, TaskRequest request, User assignee) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setAssignee(assignee);
    }
}
