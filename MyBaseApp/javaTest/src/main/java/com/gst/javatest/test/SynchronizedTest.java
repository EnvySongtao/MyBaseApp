package com.gst.javatest.test;

public class SynchronizedTest {

    public void test() {
        Clazz test1 = new Clazz("test1");
        Clazz test2 = new Clazz("test2");

        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                //同一对象 使用对象锁 执行时，有对象锁的方法按顺序依次执行，直到前面方法执行完，才执行后面方法
//                test1.testObject1();
//                test1.testObject1();
//                test1.testObject2();
                //不同对象 使用对象锁
//                test1.testObject1();
//                test2.testObject1();
                //同一对象 使用类锁 执行时
//                test1.testClass1();
//                test1.testClass1();
//                test1.testClass2();
                //不同对象 使用类锁
//                test1.testClass1();
//                test2.testClass1();

                test1.testProperty1();
                test1.lock="bac";
                test1.testProperty1();
//                test2.testProperty1();
            }
        });

        Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
//                test1.testObject1();
//                test2.testObject2();
//                test1.testClass1();
//                test2.testClass1();

//                test1.testProperty1();
//                test2.testProperty1();
            }
        });
        System.out.println("test1.lock==test2.lock?" + (test1.lock==test2.lock));
        thread1.start();
        thread2.start();
    }

}
