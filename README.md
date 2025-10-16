# 🐾 TGBot For Shelter - REST API для приютов животных

REST API для управления приютами для кошек и собак, усыновителями, питомцами и испытательными сроками.

---

## 🔥 ДЛЯ РАЗРАБОТЧИКОВ TELEGRAM-БОТА

### 📋 **SWAGGER UI ДОКУМЕНТАЦИЯ API**

После запуска сервера откройте:

```
🔗 http://localhost:8080/swagger-ui/index.html
```

**В Swagger UI вы найдете:**
- ✅ Все доступные эндпоинты
- ✅ Описание каждого метода
- ✅ Примеры запросов и ответов
- ✅ Возможность протестировать API прямо из браузера

---

## 🚀 Быстрый старт

### Требования
- Java 17+
- PostgreSQL 12+
- Maven 3.6+



### 1. Настройка базы данных
```bash
# Создайте базу данных
createdb tgBot

# Или через psql
psql -U postgres
CREATE DATABASE tgBot;
\q
```

### 3. Настройка application.properties
Откройте `src/main/resources/application.properties` и настройте подключение к БД:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tgBot
spring.datasource.username=postgres
spring.datasource.password=ваш_пароль
```

### 4. Запуск приложения
```bash
mvn clean install
mvn spring-boot:run
```

Сервер запустится на порту **8080**.

---

## 📚 API Endpoints

### 🏠 Приюты (Shelters)
- `POST /api/shelters` - Создать приют
- `GET /api/shelters` - Получить все приюты
- `GET /api/shelters/{id}` - Получить приют по ID
- `PUT /api/shelters/{id}` - Обновить приют
- `DELETE /api/shelters/{id}` - Удалить приют

### 👥 Пользователи (Users)
- `POST /api/users` - Создать пользователя
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `GET /api/users/telegram/{telegramId}` - Получить пользователя по Telegram ID
- `GET /api/users/volunteers` - Получить всех волонтеров
- `PUT /api/users/{id}` - Обновить пользователя
- `PATCH /api/users/{id}/volunteer` - Установить статус волонтера
- `DELETE /api/users/{id}` - Удалить пользователя

### 🐱 Питомцы (Pets)
- `POST /api/pets` - Создать питомца
- `GET /api/pets` - Получить всех питомцев
- `GET /api/pets/{id}` - Получить питомца по ID
- `GET /api/pets/search?name={name}` - Поиск по кличке
- `GET /api/pets/young?type={type}&maxAge={age}` - Получить молодых питомцев
- `PUT /api/pets/{id}` - Обновить питомца
- `DELETE /api/pets/{id}` - Удалить питомца

### 🤝 Усыновители (Adopters)
- `POST /api/adopters` - Создать усыновителя
- `GET /api/adopters` - Получить всех усыновителей
- `GET /api/adopters/{id}` - Получить усыновителя по ID
- `GET /api/adopters/user/{userId}` - Получить усыновителя по ID пользователя
- `GET /api/adopters/recent?daysAgo={days}` - Получить недавних усыновителей
- `PUT /api/adopters/{id}` - Обновить усыновителя
- `DELETE /api/adopters/{id}` - Удалить усыновителя

### ⏰ Испытательные сроки (Trial Periods)
- `POST /api/trial-periods` - Создать испытательный срок
- `GET /api/trial-periods` - Получить все испытательные сроки
- `GET /api/trial-periods/{id}` - Получить испытательный срок по ID
- `GET /api/trial-periods/adopter/{adopterId}` - Получить по ID усыновителя
- `GET /api/trial-periods/active` - Получить активные испытательные сроки
- `GET /api/trial-periods/requiring-attention` - Получить требующие внимания
- `PATCH /api/trial-periods/{id}/pass` - Завершить успешно
- `PATCH /api/trial-periods/{id}/fail` - Отметить как не пройденный
- `PATCH /api/trial-periods/{id}/extend-14` - Продлить на 14 дней
- `PATCH /api/trial-periods/{id}/extend-30` - Продлить на 30 дней
- `PATCH /api/trial-periods/{id}/increment-missed-reports` - Увеличить счетчик пропусков
- `PUT /api/trial-periods/{id}` - Обновить испытательный срок
- `DELETE /api/trial-periods/{id}` - Удалить испытательный срок

### 📝 Ежедневные отчеты (Daily Reports)
- `POST /api/daily-reports` - Создать отчет
- `GET /api/daily-reports` - Получить все отчеты
- `GET /api/daily-reports/{id}` - Получить отчет по ID
- `GET /api/daily-reports/for-review` - Получить отчеты для проверки волонтером
- `GET /api/daily-reports/latest/{trialPeriodId}` - Получить последний отчет
- `GET /api/daily-reports/has-today/{trialPeriodId}` - Проверить наличие отчета за сегодня
- `PATCH /api/daily-reports/{id}/review?poorQuality={bool}` - Проверить отчет
- `PUT /api/daily-reports/{id}` - Обновить отчет
- `DELETE /api/daily-reports/{id}` - Удалить отчет

---

## 💡 Примеры использования

### Создание приюта
```bash
curl -X POST http://localhost:8080/api/shelters \
  -H "Content-Type: application/json" \
  -d '{
    "type": "CAT",
    "name": "Приют для кошек Мурка",
    "address": "г. Москва, ул. Примерная, д. 1",
    "schedule": "Пн-Пт: 9:00-18:00",
    "securityContact": "+7 (495) 123-45-67",
    "safetyRules": "Не кормить животных без разрешения",
    "information": "Приют работает с 2010 года"
  }'
