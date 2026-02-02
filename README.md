# Task Manager


RESTful сервис для управления задачами, реализованный на Spring Boot с использованием Spring Data JPA, JWT и Spring Security.


---


## Перед запуском
Укажите свои данные в конфигурационном файле `application.yml`, а именно URL вашей БД, логин, пароль и секретный ключ (HS512) для работы JWT.


Запустите Postman и импортируйте `postman_task_manager.json`.


---


## Auth
- **POST** `/api/auth/register` — регистрация пользователя
- **POST** `/api/auth/login` — получение JWT


---


## Tasks
- **POST** `/api/tasks` — создание задачи
- **GET** `/api/tasks/{id}` — получение задачи по ID
- **PUT** `/api/tasks/{id}` — обновление задачи
- **DELETE** `/api/tasks/{id}` — удаление задачи
- **GET** `/api/tasks?status=IN_PROGRESS&assigneeId=2&authorId=1` — получение списка задач с фильтрацией по `status`, `assigneeId`, `authorId`


> Тела запросов смотрите в `postman_task_manager.json`
