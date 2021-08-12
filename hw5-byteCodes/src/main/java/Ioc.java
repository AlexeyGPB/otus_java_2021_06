import ru.otus.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Ioc {

    private Ioc() {
    }

    static TestLogging createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging myClass;
        private Set<String> logMethods = new HashSet<>();

        DemoInvocationHandler(TestLogging myClass) {
            this.myClass = myClass;
            methodsWithAnnotations();
        }

        public void methodsWithAnnotations() {
            for (Method method : TestLoggingImpl.class.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    logMethods.add(method.getName() + getParam(method));
                }
            }
        }

        private String getParam(Method method) {
            var paramsTypes = new StringBuilder();
            paramsTypes.append('[');
            for (var paramType : method.getParameterTypes()) {
                paramsTypes.append(paramType);
                paramsTypes.append(',');
            }
            paramsTypes.deleteCharAt(paramsTypes.length() - 1);
            paramsTypes.append(']');
            return paramsTypes.toString();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (logMethods.contains(method.getName() + getParam(method))) {

                if (args != null) {
                    System.out.println("executed method: " + method.getName() + " param: " + Arrays.toString(args));
                } else System.out.println("executed method: " + method.getName() + " param: " + "no params");
            }

            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}