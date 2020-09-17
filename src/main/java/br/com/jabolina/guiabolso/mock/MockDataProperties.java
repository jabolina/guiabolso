package br.com.jabolina.guiabolso.mock;

public interface MockDataProperties {
    abstract class Builder<T, B extends Builder<T, B>> implements br.com.jabolina.guiabolso.util.helpers.Builder<T> {
        /**
         * Verify if the given data is valid.
         */
        abstract void validate();
    }
}
