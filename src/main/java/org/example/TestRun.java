package org.example;


import annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRun {
    public static List<String> run(Class<?> testClass) {
        final Object testObject = initTestObject(testClass);
        List<Method> testMethods = new ArrayList<>();
        List<Method> beforeAllMethod = new ArrayList<>();
        List<Method> beforeEachMethod = new ArrayList<>();
        List<Method> afterAllMethod = new ArrayList<>();
        List<Method> afterEachMethod = new ArrayList<>();
        List<String> result = new ArrayList<>();


        for (Method method : testClass.getDeclaredMethods()) {
            if (method.getModifiers() == Modifier.PUBLIC) {
                if (method.getAnnotation(Test.class) != null) {
                    testMethods.add(method);
                } else if (method.getAnnotation(BeforeAll.class) != null) {
                    beforeAllMethod.add(method);
                } else if (method.getAnnotation(BeforeEach.class) != null) {
                    beforeEachMethod.add(method);
                } else if (method.getAnnotation(AfterAll.class) != null) {
                    afterAllMethod.add(method);
                } else if (method.getAnnotation(AfterEach.class) != null) {
                    afterEachMethod.add(method);
                }
            }
        }
        testMethods.sort(Comparator.comparing(m -> m.getAnnotation(Test.class).Order()));
        for (Method method : beforeAllMethod) {
            invoke(testObject, method, result);
        }
        for (Method testMethod : testMethods) {
            for (Method method : beforeEachMethod) {
                invoke(testObject, method, result);
            }

            invoke(testObject, testMethod, result);
            for (Method method : afterEachMethod) {
                invoke(testObject, method, result);
            }
        }
        for (Method method : afterAllMethod) {
            invoke(testObject, method, result);
        }
        return result;

    }

    private static void invoke(Object o, Method method, List<String> result) {
        try {
            method.invoke(o);
        } catch (InvocationTargetException e) {
            if (e.getCause().getClass().equals(AssertionMessage.class)) {
                result.add(method.getName() + ":" + e.getCause().getMessage());
            } else {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Вызов недоступного метода");
        }
    }

    private static Object initTestObject(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора по умолчанию");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать текст класса");


        }
    }
}
