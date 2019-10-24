import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class RunTest {

    public void start(Class testClass) throws Exception{
        TestClass tc = new TestClass();

        List<Method> annotationBefore = new ArrayList<>();
        List<Method> annotationAfter = new ArrayList<>();
        Map<Integer, Method> tests = new TreeMap<>();
        Method[] declaredMethods = testClass.getDeclaredMethods();

        for (Method method : declaredMethods) {
            Annotation annotation = method.getDeclaredAnnotations()[0];
            if (annotation.annotationType().equals(BeforeSuite.class))
                annotationBefore.add(method);

            if (annotation.annotationType().equals(AfterSuite.class))
                annotationAfter.add(method);

            if (annotation.annotationType().equals(Test.class))
                tests.put(((Test)annotation).priority(), method);
        }

        if (annotationBefore.size() > 1 || annotationAfter.size() >1)
            throw new RuntimeException();

        annotationBefore.get(0).invoke(tc);

        tests.forEach((priority, method) -> {
            try {
                method.invoke(tc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        annotationAfter.get(0).invoke(tc);
    }
}
