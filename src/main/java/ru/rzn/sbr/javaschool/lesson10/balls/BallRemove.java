package ru.rzn.sbr.javaschool.lesson10.balls;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.FutureTask;

public class BallRemove implements Runnable {

    private ArrayList<FutureTask> fm;
    private ArrayList<Ball> bm;

    public BallRemove(ArrayList<FutureTask> fm, ArrayList<Ball> bm) {
        this.fm = fm;
        this.bm = bm;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            while (bm.size() > 0) {
                Integer i = random.nextInt(10000);
                Thread.sleep(i);
                Integer numF = random.nextInt(bm.size());
                bm.get(numF).setBreak(true);
                bm.get(numF).world.deleteBall(bm.get(numF));
                bm.remove(bm.get(numF));
                fm.get(numF).cancel(true);
                fm.remove(numF);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
