import java.lang.reflect.Method;

public class test01 {
    public static void main(String[] args) throws Exception {
        // Get the method name from args
        String methodName = args[0];

        // Get the test01 class
        Class<?> testClass = Class.forName("test01");

        // Get the method with the given name
        Method method = testClass.getDeclaredMethod(methodName);

        // Invoke the method
        method.invoke(null);

    }

    public static void TryTest() {
        System.out.println("TryTest");
        System.exit(0);
    }

}