package mcprol.aspectj.timewatch;

public class Driver {

    @ExecutionTimeAnnotation
    public static void main(String[] args) {
        System.out.println("say hii from driver");
    }
}
