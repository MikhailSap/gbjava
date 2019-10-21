
import static org.junit.Assert.*;
import org.junit.Test;
import partTwoThree.ArrayMethods;

public class ArrayMethodsTest {

    ArrayMethods arrayMethods = new ArrayMethods();

    @Test
    public void arrayFilterTest_Success() {
        int actuals[] = {1,5,6,4,7,4,0,9,8,4,1,2,3};
        int expecteds[] = {1,2,3};
        assertArrayEquals(expecteds, arrayMethods.arrayFilter(actuals));
    }

    @Test(expected = RuntimeException.class)
    public void arrayFilterTest_ThereIsNoFourNumberInTheArray() {
        int actuals[] = {1,5,6,7,0,9,8,1,2,3};
        arrayMethods.arrayFilter(actuals);
    }

    @Test
    public void arrayFilterTest_OnlyOneFourNumber() {
        int actuals[] = {4};
        int expecteds[] = {};
        assertArrayEquals(expecteds, arrayMethods.arrayFilter(actuals));
    }

    @Test
    public void arrayCheckTest_Success() {
        int actuals[] = {1,4,1,1,1};
        assertTrue(arrayMethods.arrayCheck(actuals));
    }

    @Test
    public void arrayCheckTest_ThereIsNoOneNumberInTheArray() {
        int actuals[] = {4,4};
        assertFalse(arrayMethods.arrayCheck(actuals));
    }

    @Test
    public void arrayCheckTest_ThereIsNoFourNumberInTheArray() {
        int actuals[] = {1,1,1,1};
        assertFalse(arrayMethods.arrayCheck(actuals));
    }

    @Test
    public void arrayCheckTest_ThereIsAnotherNumberInTheArray() {
        int actuals[] = {1,1,1,1,4,0};
        assertFalse(arrayMethods.arrayCheck(actuals));
    }
}