```

### Создание пользователя
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "telegramId": 123456789,
    "firstName": "Иван",
    "lastName": "Иванов",
    "username": "ivanov123",
    "phoneNumber": "+79991234567",
    "email": "ivan@example.com"
  }'
```

### Получение пользователя по Telegram ID
```bash
curl http://localhost:8080/api/users/telegram/123456789
```

### Создание ежедневного отчета
```bash
curl -X POST http://localhost:8080/api/daily-reports \
  -H "Content-Type: application/json" \
  -d '{
    "trialPeriod": {"id": 1},
    "reportDate": "2025-10-15",
    "photoPath": "/photos/cat1.jpg",
    "diet": "Сухой корм Royal Canin 2 раза в день",
    "wellBeing": "Питомец активный, хорошо ест",
    "behaviorChanges": "Начал играть с игрушками"
  }'
```

---

## 🏗️ Технологии

- **Java 17** - Язык программирования
- **Spring Boot 3.5.6** - Фреймворк
- **Spring Data JPA** - Работа с БД
- **Hibernate** - ORM
- **PostgreSQL 17** - База данных
- **Lombok** - Уменьшение boilerplate-кода
- **Swagger/OpenAPI 3** - Документация API
- **JUnit 5 + Mockito** - Тестирование
- **Maven** - Сборка проекта

---


## 🧪 Запуск тестов
```bash
# Запустить все тесты
mvn test

# Запустить тесты с отчетом о покрытии
mvn clean test jacoco:report

# Отчет будет доступен в target/site/jacoco/index.html
```
---

## 📊 HTTP Статусы

API использует стандартные HTTP-статусы:

- `200 OK` - Успешное выполнение GET/PUT
- `201 Created` - Успешное создание (POST)
- `204 No Content` - Успешное удаление (DELETE)
- `400 Bad Request` - Некорректные данные
- `404 Not Found` - Ресурс не найден
- `500 Internal Server Error` - Ошибка сервера

---

## 🔐 База данных

### Схема БД

```
shelters
├── id (PK)
├── type (CAT/DOG)
├── name
├── address
├── schedule
├── security_contact
├── safety_rules
└── information

users
├── id (PK)
├── telegram_id (UNIQUE)
├── first_name
├── last_name
├── username
├── phone_number
├── email
├── registered_at
└── is_volunteer

pets
├── id (PK)
├── name
├── type (CAT/DOG)
├── breed
├── age
├── description
└── has_disabilities

adopters
├── id (PK)
├── user_id (FK -> users)
├── shelter_id (FK -> shelters)
├── pet_id (FK -> pets)
└── adoption_date

trial_periods
├── id (PK)
├── adopter_id (FK -> adopters)
├── start_date
├── end_date
├── status
└── missed_reports

daily_reports
├── id (PK)
├── trial_period_id (FK -> trial_periods)
├── report_date
├── photo_path
├── diet
├── well_being
├── behavior_changes
├── created_at
├── reviewed
└── poor_quality
```

---

## 🤝 Интеграция с Telegram-ботом

### Типичный сценарий работы бота:

1. **Пользователь пишет боту** → Бот создает пользователя через `POST /api/users`
2. **Пользователь выбирает приют** → Бот получает приюты через `GET /api/shelters?type=CAT`
3. **Пользователь хочет усыновить питомца** → Бот создает усыновителя через `POST /api/adopters`
4. **Волонтер одобряет** → Бот создает испытательный срок через `POST /api/trial-periods`
5. **Пользователь шлет отчет** → Бот создает отчет через `POST /api/daily-reports`
6. **Волонтер проверяет** → Бот получает отчеты через `GET /api/daily-reports/for-review`


## 📞 Полезные команды

### Maven
```bash
# Собрать проект
mvn clean install

# Запустить приложение
mvn spring-boot:run

# Запустить тесты
mvn test

# Пропустить тесты при сборке
mvn clean install -DskipTests

# Создать JAR файл
mvn package
```

### PostgreSQL
```bash
# Подключиться к БД
psql -U postgres -d tgBot

# Показать все таблицы
\dt

# Показать структуру таблицы
\d users

# Выполнить запрос
SELECT * FROM users;

# Выйти
\q
```

## 🎯 Бизнес-логика

### Правила работы испытательного срока

1. Испытательный срок составляет **30 дней** по умолчанию
2. Усыновитель должен отправлять отчет **каждый день**
3. Волонтеры проверяют отчеты после **21:00**
4. Если отчет не отправлен **2 дня подряд**, волонтер получает уведомление
5. Испытательный срок может быть:
   - **Пройден** (PASSED)
   - **Продлен на 14 дней** (EXTENDED_14)
   - **Продлен на 30 дней** (EXTENDED_30)
   - **Не пройден** (FAILED)

### Формат отчета

Ежедневный отчет должен содержать:
- **Фото питомца** - путь к файлу
- **Рацион** - описание питания
- **Общее самочувствие** - как себя чувствует питомец
- **Изменения в поведении** - новые привычки или изменения

---

## 🚨 Обработка ошибок

API возвращает понятные сообщения об ошибках:

```json
{
  "timestamp": "2025-10-15T20:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Пользователь с id 999 не найден",
  "path": "/api/users/999"
}
```

### Типичные ошибки:

- **IllegalArgumentException** - некорректные данные
- **EntityNotFoundException** - сущность не найдена
- **IllegalStateException** - недопустимое состояние (например, попытка продлить завершенный испытательный срок)

---
