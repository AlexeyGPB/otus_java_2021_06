package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.Memento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final HashMap<Long, LinkedList<Memento>> stack = new HashMap<>();
    private final DateTimeProvider dateTimeProvider = LocalDateTime::now;

    @Override
    public void onUpdated(Message msg) {
        ObjectForMessage field13 = new ObjectForMessage();
        field13.setData(new ArrayList<>(msg.getField13().getData()));

        Message copy = new Message.Builder(msg.getId())
                .field1(msg.getField1())
                .field2(msg.getField2())
                .field3(msg.getField3())
                .field4(msg.getField4())
                .field5(msg.getField5())
                .field6(msg.getField6())
                .field7(msg.getField7())
                .field8(msg.getField8())
                .field9(msg.getField9())
                .field10(msg.getField10())
                .field11(msg.getField11())
                .field12(msg.getField12())
                .field13(field13)
                .build();
        LinkedList<Memento> stackList = stack.getOrDefault(msg.getId(), new LinkedList<>());
        stackList.add(new Memento(copy, dateTimeProvider.getSecondTime()));
        stack.put(msg.getId(), stackList);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return stack.containsKey(id)
                ? Optional.ofNullable(stack.get(id).getLast().getMessage())
                : Optional.empty();
    }
}
