import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class TestCase01 {
    public static void main(String[] args) throws Exception {
        // Get the method name from args
        String methodName = args[0];

        // Get the test01 class
        Class<?> testClass = Class.forName("TestCase01");

        // Get the method with the given name
        Method method = testClass.getDeclaredMethod(methodName);

        // Invoke the method
        method.invoke(null);

    }

    public static void InputTest() {
        char[] testCases = { 'W', 'A', 'S', 'D', 'q', 'r', 'h' };
        char[] negativeTestCases = { 'w', 'a', 's', 'd', 'Q', 'R', 'H', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '0', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '{', '}', '[', ']', '|',
                '\\', ':', ';', '"', '\'', '<', '>', ',', '.', '?', '/', '`', '~', ' ' };
        char[] allCases = Arrays.copyOf(testCases, testCases.length + negativeTestCases.length);
        System.arraycopy(negativeTestCases, 0, allCases, testCases.length, negativeTestCases.length);

        HashMap<Character, Boolean> results = new HashMap<>();
        Thread[] inputThreads = new Thread[allCases.length];

        // save the original output streams
        PrintStream originalOut = System.out;

        for (int i = 0; i < allCases.length; i++) {
            final int index = i;
            inputThreads[i] = new Thread(() -> {
                try {
                    InputStream inputStream = System.in;
                    System.setIn(new ByteArrayInputStream(new byte[] { (byte) allCases[index] }));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    System.setOut(new PrintStream(baos));

                    Sokoban sokoban = new Sokoban();
                    char cleanedChar = sokoban.readValidInput();
                    results.put(allCases[index], cleanedChar == allCases[index]);
                    System.setIn(inputStream);

                } catch (Exception e) {
                    // e.printStackTrace();
                }
            });
        }

        for (int i = 0; i < allCases.length; i++) {
            try {
                inputThreads[i].start();
                inputThreads[i].join(50);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
        System.setOut(originalOut);

        for (int i = 0; i < testCases.length; i++) {
            if (results.containsKey(testCases[i])) {
                System.out.println("InputTest passed for " + testCases[i]);
            } else {
                System.out.println("InputTest failed for " + testCases[i]);
            }
        }

        for (int i = 0; i < negativeTestCases.length; i++) {
            if (results.containsKey(negativeTestCases[i])) {
                System.out.println("InputTest failed for " + negativeTestCases[i]);
            } else {
                System.out.println("InputTest passed for " + negativeTestCases[i]);
            }
        }
    }
}

