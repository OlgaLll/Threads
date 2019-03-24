package ru.rzn.sbr.javaschool.lesson10.balls;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.*;
import javax.swing.*;

/**
 * 1. Измените метода {@link Solution#main(String[])} таким образом, чтобы вместо явного создания потоков использовался
 * какой-нибудь {@link java.util.concurrent.Executor}.
 * 2. Реализуйте последовательую "заморозку" потоков при попадании {@link Ball} на диагональ {@link BallWorld}
 * (где x == y). Попаданием считать пересечение описывающего прямоуголькника диагонали. При заморозке всех потоков
 * осуществляйте возобновление выполнения
 * 3. Введите в программу дополнительный поток, который уничтожает {@link Ball} в случайные моменты времени.
 * Начните выполнение этого потока c задержкой в 15 секунд после старта всех {@link Ball}. {@link Ball} должны
 * уничтожаться в случайном порядке. Под уничтожением {@link Ball} подразумевается
 * а) исключение из массива {@link BallWorld#balls} (нужно реализовать потокобезопасный вариант)
 * б) завершение потока, в котором выполняется соответствующая задача (следует использовать
 * {@link java.util.concurrent.Future}сформированный при запуске потока для прерывания
 * {@link java.util.concurrent.Future#cancel(boolean)})
 */
public class Solution {

    public static void nap(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println("Thread " + Thread.currentThread().getName() + " throwed exception " + e.getMessage());
        }
    }

    public static void main(String[] a) {

        final BallWorld world = new BallWorld();
        final JFrame win = new JFrame();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                win.getContentPane().add(world);
                win.pack();
                win.setVisible(true);
            }
        });

        Thread.currentThread().setName("MyMainThread");

        ExecutorService tpe = Executors.newFixedThreadPool(5);
        ArrayList<Ball> bm = new ArrayList<>();
        Ball b1 = new Ball(world, 50, 80, 5, 10, Color.red);
        Ball b2 = new Ball(world, 70, 100, 8, 6, Color.blue);
        Ball b3 = new Ball(world, 150, 100, 9, 7, Color.green);
        Ball b4 = new Ball(world, 200, 130, 3, 8, Color.black);
        bm.add(b1);
        bm.add(b2);
        bm.add(b3);
        bm.add(b4);

        ArrayList<FutureTask> fm = new ArrayList<>();
        FutureTask fut1 = new FutureTask<>(b1,1);
        FutureTask fut2 = new FutureTask<>(b2,2);
        FutureTask fut3 = new FutureTask<>(b3,3);
        FutureTask fut4 = new FutureTask<>(b4,4);
        fm.add(fut1);
        fm.add(fut2);
        fm.add(fut3);
        fm.add(fut4);

        nap((int) (5000 * Math.random()));
        //new Thread(new Ball(world, 50, 80, 5, 10, Color.red)).start();
        tpe.execute(fut1);
        nap((int) (5000 * Math.random()));
        //new Thread(new Ball(world, 70, 100, 8, 6, Color.blue)).start();
        tpe.execute(fut2);
        nap((int) (5000 * Math.random()));
        //new Thread(new Ball(world, 150, 100, 9, 7, Color.green)).start();
        tpe.execute(fut3);
        nap((int) (5000 * Math.random()));
        //new Thread(new Ball(world, 200, 130, 3, 8, Color.black)).start();
        tpe.execute(fut4);
        //nap((int) (5000 * Math.random()));
        nap(15000);
        tpe.execute(new BallRemove(fm, bm));
    }
}
