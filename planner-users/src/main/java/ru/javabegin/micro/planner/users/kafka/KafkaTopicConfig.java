package ru.javabegin.micro.planner.users.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic createTopic() {
        return new NewTopic("first-topic", 1, (short) 1);
        // "my-first-topic" — имя топика
        // 1 — количество партиций
        // (short) 1 — количество реплик
    }
}
