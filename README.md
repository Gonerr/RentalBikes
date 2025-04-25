# BikeRental_Service

Сервис для аренды велосипедов

`<a name="overview"></a>`

## 1. Обзор системы

Система управления прокатом велосипедов представляет собой RESTful веб-сервис, реализующий:

- Многоуровневую архитектуру (Controller-Service-Repository)
- Наследование сущностей с стратегией `JOINED`
- Сложные бизнес-правила аренды
- Автоматическую обработку просроченных аренд

**Ключевые технологии**:

- Spring Boot 3.2
- Hibernate 6.4
- Lombok
- PostgreSQL

## 2. Функционал сервиса

**Сервис позволяет**:

- Арендовать и возвращать велосипеды
- Управлять клиентами, пунктами проката и велосипедами
- Просматривать активные и завершенные аренды

## Основные сущности

### 2.1. Велосипеды (Bicycles)

**Типы велосипедов:**
- Горные (Mountain)
- Шоссейные (Road)
- Городские (City)  
  (Реализовано через наследование с @Inheritance)

**Характеристики:**
- Материал рамы
- Вес
- Количество скоростей
- Для горных: тип подвески, размер колес
- Для шоссейных: ширина покрышек, аэродинамика
- Для городских: наличие корзины, крыльев, ремня

**Endpoints:**

| Метод | Endpoint | Описание | Параметры |
|-------|----------|----------|-----------|
| GET | `/api/bicycles` | Список всех велосипедов с фильтрами | `type`, `rentalPoint`, `minPrice`, `maxPrice`, `availability`, `startDate`, `endDate` |
| GET | `/api/bicycles/available` | Доступные для аренды велосипеды | - |
| GET | `/api/bicycles/type/{type}` | Велосипеды по типу | `type` (MOUNTAIN, ROAD, CITY) |
| GET | `/api/bicycles/{id}` | Информация о конкретном велосипеде | - |
| GET | `/api/bicycles/{id}/active-rentals` | Активные аренды для велосипеда | - |
| GET | `/api/bicycles/{id}/check-availability` | Проверка доступности | `startDate`, `endDate` |
| POST | `/api/bicycles` | Создать новый велосипед | Тело запроса (BicycleRequest) |
| POST | `/api/bicycles/{id}/rent` | Арендовать велосипед | `startDate`, `endDate` |
| POST | `/api/bicycles/{id}/return` | Вернуть велосипед | `actualReturnDate` |

### 2.2. Клиенты (Clients)

**Функциональность:**
- Регистрация клиентов с контактными данными
- Просмотр истории аренд

**Endpoints:**

| Метод | Endpoint | Описание | Параметры |
|-------|----------|----------|-----------|
| POST | `/api/clients` | Создать клиента | `name`, `email`, `phone` |
| GET | `/api/clients/{id}` | Информация о клиенте | - |
| GET | `/api/clients` | Список всех клиентов | - |

### 2.3. Пункты проката (RentalPoints)

**Функциональность:**
- Просмотр пунктов проката
- Управление велосипедами в пунктах

**Endpoints:**

| Метод | Endpoint | Описание | Параметры |
|-------|----------|----------|-----------|
| GET | `/api/rental-points` | Список всех пунктов проката | - |
| GET | `/api/rental-points/{id}` | Информация о конкретном пункте | - |

### 2.4. Аренда (Rentals)

**Функциональность:**
- Создание аренды (клиент + велосипед + срок)
- Автоматическая проверка просроченных аренд
- Ручное завершение аренды

**Endpoints:**

| Метод | Endpoint | Описание | Параметры |
|-------|----------|----------|-----------|
| POST | `/api/rentals` | Начать аренду | Тело запроса (RentalRequest) |
| GET | `/api/rentals` | Список активных аренд | - |
| GET | `/api/rentals/{id}` | Информация об аренде | - |
| POST | `/api/rentals/{id}/return` | Завершить аренду | - |

### 2.5. Производители (Manufacturers)

**Endpoints:**

| Метод | Endpoint | Описание | Параметры |
|-------|----------|----------|-----------|
| GET | `/api/manufacturers` | Список производителей | - |
| GET | `/api/manufacturers/{id}` | Информация о производителе | - |
| POST | `/api/manufacturers` | Создать производителя | Тело запроса (Manufacturer) |

<img src="Диаграмма%20классов.png" alt="Диаграмма классов" width="700"/>

## 3. Архитектурные решения

### 3.1 Наследование сущностей

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "bike_type")
public abstract class Bicycle {
    // Базовые поля
}

@Entity
@DiscriminatorValue("MOUNTAIN")
public class MountainBicycle extends Bicycle {
    private String suspensionType;
    private double wheelSize;
}

@Entity
@DiscriminatorValue("ROAD") 
public class RoadBicycle extends Bicycle {
    private double tireWidth;
    private boolean isAero;
}
```

## 4. База данных

### Диаграмма структуры БД

#### `<img src="бд.png" alt="Диаграмма сущностей" width="700"/>`

### Описание таблиц

### Специализированные таблицы велосипедов

4. **city_bicycles** - характеристики городских велосипедов:

   - Наличие корзины (`has_basket`)
   - Наличие крыльев (`has_fenders`)
   - Тип привода (`has_belt`)
5. **mountain_bicycles** - горные велосипеды:

   - Тип подвески (`suspension_type`)
   - Размер колес (`wheel_size`)
6. **road_bicycles** - шоссейные велосипеды:

   - Ширина покрышек (`tire_width`)
   - Аэродинамическая конструкция (`is_aero`)

### Аренда

7. **rental_points** - пункты проката:

   - `id` (PK)
   - `location` - местоположение
8. **rentals** - информация об аренде:

   - `id` (PK)
   - `client_id` (FK) - клиент
   - `bicycle_id` (FK) - велосипед
   - `rental_point_id` (FK) - пункт выдачи
   - Даты аренды (`start_date`, `end_date`)
   - Фактические даты (`actual_end_date`, `actual_return_date`)
   - Стоимость (`total_cost`)
   - Статус (`status`)

### Вспомогательные таблицы

9. **characteristics_of_bicycles** - технические характеристики:

   - Количество передач (`gear_count`)
   - Наличие фар (`has_lights`)
   - Вес (`weight`)
   - Тип (`type`)
10. **client_rented_bicycles** - связь клиентов и арендованных велосипедов (many-to-many)
11. **rental_point_bicycles** - связь пунктов проката и велосипедов (many-to-many)

### Ключевые связи

- Клиент может арендовать несколько велосипедов (через таблицу `client_rented_bicycles`)
- Каждый велосипед принадлежит определенному производителю (`manufacturer_id`)
- Аренда всегда привязана к пункту проката (`rental_point_id`)
