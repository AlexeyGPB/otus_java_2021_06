package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorExceptionEvenSecondTest {
    @Test
    void timeExceptionTest() {

        var message = new Message.Builder(1L).field13(new ObjectForMessage()).build();

        var currentSecond = mock(DateTimeProvider.class);
        when(currentSecond.getSecondTime()).thenReturn(LocalDateTime.of(LocalDate.now(), LocalTime.of(2, 2, 2)));

        var processor = new ProcessorExceptionEvenSecond(currentSecond);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> processor.process(message));
    }
}

