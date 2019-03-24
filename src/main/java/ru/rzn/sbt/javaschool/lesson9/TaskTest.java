package ru.rzn.sbt.javaschool.lesson9;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Task implements Runnable {
    @Override
    public void run() {
        // вывести текст на экран
        try {
            Thread.sleep(1000);
        } catch (InterruptedException u) {

            System.out.println(Thread.currentThread() + " inter");
        }
        System.out.println(Thread.currentThread() + "zzzz");
    }
}


public class TaskTest {
    public static void main(String[] args)
            throws Exception {
        Runnable task = new Task();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);
        for( int i = 0; i < 100 ; i++) {
//            executorService.schedule(new Task(), 500, TimeUnit.MILLISECONDS);
            executorService.submit(new Task());
        }
        List<Runnable> res = executorService.shutdownNow();
//        executorService.awaitTermination(100000, TimeUnit.MILLISECONDS);
        System.out.println("zzzzz" + res.size());
    }
}

