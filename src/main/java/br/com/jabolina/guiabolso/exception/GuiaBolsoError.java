package br.com.jabolina.guiabolso.exception;

import org.springframework.http.HttpStatus;

public enum GuiaBolsoError {
    /**
     * Used when the processing of generating data fails.
     */
    GENERATION_ERROR {
        @Override
        public HttpStatus statusCode() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    },
    /**
     * Used when the given user input is not valid.
     */
    INVALID_INPUT {
        @Override
        public HttpStatus statusCode() {
            return HttpStatus.BAD_REQUEST;
        }
    },

    /**
     * Used as a general error code when the origin is unknown.
     */
    UNKNOWN_ERROR {
        @Override
        public HttpStatus statusCode() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    };

    public abstract HttpStatus statusCode();
}
