package br.com.jabolina.guiabolso.service;

import br.com.jabolina.guiabolso.util.helpers.AssertInput;

public interface ValidationService {
    /**
     * For the given validator, verifies if everything is all right.
     *
     * @param validator: input validator.
     * @throws br.com.jabolina.guiabolso.exception.GuiaBolsoException in case input is not valid.
     */
    void validate(AssertInput validator);
}
