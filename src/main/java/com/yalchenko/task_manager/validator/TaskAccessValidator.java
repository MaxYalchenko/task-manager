package com.yalchenko.task_manager.validator;

import com.yalchenko.task_manager.entity.Task;
import com.yalchenko.task_manager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TaskAccessValidator {

    public void validate(User currentUser, Task task) {
        boolean isAuthor = task.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");

        if (!isAuthor && !isAdmin) {
            throw new RuntimeException("Access denied: Only author or admin can modify this task");
        }
    }
}
