package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorExceptionEvenSecond implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorExceptionEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        LocalDateTime time = dateTimeProvider.getSecondTime();
        if (time.getSecond() % 2 == 0) {
            throw new RuntimeException(String.format("Exception every even second, now even second - %d!", time.getSecond()));
        }
        return message;
    }
}
