package br.com.jabolina.guiabolso.util.helpers;

public interface Builder<T> {
    /**
     * Build a new or an already existence instance of T.
     *
     * @return a new or an already existence instance of T.
     */
    T build();
}
