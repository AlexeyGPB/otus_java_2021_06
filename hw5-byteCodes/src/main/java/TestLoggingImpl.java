import ru.otus.annotation.Log;

public class TestLoggingImpl implements TestLogging {
    @Log
    @Override
    public void calculation(int param) {
        System.out.println(param);
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        System.out.println(param1 + param2);
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println(param1 + param2 + param3);
    }

    @Log
    @Override
    public void calculation() {
        System.out.println("calculation - No param");
    }

    @Override
    public void withoutLog(){
        System.out.println("---------------");
        System.out.println("Without Log");
    }
}
