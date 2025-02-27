# Изменения версий

## 2.4.0

### Новое в версии

* Добавлена возможность загрузки расширений из хранилища расширения на этапе `prepareBase`
* Добавлена сборка расширений на этапе `build`

### Дополнительно

* Исправлена ошибка переопределения каталога сборки на этапе `build`
* Удалено ошибочное формирование отчетов на этапе `runExternal`
* Сборка поставки и расширений переведена на `vanessa-runner`

## 2.3.0

### Новое в версии

* Добавлен новый этап в сборочную линию `runExternal`
* Добавлен новый этап в сборочную линию `checkExtensions`
* Добавлена возможность оповещения в Telegram

### Дополнительно

* Исправлена ошибка использования Jenkins credential в этапах `prepareBase`, `smoke`
* Обновлена документация
* Исправлены опечатки

## 2.2.0

### Новое в версии

* Переход на декларативные pipline-s
* Добавлена возможность указать путь к xUnit в этапах smoke, tdd
* Добавлена возможность указать путь к vanessa-automation или add в этапе bdd
* Добавлена возможность указать путь к настройкам vanessa-automation или add в этапе bdd

### Дополнительно

* Исправлена ошибка авторизации на этапе gitsync
* Исправлены опечатки
* Добавлена начальная документация
* Скорректированы описания полей схемы JSON для конфигурационных файлах

## 2.1.0

### Новое в версии

- Загрузка расширений из исходного кода в тестовую базу
- Проверка расширений в этапе `syntax-check`
- Обновление базы из хранилища 1С версией из файла `VERSION`
- Возможность указать агента для чтения конфигурационного файла
- Чтение конфигурационного файла без дополнительного клонирования (только для GitLab проектов)
- Возможность использовать временную базу в этапе `gitsync`
- Возможность указать каталог временных файлов в этапе `gitsync`
- Синхронизация релизов 1С в git-репозиторий с помощью `yard`

### Дополнительно

- Добавлено логирование
- Исправлены опечатки

## 2.0.0

Первый релиз новой редакции библиотеки.

### Новое в версии

- Конфигурирование через json-файлы
- Одновременное использование скриптов и объектной модели
- (beta) Матричное тестирование
- Отправка уведомлений в Slack

## 1.7.0

### Новое в версии

- В отчет Allure добавлено разделение по категориям для синтаксической проверки, TDD/BDD-тестов

### Изменения

- Отладка sonar-scanner через опцию `SONAR_DEBUG`
- Запуск сборки релиза контролируется через опцию `PROCEDURE_BUILD_RELEASE`

### Исправление ошибок

- Для этапа синтаксической проверки исправлен путь к отчету Allure

## 1.6.5

### Изменения

- Реализована авторизация в Конфигуратор ИБ 1С для packman
- Восстановлена авторизация в хранилище 1С

## 1.6.0

### Изменения

- 2 вида авторизации - для Конфигуратора и Предприятия
- Произвольный файл обработки для выполнения обновления в режиме Предприятия вместо существующего кода выполнения через vrunner run

- Поддержка режима реструктуризации БД V2
- Управление режимами синтакс-проверки
- Управление отладочными логами приложений и пакетов oscript
    - добавлен параметр `OSCRIPT_LOGOS_LEVEL_DEBUG`

- Уточнены пути результатов тестирования для Junit и Allure
    - По умолчанию все результаты от Аллюр получаются в каталоги
        - дымовые - `out/allure`
        - bdd - `out/bddallure`
        - синтакс-проверка - `out/syntaxCheck/allure`
    - все результаты `junit` - в каталог `out/junit`

- Показ версий Usher и приложений oscript, vanessa-runner, packman, gitsync, установленных на ноде, при старте заданий

## 1.5.0

### Изменения

- Восстановлено обновление БД при загрузке из хранилища

## 1.4.2

### Изменения

- исправлена опечатка для команды синтакс-контроля

## 1.4.1

### Изменения

- Добавлен параметр `TIMEOUT_GITSYNC` со значением по умолчанию 60 минут, определяющий таймаут выполнения задачи `gitsync`
- Добавлен параметр `TIMEOUT_CI` со значением по умолчанию 60 минут, определяющий таймаут выполнения задачи сборочной линии

## 1.3.3

### Изменения

- Добавлено использование конфигурационного файла vrunner для всех шагов vrunner, в т.ч. при подготовке тестовой базы и синтаксическом контроле.
- В 2 раза увеличен таймаут выполнения сборочных задач.

## 1.3.2

### Исправление ошибок

- Исправление ошибки: No such property: sendEmailMessage

## 1.3.1

### Новое в версии

- запрет паралельных запусков задач синхронизаций git
- поддержка обновления серверной тестовой базы из хранилища 1С. Добавлены новые параметры

## 1.2.1

### Новое в версии

- добавлена возможность указания ноды для проведения статического анализа SonarQube
- добавлена возможность публикации сообщений в RabbitMQ. Для работы должен быть установлен плагин [rabbitmq-publisher](https://plugins.jenkins.io/rabbitmq-publisher)

## 1.1.1

### Новое в версии

- Поддержка синхронизации хранилища и Git;
- Сборочный цикл для основных сценариев контура:
    - Подготовка окружения;
    - Синтаксическая проверка;
    - Статический анализ конфигурации SonarQube;
    - TDD тестирование;
    - BDD тестирование;
    - Формирование отчетов Allure и Junit;
    - Отправка почтовых уведомлений;
    - Сборка поставки конфигурации при успешном прохождении тестирования.
- Публикация поставки конфигурации на продуктивную ИБ.
