package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.*;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value

        var collect = data.stream()
                .filter(collector -> collector.getName() != null)
                .collect(Collectors.groupingBy(Measurement::getName,TreeMap::new,
                        Collectors.summingDouble(Measurement::getValue)));
        return collect;









    }
}
