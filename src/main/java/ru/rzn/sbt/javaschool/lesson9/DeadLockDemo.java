package ru.rzn.sbt.javaschool.lesson9;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

class FirstClass {

    private ReentrantLock lock = new ReentrantLock();

    void foo(SecondClass b) throws InterruptedException {
//        while (true) {
            if (lock.tryLock()) {
                try {
                    String name = Thread.currentThread().getName();
                    System.out.println(name + " вошел в метод FirstClass.foo()");

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println("Класс FirstClass прерван");
                    }

                    System.out.println(name + " пытается вызвать метод SecondClass.last()");
                    b.last();
                } finally {
                    lock.unlock();
                }
            }
//            Thread.sleep(1000);
//        }
    }

    void last() throws InterruptedException {
//        while (true) {
            if (lock.tryLock()) {
                try {
                    System.out.println("В методе FirstClass.last()");
                } finally {
                    lock.unlock();
                }
            }
//            Thread.sleep(1000);
//        }
    }
}

class SecondClass {

    private ReentrantLock lock = new ReentrantLock();

    void bar(FirstClass a) throws InterruptedException {
//        while (true) {
            if (lock.tryLock()) {
                try {
                    String name = Thread.currentThread().getName();
                    System.out.println(name + " вошел в метод SecondClass.bar()");

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println("Класс SecondClass прерван");
                    }

                    System.out.println(name + " пытается вызвать метод FirstClass.last()");
                    a.last();
                } finally {
                    lock.unlock();
                }
            }
//            Thread.sleep(1000);
//        }
    }

    void last() throws InterruptedException {
//        while (true) {
            if (lock.tryLock()) {
                try {
                    System.out.println("В методе SecondClass.last()");
                } finally {
                    lock.unlock();
                }
            }
//            Thread.sleep(1000);
//        }
    }
}

public class DeadLockDemo {
    public static void main(String args[]) throws InterruptedException {
        FirstClass a = new FirstClass();
        SecondClass b = new SecondClass();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    a.foo(b);
                } catch (InterruptedException e) {

                }
                System.out.println("Возврат из foo");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    b.bar(a);
                } catch (InterruptedException e) {

                }
                System.out.println("Возврат из bar");
            }
        });
        t2.start();
        t1.start();

    }
}
