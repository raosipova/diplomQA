# Дипломный проект

### Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

Приложение представляет собой веб-сервис, который предоставляет возможность купить тур по определённой цене с помощью
двух способов:

* Обычная оплата по дебетовой карте
* Уникальная технология: выдача кредита по данным банковской карты

### Начало работы

Небольшой набор инструкций, для запуска проекта на локальном ПК.

#### Установить на ПК:

* JAVA JDK 11
* IntelliJ IDEA
* Docker Desktop
* GIT
* Браузер: Chrome Версия 124.0.6367.208 (Официальная сборка), (64 бит)
* Node jc 20.15.0 с пакетным менеджером npm
* Клонировать [репозиторий](https://github.com/raosipova/diplomQA)

### Порядок запуска Дипломного проекта.

* Запустить Docker Desktop

* Открыть тестовый проект в IntelliJ IDEA

* В терминале IntelliJ IDEA выполнить команду `docker-compose up`, дождаться подъема контейнеров.

* В терминале IntelliJ IDEA добавить новый терминал и выполнить команду для запуска приложения:

- для MySQL: `java -jar .\aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app`

- для Postgres: `java -jar .\aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app`

* В терминале IntelliJ IDEA добавить новый терминал и выполнить команды для запуска эмулятора:

- `cd gate-simulator` #перейти в папку с эмулятором
- `npm start` #запустить проект node jc

* В терминале IntelliJ IDEA добавить новый терминал и выполнить команду для прогона автотестов: (Полный прогон
  автотестов занимает от 17 до 20 минут.)

- для MySQL: `.\gradlew clean test -D dbUrl=jdbc:mysql://localhost:3306/app -D dbUser=app -D dbPass=pass`
- для Postgres: `.\gradlew clean test -D dbUrl=jdbc:postgresql://localhost:5432/app -D dbUser=app -D dbPass=pass`

* В терминале IntelliJ IDEA выполнить команду для получения отчета: `.\gradlew allureServe`
* После завершения прогона автотестов и получения отчета:

* Завершить обработку отчета сочетанием клавиш CTRL + C, в терминале нажать клавишу Y, нажать Enter.
* Закрыть приложение сочитанием клавиш CTRL + C в терминале запуска.
* Закрыть эмулятор сочитанием клавиш CTRL + C в терминале запуска.
* Остановить работу контейнеров сочитанием клавиш CTRL + C в терминале запуска.

### Лицензия:
Не требуется

### Список документов:

* [План автоматизации тестирования](https://github.com/raosipova/diplomQA/blob/main/docs/Plan.md)
* [Отчет по итогам автоматизации](https://github.com/raosipova/diplomQA/blob/main/docs/Summary.md)
* [Отчет о проведенном тестировании](https://github.com/raosipova/diplomQA/blob/main/docs/Report.md)