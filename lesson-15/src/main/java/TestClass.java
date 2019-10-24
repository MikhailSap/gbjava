public class TestClass {

    @BeforeSuite
    public void before() {
        System.out.println("before");

    }

    @AfterSuite
    public void after() {
        System.out.println("after");

    }


    @Test
    public void tesOne() {
        System.out.println("one");

    }

    @Test(priority = 3)
    public void testTwo() {
        System.out.println("two");

    }

    @Test(priority = 3)
    public void testThree() {
        System.out.println("Three");

    }


}
