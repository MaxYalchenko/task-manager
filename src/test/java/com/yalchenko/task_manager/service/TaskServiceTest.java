package com.yalchenko.task_manager.service;

import com.yalchenko.task_manager.dto.task.TaskRequest;
import com.yalchenko.task_manager.entity.Task;
import com.yalchenko.task_manager.entity.User;
import com.yalchenko.task_manager.entity.enums.Role;
import com.yalchenko.task_manager.mapper.TaskMapper;
import com.yalchenko.task_manager.repository.TaskRepository;
import com.yalchenko.task_manager.repository.UserRepository;
import com.yalchenko.task_manager.service.impl.TaskServiceImpl;
import com.yalchenko.task_manager.validator.TaskAccessValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class TaskServiceTest {

    private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final TaskMapper taskMapper = new TaskMapper();
    private final TaskAccessValidator accessValidator = new TaskAccessValidator();

    private final TaskService taskService =
            new TaskServiceImpl(taskRepository, userRepository, taskMapper, accessValidator);

    @Test
    void shouldCreateTaskSuccessfully() {
        User author = new User();
        author.setId(1L);
        author.setUsername("author");
        author.setRole(Role.USER);

        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");

        Mockito.when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = taskService.createTask(request, author);

        assertThat(response.getTitle()).isEqualTo("Test Task");
        assertThat(response.getAuthorUsername()).isEqualTo("author");
    }
}
