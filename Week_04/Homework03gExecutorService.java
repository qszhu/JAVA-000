import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Homework03gExecutorService {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        ExecutorService es = Executors.newSingleThreadExecutor();
        String result = es.submit(Homework03gExecutorService::sum).get();

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
        es.shutdown();
    }

    private static String sum() {
        int n = 666666;
        Fib fib = new FibExp();
        return fib.calc(n);
    }
}
