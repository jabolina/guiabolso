package br.com.jabolina.guiabolso.service.impl;

import br.com.jabolina.guiabolso.exception.GuiaBolsoError;
import br.com.jabolina.guiabolso.exception.GuiaBolsoException;
import br.com.jabolina.guiabolso.service.ValidationService;
import br.com.jabolina.guiabolso.util.helpers.AssertInput;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public void validate(AssertInput validator) {
        try {
            validator.assertData();
        } catch (Throwable e) {
            throw new GuiaBolsoException(GuiaBolsoError.INVALID_INPUT, "Input not valid. %s", e.getMessage());
        }
    }
}
