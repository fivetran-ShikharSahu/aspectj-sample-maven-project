package mcprol.aspectj.timewatch;

public class Driver {


    @Watcher
    public static void main(String[] args) throws InterruptedException {
        System.out.println("say hii from driver");
        Driver obj = new Driver();
        obj.test();
        obj.test1();
        obj.test2();
    }

    @ExecutionTimeAnnotation
    public void test() throws InterruptedException {
        System.out.println("say hii from test");
        Thread.sleep(2000);
        test3();
    }

    @ExecutionTimeAnnotation
    public void test1() throws InterruptedException {
        System.out.println("say hii from test1");
        Thread.sleep(3000);
    }

    @ExecutionTimeAnnotation
    public void test2() throws InterruptedException {
        System.out.println("say hii from test2");
        Thread.sleep(4000);
    }

    @ExecutionTimeAnnotation
    public void test3() throws InterruptedException {
        System.out.println("say hii from test3");
        Thread.sleep(5000);
    }
}
