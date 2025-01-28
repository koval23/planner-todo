package ru.javabegin.micro.planner.todo.controller;

/*

Чтобы дать меньше шансов для взлома (например, CSRF атак): POST/PUT запросы могут изменять/фильтровать закрытые данные, а GET запросы - для получения незащищенных данных
Т.е. GET-запросы не должны использоваться для изменения/получения секретных данных

Если возникнет exception - вернется код  500 Internal Server Error, поэтому не нужно все действия оборачивать в try-catch

Используем @RestController вместо обычного @Controller, чтобы все ответы сразу оборачивались в JSON,
иначе пришлось бы добавлять лишние объекты в код, использовать @ResponseBody для ответа, указывать тип отправки JSON

Названия методов могут быть любыми, главное не дублировать их имена и URL mapping

*/
//
//@RestController
//@RequiredArgsConstructor
//// базовый URI не нужен, т.к. метод только один
//public class StatController {
//
//    private final StatService statService; // сервис для доступа к данным (напрямую к репозиториям не обращаемся)
//
//    // для статистика всгда получаем только одну строку с id=1 (согласно таблице БД)
//    @PostMapping("/stat")
//    public ResponseEntity<Stat> findByEmail(@AuthenticationPrincipal Jwt jwt) {
//
//        // можно не использовать ResponseEntity, а просто вернуть коллекцию, код все равно будет 200 ОК
//        return ResponseEntity.ok(statService.findStat(jwt.getSubject()));
//    }
//
//
//}
