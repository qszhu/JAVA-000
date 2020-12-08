package io.github.qszhu.datasource1;

public class DBContextHolder {
    private static ThreadLocal<DBType> CONTEXT = new ThreadLocal<>();

    public static void set(DBType dbType) {
        CONTEXT.set(dbType);
    }

    public static DBType get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
