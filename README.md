# JaVlasovBlog - блог о программировании и технологиях | blog about programming and technologies.
[English version bellow](#English-version)
___

<h2 align="center">

![image](./readme_assets/JaVlasov.jpg )</h2>

<h2 align="center">

<a  href="https://javlasov-blog.herokuapp.com/">Демоверсия (В данный момент ссылка на работает. Просьба запускать локально)</a></h2>


# Навигация
- [Описание](#Описание)
- [Локальный запуск](#Как-запустить-локально)
- [База данных](#База-данных)
- [Покрытие тестами](#Покрытие-тестами)
- [Навигация по проекту](#Навигация-по-проекту) 
- [Используемые технологии](#Используемые-технологии)
- [Контанктные данные](#Контактные-данные)

___
## Описание:

Это дипломный проект образовательной платформы [Skillbox](https://skillbox.ru/), курса **Java-разработчик**.

Данный проект представляет собой обычный блог, который базируется на технологии SpringBoot. (список исп. технологий см. [тут](#Используемые-технологии))

<h2 align="center">

Важно! Весь фронтенд был предоставлен платформой [Skillbox](https://skillbox.ru/)
</h2>

На данном блоге можно:

- Смотреть чужие посты;
- Регистрироваться;
- Редактировать свой профиль;
    - Есть возможность загрузить свою фотографию (она будет автоматически обрезана). Допустимые форматы фотографии jpg\jpeg и png. При попытке загрузить другие форматы вы увидите ошибку. Также можно меняит имя, пароль и email.
- Писать свой пост. Важно: после публикации пост должен утвердить или отклонить модератор сайта;
- Редактировать свои посты;
    - после редактирования на посту автоматически появится галочка "Скрытая публикация". Уберите её, если хотите, чтобы другие пользователи увидели ваш текст.
- Писать комментарии и отвечать на них;
    - также есть возможность помима текста добавить любую фотографию или картинку на ваш выбор (помним про формат файла).
- Ставить оценки публикациям; (лайки или дизлайки)
- Смотреть статистику; (если она открыта)
    - Смотреть статистику можно как свою, так и общую по всему блогу.
- Искать публикации: по слову, по тэгу, по дате публикации;

Несколько медиафайлов для демонстрации:

<h2 align="center">

![image](https://github.com/VlasovM/JaVlasovBlog/blob/master/readme_assets/gif%20demo%20application.gif)</h2>

![image](./readme_assets/Demo%20for%20application.png)

## Как запустить локально:

Для того, чтобы запустить проект локально вам необходимы JDK 11, система контроля версий git, сборщик проектов maven.
Клонировать проект можно через git bash:

    git clone https://github.com/VlasovM/JaVlasovBlog.git
    
Далее вам нужно изменить такие параметры, как пароль и логин к подключению базы данных, пароль и email для безопасности google для отправления писем. Эти переменные лежат в файле application.properties.

Изменить эти параметры можно тремя способами: 
- Просто менять ссылку переменной окружения на своё значение;
- При запуске jar файла запустить его с параметрами;

Для первого варианта вам нужно настроить следующие переменные окружения: 

- SESSION EMAIL
- SESSION PASSWORD
- DB_USERNAME
- DB_PASSWORD

Далее независимо какой способ изменения вы выбрали, следует выполнить команду:
 
    mvn clean install
    
Если вы поменяли переменные окружения самостоятельно, то запустить проект можно следующим образом:

    java -jar javlasovblog-0.0.1-SNAPSHOT.jar
    
Если вы не меняли переменные окружения, то нужно задать их при запуске проекта:

    java -jar javlasovblog-0.0.1-SNAPSHOT.jar --spring.datasource.username=usernameDB --spring.datasource.password=passwordDB --session.email=email --session.password=password
    
 Где:
  - usernameDB - логин локальной базы данных;
  - passwordDB - пароль локальной базы данных;
  - email - почта для валидации безопасности от гугла (в моём случае);
  - password - пароль, сгенерированный гуглом для валидации безопасности;
    
При запуске заменить usernameDB, passwordDB, email, password на ваши собственные значения.
____

## База данных:

В качестве БД используется SQL, в качестве СУБД используется MySQL (локально) и PHPAdministrator (деплой).

Для удобства покажу структуру БД в виде ER-диаграммы:

<h2 align="center">

![image](./readme_assets/Er%20diagramm.PNG)</h2>

Также дамп базы данных будет доступен по ссылке вот [тут](https://drive.google.com/file/d/1fevEo2pctIz5yZQFCP49EjsDljpog-LP/view?usp=sharing)

В базе данных будут лежать глобальные настройки (таблица global settings), остальные таблицы пустые, создавайте какие вам захочется сущности.
____

## Покрытие тестами:

Тестовые методы прописывались для сервисного слоя (папка service). 

В тестах были использованы такие технологии как JUnit 5 и Mockito.

Процент покрытие тестами были проверены с помощью встроенной утилитой среды разработки (Intelij IDEA) -  Coverage.

Результаты покрытия тестами:

<h2 align="center">

![image](./readme_assets/All%20%20results%20coverage.PNG)</h2>

<h2 align="center">

![image](./readme_assets/Result%20tests%20coverage.PNG)</h2>

____
## Навигация по проекту:

[src -> main -> java -> com -> javlasov -> blog:](https://github.com/VlasovM/JaVlasovBlog/tree/master/src/main/java/com/javlasov/blog)

- annotations -> собственные аннотации на валидацию имени, пароля и почты;
- aop ->
    - exceptions -> создание собственных классов ошибок; (наследников Runtime Exception)
    - handlers -> кастомные обработчики тех или иных ошибок;
- api -> 
    - request -> классы-запросы от сервера, содержащие определенные переменные;
    - response -> классы-ответы для сервера в результате обработки их сервисами;
- config -> настройка конфигурации проекта;
- constants -> постоянное значение формата даты и времени;
- controller -> слой контроллеров;
- dto -> объекты передачи данных; (DTO)
- mappers -> преобразователь POJO -> DTO;
- model ->
    - enums -> классы Enum для Spring Security;
    - entity-классы для таблиц базы данных;
- repository -> слой репозиториев базы данных; (Jpa)
- security -> настройки для авторизации и аутентификации пользователя;
- service -> сервисный слой; (обработка запросов)

Тесты:

[src -> test -> java -> com -> javlasov -> blog -> service](https://github.com/VlasovM/JaVlasovBlog/tree/master/src/test/java/com/javlasov/blog/service)

____

## Используемые технологии:

- Maven;
- Spring;
- Spring boot;
- Spring Security;
- Spring Data;
- Hibernate;
- Junit 5;
- Mockito;
- MapStruct;
- Javax.mail;
- Jsoup;
- Lombok;
- Cage; (captcha)
- commons-io;
- jaxb-api;
- imgscalr-lib;
- assertj-core;

____

## Контактные данные

По всем интересующим вас вопросам можно писать на почту:

m.a.vlasov97@gmail.com

JavlasovM@gmail.com

Или связаться со мной в телеграм:

https://t.me/JaVlasov

____

## English version

## Navigation

- [Description](#Description)
- [How to local start](#How-to-local-start)
- [Database](#Database)
- [Test coverage](#Test-coverage)
- [Project navigation](#Projacr-navigation)
- [Used technologies](#Used-technologies)
- [Contacts](#Contacts)

____

## Description:

This is a diploma project education platform [Skillbox](https://skillbox.ru/), course - Java-Developer.

<h2 align="center">

Important! The entire frontend was provided by the [Skillbox](https://skillbox.ru/)
</h2>

This is a just blog, which is based in technology Spring Boot. (The list of used tecnlologies [here](#Used-technologies))

On this blog:   

- Watch other people's posts;
- Register;
    - It is a possible to upload your photo. Correct format jpg/jpeg or png. You can also change a name, password and email.
- Write post;
- Edit your posts;
- Write comment and respond to them;
- Set like or dislike in publication;
- See statistics: all in blog or your;
- Find publictation: by word, by tag, by date;

Some mediafiles to dempnstration:

<h2 align="center">

![image](https://github.com/VlasovM/JaVlasovBlog/blob/master/readme_assets/gif%20demo%20application.gif)</h2>

![image](./readme_assets/Demo%20for%20application.png)

____

## How to local start:

To run project in your local PK you need JDK 11, the system of control version git and maven. 

Clone project can with git bash:

    git clone https://github.com/VlasovM/JaVlasovBlog.git
    
Next, you need to change parameters such as password and login to connect the database, password and email for google security to send emails. These variables are stored in the application.properties file.

There are three ways to change these parameters:
- Just change the reference of the environment variable to its value;
- When starting a jar file, run it with the parameters;

For the first option, you need to configure the following environment variables:

- SESSION EMAIL
- SESSION PASSWORD
- DB_USERNAME
- DB_PASSWORD

Next, regardless of which method of change you have chosen, you should run the command:

mvn clean install

If you have changed the environment variables yourself, then you can run the project as follows:

    java -jar javlasovblog-0.0.1-SNAPSHOT.jar

If you have not changed the environment variables, then you need to set them when starting the project.:

    java -jar javlasovblog-0.0.1-SNAPSHOT.jar --spring.datasource.username=usernameDB --spring.datasource.password=passwordDB --session.email=email --session.password=password

Where:
- usernameDB - login of the local database;
- passwordDB - password of the local database;
- email - mail for security validation from google (in my case);
- password - a password generated by google for security validation;

At startup, replace usernameDB, passwordDB, email, password with your own values.
   
____

## Database

I use SQL as Database and MySQL (local) as Database Management System and phpAdmin (Deploy).

I will show the database structure in the form of an ER diagram:

<h2 align="center">

![image](./readme_assets/Er%20diagramm.PNG)</h2>

The dump of databese will be [here](https://drive.google.com/file/d/1fevEo2pctIz5yZQFCP49EjsDljpog-LP/view?usp=sharing)

____

## Test coverage:

Test methods were prescribed for the service (service folder).

In test used techlonogies JUnit 5 and Mockito;

The percentage of the test coverage was checked Coverage. (Intelij Idea utility)

Test coverage result:

<h2 align="center">

![image](./readme_assets/All%20%20results%20coverage.PNG)</h2>

<h2 align="center">

![image](./readme_assets/Result%20tests%20coverage.PNG)</h2>

____

## Project navigation:

[src -> main -> java -> com -> javlasov -> blog:](https://github.com/VlasovM/JaVlasovBlog/tree/master/src/main/java/com/javlasov/blog)

- annotations -> own annotation to valid name, password and email;
- aop ->
    - exceptions -> own class of exceptions; (extends Runtime Exception)
    - handlers -> custom handler some exceptions;
- api -> 
    - request -> class-request from the server;
    - response -> class-response for server;
- config -> setting configuration of project;
- constants
- controller
- dto -> data transfer object; (DTO)
- mappers -> transformer POJO -> DTO;
- model ->
    - enums -> class Enum for Spring Security;
    - entity-classes for the database;
- repository -> Jpa repository for DB;
- security -> setting for autentification users;
- service

Tests:

[src -> test -> java -> com -> javlasov -> blog -> service](https://github.com/VlasovM/JaVlasovBlog/tree/master/src/test/java/com/javlasov/blog/service)

____

## Used technologies:

- Maven;
- Spring;
- Spring boot;
- Spring Security;
- Spring Data;
- Hibernate;
- Junit 5;
- Mockito;
- MapStruct;
- Javax.mail;
- Jsoup;
- Lombok;
- Cage; (captcha)
- commons-io;
- jaxb-api;
- imgscalr-lib;
- assertj-core;

____

## Contacts:

My email: m.a.vlasov@gmail.com

My telegram: https://t.me/JaVlasov
