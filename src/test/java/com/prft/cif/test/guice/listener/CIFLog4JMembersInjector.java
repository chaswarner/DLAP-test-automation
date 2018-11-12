package com.prft.cif.test.guice.listener;

import com.google.inject.MembersInjector;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

/**
 * @author Anant Gowerdhan
 * @param <T>
 * @since 07/18/2018
 */
class CIFLog4JMembersInjector<T> implements MembersInjector<T> {
    private final Field field;
    private final Logger logger;

    /**
     *
     * @param field
     */
    CIFLog4JMembersInjector(Field field) {
        this.field = field;
        this.logger = Logger.getLogger(field.getDeclaringClass());
        field.setAccessible(true);
    }

    /**
     *
     * @param t
     */
    public void injectMembers(T t) {
        try {
            field.set(t, logger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
