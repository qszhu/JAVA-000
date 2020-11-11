import java.util.concurrent.CountDownLatch;

public class Homework03eCountDownLatch {
    private static String result;

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        CountDownLatch l = new CountDownLatch(1);

        new Thread(() -> {
            result = sum();
            l.countDown();
        }).start();

        l.await();

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
    }

    private static String sum() {
        int n = 666666;
        Fib fib = new FibExp();
        return fib.calc(n);
    }
}
