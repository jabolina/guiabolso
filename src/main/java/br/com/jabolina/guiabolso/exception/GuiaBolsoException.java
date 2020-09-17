package br.com.jabolina.guiabolso.exception;

public class GuiaBolsoException extends RuntimeException {
    private final GuiaBolsoError error;

    public GuiaBolsoException(GuiaBolsoError error, String message, Object ... args) {
        super(String.format(message, args));
        this.error = error;
    }

    /**
     * Get the origin of the error, used to retrieve the status code.
     *
     * @return the error that originated the exception.
     */
    public GuiaBolsoError error() {
        return error;
    }
}
