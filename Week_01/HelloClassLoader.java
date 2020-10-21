import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        Class<?> Hello = new HelloClassLoader().findClass("Hello");
        Object helloObj = Hello.newInstance();
        Method hello = Hello.getMethod("hello");
        hello.invoke(helloObj);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String path = String.format("%s.xlass", name);
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (0xFF - bytes[i]);
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Class file read error", e);
        }
    }

}
