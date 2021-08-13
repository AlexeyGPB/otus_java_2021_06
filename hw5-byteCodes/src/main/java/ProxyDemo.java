public class ProxyDemo {
    public static void main(String[] args) {
        TestLogging myClass = Ioc.createMyClass();
        myClass.calculation (5);
        myClass.calculation (5,5);
        myClass.calculation (5,5,"-addition of param");
        myClass.calculation();
        myClass.withoutLog();
    }
}
