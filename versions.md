# Версии и журнал

### Версия 0.2 22/04/2016
Сделал настройки приложения. Ну как сделал. Обеспечил их наличие. Вывел работу с настройками в отдельны класс. Пока что сами настройки не задействованы. 
Были некоторые траблы с Едитом из PreferenceScreen, решение хранится в списке ресурсов проекта. Обновил иконку приложения, залил на телефон. Вроде как все, лол


### Версия 0.1. 14/04/2016. 
Проделал работу по рефакторингу UI. Внедрил и проэкспериментировал с сервисами в Андроид. Переместил в него таймер и настроил Broadcast на ежесекундную отправку строки с миллисекундами разницы по системе. В активити настроил BroadcastReciever, который сканировал по Тэгу события и отображал передаваемое мною. 
Была проблема. Я сумел остановить процесс, однако в системе осталась строка по фильтру, и чем больше раз я запускал таймер, тем больше строк подгружались в активити из системы. Это значит, что вместо актуального времени в текствью записывалась и нулевая строка. ВОпрос пока не решал и не искал на стаке. Переместил назад пока что счетчик в активити, закомментировал процедуры с сервисами. 
Также выполнил юзкейс, когда при закрытии активити со счетчиком данные по проекту (время исполнения) обновлялись. Дополнил Активити редактирования заказа, тееперь там вводится стоимость проекта.

### Версия 0.2. 16/04/2016. 
Внедрена функция добавления/убавления часов, проведенных над проектом, в активити редактирования. На опенактивити сделал вывод статистической информации. Добавил градиент на активити секундомера. ПО закрытию (уничтожению) активити секундмера данные о времени записываются в интент. Есть мысль перевести запись сразу в базу данных, так как в таком случае при завершении приложения системой время работы сохранится.

### Версия 0.4. 13/05/2016. 
Сделал code refactoring. Насмотрелся видеоуроков от [Hexlet.io](https://www.youtube.com/channel/UCYdmV72o01clQdSF3E5s6tw) и 
решил *причесать* код в соответсии с паттернами программирования. В частности, сейчас был реализован некоторый мелкий полиморфизм. 
Организовал CRUD операции в один пакет без глобальной видимости, но создал класс Database, который организует передачу параметров этим CRUD классам.
Рефектиронг коснулся и Интерфейсов - создал для базы данных один интерфейс, а имплементацию - вывел в активити вместо создания new IExecuteResult при вызове. 
Стало как-то менее стыдно за проект, даже показать людям можно.