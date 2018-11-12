package com.prft.cif.test.guice.listener;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

/**
 * @author Anant Gowerdhan
 * @since 07/18/2018
 */
public class CIFLog4JTypeListener implements TypeListener {

    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        Class<?> clazz = typeLiteral.getRawType();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == Logger.class) {
                    typeEncounter.register(new CIFLog4JMembersInjector<I>(field));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
