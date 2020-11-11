import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Homework03kForkJoinPool {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        ForkJoinPool fjp = new ForkJoinPool(1);
        Integer result = fjp.invoke(new FibTask(36));

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
    }

    static class FibTask extends RecursiveTask<Integer> {
        final int n;

        FibTask(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n < 2) return n;

            FibTask f1 = new FibTask(n - 1);
            f1.fork();

            FibTask f2 = new FibTask(n - 2);

            return f2.compute() + f1.join();
        }
    }
}
