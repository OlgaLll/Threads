package ru.rzn.sbr.javaschool.lesson10.cars;

import java.util.concurrent.Semaphore;

public class TrafficController {
    Semaphore sem = new Semaphore(1);
    public void enterLeft() {
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterRight() {
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leaveLeft() {
        sem.release();
    }

    public void leaveRight() {
        sem.release();
    }
}