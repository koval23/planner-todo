package ru.javabegin.micro.planner.utils.aop;

//@Aspect
//@Component
//@Log
//
//// можно динамически изменять поведение кода с помощью аспектов
//public class LoggingAspect {
//
//    //аспект будет выполняться для всех методов из пакета контроллеров
//    @Around("execution(* ru.javabegin.micro.planner.todo.controller..*(..)))")
//    public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
//    {
//
//        // считываем метаданные - что сейчас выполняется
//        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
//
//        // получить информацию о том, какой класс и метод выполняется
//        String className = methodSignature.getDeclaringType().getSimpleName();
//        String methodName = methodSignature.getName();
//
//        log.info("-------- Executing "+ className + "." + methodName + "   ----------- ");
//
//        StopWatch countdown = new StopWatch();
//
//        //  засекаем время
//        countdown.start();
//        Object result = proceedingJoinPoint.proceed(); // выполняем сам метод
//        countdown.stop();
//
//        log.info("-------- Execution time of " + className + "." + methodName + " :: " + countdown.getTotalTimeMillis() + " ms");
//
//        return result;
//    }
//
//}
