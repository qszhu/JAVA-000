import java.math.BigInteger;

public class FibExp implements Fib {
    @Override
    public String calc(int n) {
        BigInteger[] m = new BigInteger[]{BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO};
        m = exp(m, n);
        return m[0].toString();
    }

    private static BigInteger[] exp(BigInteger[] a, int p) {
        if (p == 0) return new BigInteger[]{BigInteger.ONE, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ONE};
        if ((p & 1) == 1) return mul(a, exp(a, p - 1));
        BigInteger[] m = exp(a, p / 2);
        return mul(m, m);
    }

    private static BigInteger[] mul(BigInteger[] a, BigInteger[] b) {
        return new BigInteger[]{
                a[0].multiply(b[0]).add(a[1].multiply(b[2])),
                a[0].multiply(b[1]).add(a[1].multiply(b[3])),
                a[2].multiply(b[0]).add(a[3].multiply(b[2])),
                a[2].multiply(b[1]).add(a[3].multiply(b[3])),
        };
    }
}
