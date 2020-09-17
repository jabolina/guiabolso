package br.com.jabolina.guiabolso.service;

import br.com.jabolina.guiabolso.mock.MockDataProperties;

/**
 * Interface that the mocking services must extend.
 *
 * @param <T>: type of the mocked object.
 */
public interface MockService<T> {

    /**
     * Create a new mock instance for the object.
     *
     * @param properties: arguments needed for construction.
     * @return a instance of the object filled with values.
     */
    T mock(MockDataProperties properties);
}
