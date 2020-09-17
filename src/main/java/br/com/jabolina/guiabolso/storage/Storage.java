package br.com.jabolina.guiabolso.storage;

import java.util.Optional;

/**
 * Default interface for building a storage on the application.
 *
 * @param <K>: type of the key.
 * @param <V>: type of the value.
 */
public interface Storage<K, V> {
    /**
     * Insert a new value into the storage and return the previous element.
     *
     * @param k: key to associate the value.
     * @param v: value to associate the key.
     * @return the previous element or null if none exists.
     */
    V put(K k, V v);

    /**
     * Retrieve the element associated with the given key.
     *
     * @param k: key to search.
     * @return an optional containing the element or empty.
     */
    Optional<V> get(K k);
}
