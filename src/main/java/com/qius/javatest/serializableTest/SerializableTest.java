package com.qius.javatest.serializableTest;


import java.io.*;

/**
 * 测试对象的序列化和反序列化
 */
public class SerializableTest {

    public static void main(String[] args) throws Exception {

        File file = new File("D:\\test\\test.txt");

        // 序列化 写入文件
//        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file));
//        Person person = new Person("Haozi", 22, "上海");
//        oout.writeObject(person);
//        oout.close();
        // 反序列化 根据文件中的内容构造对象
        // 在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体（类）的serialVersionUID进行比较，
        // 如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常。
        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
        Object newPerson = oin.readObject();
        oin.close();
        System.out.println(newPerson);
    }

    /**
     * 定义待序列化的对象
     */
    public static class Person implements Serializable {

        /**
         * 该字段用于指定序列化中的版本标识
         * 若对象在进行了不可兼容的修改后 可通过修改该版本号 之前版本的对象进行反序列化时直接报错 通过该手段 维持序列化对象的版本
         */
        private static final long serialVersionUID = 1L;

        private String name;
        private Integer age;
        private String address;

        public Person() {
        }

        public Person(String name, Integer age, String address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }


        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
}
