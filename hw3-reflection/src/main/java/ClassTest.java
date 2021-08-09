import ru.otus.annotation.Before;
import ru.otus.annotation.Test;
import ru.otus.annotation.After;

public class ClassTest {

    @Before
    void before() {
        System.out.println("run Before");
    }

    @Test
    void test1() {
        System.out.println("run test1");
    }

    @Test
    void test2() {
        System.out.println("run test2");
    }

    @After
    void after() {
        System.out.println("run after" + "\n");
    }
}