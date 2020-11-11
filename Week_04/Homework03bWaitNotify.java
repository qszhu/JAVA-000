public class Homework03bWaitNotify {
    private static String result;

    static class MyLock {
        synchronized void myWait() throws InterruptedException {
            wait();
        }

        synchronized void myNotify() {
            notify();
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
