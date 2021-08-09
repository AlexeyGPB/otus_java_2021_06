import ru.otus.annotation.Before;
import ru.otus.annotation.Test;
import ru.otus.annotation.After;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.lang.annotation.Annotation;
import java.util.stream.Collectors;

public class ClassTestRun {

    private static int pass;
    private static int fail;

    private static List<Method> AnnotionMethods(Method[] methods, Class<? extends Annotation> annotationTestClass) {
        return Arrays.stream(methods).filter(x -> x.isAnnotationPresent(annotationTestClass))
                .collect(Collectors.toList());
    }

    public static void run(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Method[] methodsForTest = clazz.getDeclaredMethods();
            List<Method> beforeMethods = AnnotionMethods(methodsForTest, Before.class);
            List<Method> testMethods = AnnotionMethods(methodsForTest, Test.class);
            List<Method> afterMethods = AnnotionMethods(methodsForTest, After.class);

            for (Method method : testMethods) {
                Object instance = ReflectionHelper.instantiate(clazz);
                try {
                    beforeMethods.stream().map(Method::getName)
                            .forEach(x -> ReflectionHelper.callMethod(instance, x));
                    ReflectionHelper.callMethod(instance, method.getName());
                    pass++;
                } catch (Exception ex) {
                    fail++;
                } finally {
                    afterMethods.stream().map(Method::getName)
                            .forEach(x -> ReflectionHelper.callMethod(instance, x));
                }
            }

            System.out.println("Всего пройдено тестов: " + (pass + fail));
            System.out.println("Успешно пройдено тестов: " + pass);
            System.out.println("Непройденные тесты: " + fail);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}