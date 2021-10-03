package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class Memento {
    private final Message message;
    private final LocalDateTime createdAt;

    public Memento(Message message, LocalDateTime createdAt) {
        this.message = message;
        this.createdAt = createdAt;
    }

    public Message getMessage() {
        return message;
    }

    public LocalDateTime getSecondTime() {
        return createdAt;
    }
}
