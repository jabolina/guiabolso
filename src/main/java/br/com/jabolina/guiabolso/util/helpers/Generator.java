package br.com.jabolina.guiabolso.util.helpers;

import br.com.jabolina.guiabolso.util.annotation.RandomData;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Generator {
    private final Faker faker;

    public Generator() {
        this.faker = new Faker();
    }

    /**
     * Generate a new instance for the given class type, the values will be populated
     * randomly on the fields that are annotated with {@link RandomData}, the values
     * will follow the pattern defined by the regex.
     *
     * @param clazz: class to be created.
     * @param <T>: type of the class.
     * @return a new instance with populated values.
     * @throws IllegalAccessException: if not possible to create a new instance.
     * @throws InstantiationException: if not possible to create a new instance.
     */
    public <T> T generate(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T instance = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            Object value = generateDataForField(field);
            if (!field.isAccessible() && value != null) {
                field.setAccessible(true);
                field.set(instance, value);
                field.setAccessible(false);
            }
        }
        return instance;
    }

    /**
     * For the given field, verify if an annotation is present.
     * Given the annotation parameters, generate a random value, if the annotation
     * is not present than returns null.
     *
     * @param field: field to be verified.
     * @return the generated data or null.
     */
    private Object generateDataForField(Field field) {
        Annotation[] annotations = field.getAnnotationsByType(RandomData.class);
        if (annotations.length == 0) {
            log.debug("Not found annotation for [{}]", field.getName());
            return null;
        }

        RandomData annotation = (RandomData) annotations[0];

        if (annotation.type().equals(RandomData.Type.TEXT)) {
            int tries = 0;
            int min = annotation.min();
            if (min == Integer.MIN_VALUE) {
                min = 0;
            }

            // This is a workaround for creating understandable sentences.
            // Since the faker.lorem().characters do not produce legible sentences,
            // we must use this method and create something that follow the configured
            // sizes. If we do not generated anything valid on 50 tries, we give up and
            // use something that is not legible.
            String value = faker.lorem().sentence(1);
            final int maxTries = 50;
            boolean isSizeIncorrect = value.length() < min || value.length() > annotation.max();
            while (isSizeIncorrect && tries < maxTries) {
                value = faker.lorem().sentence(1);
                isSizeIncorrect = value.length() < min || value.length() > annotation.max();
                tries += 1;
            }

            if (value.isEmpty() || isSizeIncorrect) {
                value = faker.lorem().characters(min, annotation.max(), true);
            }

            return value;
        }

        // Create dates that is at most roughly 3 years old.
        if (annotation.type().equals(RandomData.Type.DATE)) {
            return faker.date().past(3 * 365, TimeUnit.DAYS);
        }

        // Generate only long values, see that this will fail for other primitive types.
        return (long) faker.number().numberBetween(annotation.min(), annotation.max());
    }
}
