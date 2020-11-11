import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Homework03cCondition {
    private static String result;

    static class MyLock {
        private final Lock l = new ReentrantLock();
        private final Condition c = l.newCondition();
        private volatile boolean finished = false;

        void myWait() throws InterruptedException {
            l.lock();
            try {
                while (!finished) {
                    c.await();
                }
            } finally {
                l.unlock();
            }
        }

        void myNotify() {
            l.lock();
            try {
                finished = true;
                c.signal();
            } finally {
                l.unlock();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        MyLock l = new MyLock();

        new Thread(() -> {
            result = sum();
            l.myNotify();
        }).start();

        l.myWait();

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
