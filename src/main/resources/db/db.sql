
-- добавляем уровни
insert into levels(title, description, point_from, point_to)
values ('A1',
        '•Слабые теоретические знания
        •Нуждается в дополнительных лекциях
        •Нужен индивидуальный ментор....', 5, 30),
       ('A2',
        '•Хорошее теоретическое знание
        •Способен выполнить мини проект
        •Способен самостоятельно изучать', 30, 44),
       ('B1',
        '•Способен работать в команде
        •Может выполнить сложные
        задачи в проекте с ментором.......', 44, 56),
       ('B2',
        '•Способен работать в команде
        •Может выполнить сложные задачи в проекте
        ......', 56, 70),
       ('C1', '•Может работать в проекте и не нужно ставить ментора', 70, 79),
       ('C2', '•Может вести проект как лид', 79, 88),
       ('D1', '•Готов к собеседованиям', 88, 92),
       ('D2', '•Может быть менторам и ходить на собесы', 92, 100);
-- добавляем направления
insert into direction (name, description)
values ('Backend', 'Разработка серверной части веб-приложений, работа с базами данных, API и бизнес-логикой.'),
       ('Frontend', 'Создание интерактивных пользовательских интерфейсов с использованием HTML, CSS и JavaScript.'),
       ('UX/UI designer', 'Проектирование удобных и эстетичных интерфейсов, создание макетов и прототипов.'),
       ('Java Script',
        'Динамическое программирование для веб-приложений, разработка клиентской логики и взаимодействие с сервером.'),
       ('Java', 'Объектно-ориентированное программирование для создания масштабируемых и надежных приложений.'),
       ('Python', 'Универсальный язык программирования для веб-разработки, анализа данных и автоматизации задач.'),
       ('HTML & CSS', 'Основы веб-разработки, создание структурированной разметки и стильных веб-страниц.');
-- добовляем проекты
insert into project (title, description, project_type, start_date, end_date)
values ('E-commerce Platform',
        'Разработка платформы для онлайн-торговли с поддержкой множества пользователей и безопасных платежей.',
        'COMMERCIAL', '2024-01-01', '2024-12-31'),
       ('AI Chatbot Prototype',
        'Создание прототипа чат-бота с использованием искусственного интеллекта для улучшения пользовательского опыта.',
        'SENDBOX', '2024-06-01', '2024-12-30'),
       ('Employee Management System',
        'Разработка системы управления сотрудниками для улучшения внутреннего рабочего процесса.', 'INTERNAL',
        '2024-02-15', '2024-08-15');
-- добавляем пользователя для студента
insert into "user"(firstname, lastname, role, email, password, code)
values ('Daniel', 'Ahatdzhanov', 'ROLE_STUDENT', 'student@gmail.com',
        '$2a$10$HnbCS5GOhvpQYP8JjSPYhO.ID54WDfq8.nv99cp92MuGXOFVPU4S.', 0),
       ('Admin', 'Admin', 'ROLE_ADMIN', 'admin@gmail.com',
        '$2a$10$HnbCS5GOhvpQYP8JjSPYhO.ID54WDfq8.nv99cp92MuGXOFVPU4S.', 0);
-- password=stringst
-- добавляем студента
insert into student(image, first_name, last_name, email, phone_number, status, point, code, registered, direction_id,
                    user_id)
values ('image', 'Daniel', 'Ahatdzhanov', 'student@gmail.com', '+996 700 800 900', 'EMPLOYED', 100, 0, true, 5, 2);
-- добавляем архив
insert into archive(image, first_name, last_name, date_update, old_level, new_level, old_point, new_point, student_id)
values ('image', 'Daniel', 'Ahatdazhanov', '2024-09-20T13:27:38.641926', 'A1', 'D1', 10, 20, 1),
       ('image', 'Daniel', 'Ahatdazhanov', '2024-09-20T13:27:38.641926', 'A1', 'D1', 15, 40, 1),
       ('image', 'Daniel', 'Ahatdazhanov', '2024-09-20T13:27:38.641926', 'A1', 'D1', 35, 43, null);