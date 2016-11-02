package utils;

import java.util.Map;

/**
 * Created by mirko on 12/10/2016.
 */
public interface SymbolTable<K, V> {
    void add(K key, V value);

    boolean contains(K key);

    V getValue(K key);

    String toString();

    Iterable<Map.Entry<K, V>> getAll();
}