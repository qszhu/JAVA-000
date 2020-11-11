import java.math.BigInteger;

public class FibAdd implements Fib {
    @Override
    public String calc(int n) {
        BigInteger a = BigInteger.ONE, b = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            BigInteger t = a;
            a = b;
            b = b.add(t);
        }
        return b.toString();
    }
}
