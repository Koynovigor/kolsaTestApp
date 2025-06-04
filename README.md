# Kolsa Test App (Android)

Android-приложение для просмотра и воспроизведения видео-тренировок.  
Реализовано как часть тестового задания, демонстрирует чистую архитектуру, современный стек и автоматический выпуск релизов через GitHub Actions.

---

## ✨ Основной функционал

| Экран                          | Возможности                                                                      |
|--------------------------------|----------------------------------------------------------------------------------|
| **Список тренировок**          | • Поиск по названию <br>• Фильтрация по типу <br>• RecyclerView                  |
| **Экран тренировки с плеером** | • Встроенный ExoPlayer <br>• Переключение скорости <br>• SeekBar <br>• Перемотка |
| **Кодовая база**               | • Kotlin <br>• MVVM + Clean Architecture <br>• Hilt (DI), Navigation Component   |
| **CI/CD**                      | • Автоматические релизы                                                          |     

---

## 🧱 Архитектура

app/
├─ presentation UI, ViewModel, адаптеры
├─ domain бизнес-логика, модели, use-case'ы
├─ data
│ ├─ remote API, DTO, Retrofit, Moshi
│ ├─ local Room, DAO, сущности
│ └─ repository общая точка доступа к данным
└─ di Hilt-модули

---

## 🔌 API

| Endpoint | Метод | Ответ |
|----------|-------|-------|
| `/get_workouts` | GET | Список тренировок: id, title, description, type, duration |
| `/get_video?id=` | GET | Ссылка на видео по id: id, duration, link |

* Тип тренировки: `1 = тренировка`, `2 = эфир`, `3 = комплекс`
* Retrofit + Moshi с маппингом DTO в доменные модели.

---

## 🚀 CI/CD пайплайн

**.github/workflows/android.yml** — автоматизация с помощью GitHub Actions

---

## ⚙️ Как запустить проект

1. Склонировать проект:
    ```bash
    git clone https://github.com/Koynovigor/kolsaTestApp.git
    cd kolsaTestApp
    ```
2. Добавить local.properties:
    ```bash
    API_BASE_URL=https://ref.test.kolsa.ru/
    ```
3. Собрать и запустить