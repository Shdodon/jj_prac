package lesson1.hw.homework2;

import java.lang.reflect.*;
import java.util.*;


public class TestRunner {

    public static void run(Class<?> testClass) {
        final Object testObj = initTestObj(testClass);
        Map<String, Method> methodMap = getMap(testClass);

        if (methodMap.get("BeforeAll") != null) {
            try {
                methodMap.get("BeforeAll").invoke(testObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> methods = new ArrayList<>(methodMap.keySet().stream().filter(method -> method.contains("Test")).toList());
        methods.sort((method1, method2) -> {
            Test test1 = methodMap.get(method1).getAnnotation(Test.class);
            Test test2 = methodMap.get(method2).getAnnotation(Test.class);
            if (test1 != null && test2 != null) {
                return Integer.compare(test1.order(), test2.order());
            } else if (test1 != null) {
                return -1;
            } else if (test2 != null) {
                return 1;
            } else {
                return 0;
            }
        });

        for (String method : methods) {
            if (methodMap.get("BeforeEach") != null) {
                try {
                    methodMap.get("BeforeEach").invoke(testObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                methodMap.get(method).invoke(testObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            if (methodMap.get("AfterEach") != null) {
                try {
                    methodMap.get("AfterEach").invoke(testObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (methodMap.get("AfterAll") != null) {
            try {
                methodMap.get("AfterAll").invoke(testObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static HashMap<String, Method> getMap(Class<?> testClass) {
        HashMap<String, Method> methodHashMap = new HashMap<>();
        Method[] methods = testClass.getDeclaredMethods();
        int countTestMethod = 1;
        for (Method testMethod : methods) {
            if (testMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
                continue;
            }
            if (testMethod.getAnnotation(BeforeAll.class) != null) {
//                System.out.println("BeforeAll");
                methodHashMap.put("BeforeAll", testMethod);
            }
            if (testMethod.getAnnotation(BeforeEach.class) != null) {
                methodHashMap.put("BeforeEach", testMethod);
//                System.out.println("BeforeEach");

            }
            if (testMethod.getAnnotation(Test.class) != null) {
                methodHashMap.put("Test" + countTestMethod, testMethod);
                countTestMethod++;
//                System.out.println("Test");
            }
            if (testMethod.getAnnotation(AfterEach.class) != null) {
                methodHashMap.put("AfterEach", testMethod);
//                System.out.println("AfterEach");
            }
            if (testMethod.getAnnotation(AfterAll.class) != null) {
                methodHashMap.put("AfterAll", testMethod);
//                System.out.println("AfterAll");
            }
        }
        return methodHashMap;
    }

    private static Object initTestObj(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора по умолчанию");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект тест класса");
        }
    }

}