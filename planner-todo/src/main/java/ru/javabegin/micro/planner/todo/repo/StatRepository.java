package ru.javabegin.micro.planner.todo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.micro.planner.entity.Stat;

// принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByUserId(String id); // всегда получаем только 1 объект, т.к. 1 пользователь содержит только 1 строку статистики (связь "один к одному")
}
