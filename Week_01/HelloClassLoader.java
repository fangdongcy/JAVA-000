import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义类加载器，加在一个经过x=（255-x）后的字节码文件
 * 期待输出:"Hello, classLoader!"
 * @author FangDong
 * @date 2020-10-21
 */
public class HelloClassLoader extends ClassLoader {

    private static final String path = "/Users/bigmao/learnJava/JAVA-000/Week_01/Hello.xlass";

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1];
            byte[] antiBytes = new byte[1];
            byte byte255 = (byte) 255;
            int len;
            while ((len = fileInputStream.read(bytes)) != -1) {
                antiBytes[0] = (byte) (byte255 - bytes[0]);
                byteArrayOutputStream.write(antiBytes, 0, len);
            }
            byte[] outputBytes = byteArrayOutputStream.toByteArray();
            return defineClass(name, outputBytes, 0, outputBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }


    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> cls = new HelloClassLoader().findClass("Hello");
        Method hello = cls.getDeclaredMethod("hello");
        hello.setAccessible(true);
        hello.invoke(cls.newInstance());
    }
}
