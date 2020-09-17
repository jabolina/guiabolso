package br.com.jabolina.guiabolso.util.helpers;

@FunctionalInterface
public interface AssertInput {
    /**
     * Function that validate the input.
     * @throws br.com.jabolina.guiabolso.exception.GuiaBolsoException if values are not valid.
     */
    void assertData();
}
