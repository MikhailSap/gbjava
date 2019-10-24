import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RunTest {

    public void start(Class testClass) throws Exception{

        List<Annotation> annotationBefore = new ArrayList<Annotation>();
        List<Annotation> annotationAfter = new ArrayList<Annotation>();
        List<Annotation> annotationTest = new ArrayList<Annotation>();
        Method[] declaredMethods = testClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            Annotation annotation = method.getDeclaredAnnotations()[0];
            if (annotation.annotationType().equals(BeforeSuite.class))
                annotationBefore.add(annotation);

            if (annotation.annotationType().equals(AfterSuite.class))
                annotationAfter.add(annotation);

            if (annotation.annotationType().equals(Test.class))
                annotationTest.add(annotation);
        }

        if (annotationBefore.size() > 1 || annotationAfter.size() >1)
            throw new RuntimeException();



        for (Method method : declaredMethods) {
            Annotation annotation = method.getDeclaredAnnotations()[0];
            if (annotation.annotationType().equals(BeforeSuite.class))
                method.invoke(new TestClass());
        }


        List<Integer> priorityes = annotationTest.stream()
                .map(annotation -> ((Test)annotation).priority())
                .sorted()
                .collect(Collectors.toList());




    }
}
