**MODSEN CATALOG**
---
Консольное приложение для администрирования продуктового каталога с разграничением прав доступа на основе ролей и полной поддержкой CRUD-операций над сущностями.

**Технологии**

- Java 24
- Spring Boot 4.1.1
- Maven
- Spring Data JPA
- Spring Boot Validation
- PostgreSQL
- Liquibase
- Lombok
- JUnit 5, Mockito
- Docker

## Настройка окружения

- Параметры подключения к базе задаются переменными окружения
- Создайте файл .env

## Подключение к БД

- Клонировать репозиторий: `git clone `
- Запустить контейнер: `docker-compose ud -d`
- Подключение к базе данных:
    - СУБД: PostgreSQL (Latest)
    - Хост: localhost
    - Порт: 5433 (внешний) / 5432 (внутренний)
    - Имя БД: catalog_db

## Запуск

### 1. Клонировать репозиторий

```
git clone <адрес-репозитория>
cd modsen-catalog
```

### 2. Образ приложения собирается в два этапа, описанных в `Dockerfile`
**build** — образ с JDK и Maven
**runtime** — образ с JRE

build:

```
docker compose build 
```

### 3. PostgreSQL

```
docker compose up -d
```

Проверка работоспособности контейнера

```
docker compose ps
```

### 3. Запуск приложения
запустить класс `by.may.CatalogApplication`.
