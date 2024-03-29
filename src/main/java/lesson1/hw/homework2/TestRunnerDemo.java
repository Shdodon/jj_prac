package lesson1.hw.homework2;

public class TestRunnerDemo {
    public static void main(String[] args) {
        TestRunner.run(TestRunnerDemo.class);
    }

    @BeforeAll
    void beforeAll() {
        System.out.println("BeforeAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BeforeEach");
    }

    @AfterAll
    void afterAll() {
        System.out.println("AfterAll");
    }

    @AfterEach
    void afterEach() {
        System.out.println("AfterEach");
    }


    @Test(order = 3)
    public void test1() {
        System.out.println("test1");
    }

    @Test(order = 1)
    public void test2() {
        System.out.println("test2");
    }

    @Test()
    void test3() {
        System.out.println("test3");
    }
}

