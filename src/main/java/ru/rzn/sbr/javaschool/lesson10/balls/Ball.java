package ru.rzn.sbr.javaschool.lesson10.balls;

import java.awt.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Ball implements Runnable {

    BallWorld world;

    private volatile boolean visible = false;

    private int xpos, ypos, xinc, yinc;

    private final Color col;

    private final static int BALLW = 10;
    private final static int BALLH = 10;

    private Boolean isBreak = false;

    public Boolean getBreak() {
        return isBreak;
    }

    public void setBreak(Boolean aBreak) {
        isBreak = aBreak;
    }

    public Ball(BallWorld world, int xpos, int ypos, int xinc, int yinc, Color col) {

        this.world = world;
        this.xpos = xpos;
        this.ypos = ypos;
        this.xinc = xinc;
        this.yinc = yinc;
        this.col = col;

        world.addBall(this);
    }

    @Override
    public void run() {
        this.visible = true;
        while(true) {
            try {
                while (true) {
                    move();
                }
            } catch (InterruptedException e) {
                if (isBreak) {
                    int n = barrier.getNumberWaiting();
                    if (n > barrier.getParties()-1) {n = barrier.getParties()-1;}
                    barrier.reset();
                    barrier = new CyclicBarrier(barrier.getParties()-1);
                    for (int i=0; i<n; i++) {
                        try {
                            barrier.await();
                        } catch (InterruptedException e1) {
                            //e1.printStackTrace();
                        } catch (BrokenBarrierException e1) {
                            //e1.printStackTrace();
                        }
                    }
                }
                else {
                    try {
                        barrier.await();
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        //e1.printStackTrace();
                    } catch (BrokenBarrierException e1) {
                       //e1.printStackTrace();
                    }
                }
            }
        }
    }

    private static CyclicBarrier barrier = new CyclicBarrier(4);

    public void move() throws InterruptedException {
        if (xpos >= world.getWidth() - BALLW || xpos <= 0) xinc = -xinc;

        if (ypos >= world.getHeight() - BALLH || ypos <= 0) yinc = -yinc;

        Thread.sleep(30);
        doMove();
        world.repaint();
    }

    public synchronized void doMove() throws InterruptedException {
        xpos += xinc;
        ypos += yinc;
        for (int i = xpos; i<=xpos+BALLW/4; i++) {
            for (int j = ypos; j<=ypos+BALLH/4; j++) {
                if (i==j) {
                    throw new InterruptedException();
                }
            }
        }

    }

    public synchronized void draw(Graphics g) {
        if (visible) {
            g.setColor(col);
            g.fillOval(xpos, ypos, BALLW, BALLH);
        }
    }
}
