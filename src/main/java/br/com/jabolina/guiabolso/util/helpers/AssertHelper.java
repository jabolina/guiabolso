package br.com.jabolina.guiabolso.util.helpers;

import org.springframework.util.Assert;

public final class AssertHelper {
    private AssertHelper() { }

    @SuppressWarnings("unchecked")
    public static <T> T isInstance(Class<T> clazz, Object object, String message) {
        Assert.isInstanceOf(clazz, object, message);
        return (T) object;
    }
}
