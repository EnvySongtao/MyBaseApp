package com.gst.javatest.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

public class Clazz {
    protected String lock = "synchronizedLock";//不同对象 的lock 相同
    private int countLock =10;
    private Collection<String> collection;
    private List<String> list;
    private Vector<String> vector;
    private ArrayList<String> arrayList;
    private LinkedList<String> linkedList;
    private Iterator<String> iterator;
    private ListIterator<String> listIterator;
    String name = "";

    public Clazz(String name) {
        this.name = name;
        lock =new String("synchronizedLock") ;//不同对象 的lock 不相同
//        lock ="synchronizedLock" ;//不同对象 的lock 相同
    }

    /*属性锁不能修饰method*/
    public void testProperty1() {
        synchronized (lock) {
            System.out.println(Clazz.this.name + "  testProperty1():");
            playMinus();
        }
    }

    /*对象锁实现*/
    public void testObject1() {
        synchronized (Clazz.this) {
            System.out.println(Clazz.this.name + "  testObject1():");
            playMinus();
        }
    }

    public synchronized void testObject2() {
        System.out.println(Clazz.this.name + "  testObject2():");
        playMinus();
    }

    /*类锁实现 要求方法必须是静态*/
    public static void testClass1() {
        synchronized (Clazz.class) {
            System.out.println(Clazz.class.getName() + "  testClass1():");
            playMinus();
        }
    }

    public static synchronized void testClass2() {
        System.out.println(Clazz.class.getName() + "  testClass2():");
        playMinus();
    }


    public static void playMinus() {

        int count = 5;
        for (int i = 0; i < 5; i++) {
            count--;
            System.out.println(Thread.currentThread().getName() + " - " + count);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

}
