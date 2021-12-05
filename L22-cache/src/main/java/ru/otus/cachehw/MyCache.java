package ru.otus.cachehw;


import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    public int size() {
        return cache.size();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        event(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        event(key, value, "removed");
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        event(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void event(K key, V value, String action) {
        listeners.forEach(l -> {
            try {
                l.notify(key, value, action);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
