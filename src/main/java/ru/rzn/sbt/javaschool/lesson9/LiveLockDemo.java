package ru.rzn.sbt.javaschool.lesson9;

class CommonResource {
    private Worker owner;

    public CommonResource (Worker d) {
        owner = d;
    }

    public Worker getOwner () {
        return owner;
    }

    public synchronized void setOwner (Worker d) {
        owner = d;
    }
}

class Worker {
    private String name;
    private boolean active;

    public Worker (String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public String getName () {
        return name;
    }

    public boolean isActive () {
        return active;
    }

    public synchronized void work (CommonResource commonResource, Worker otherWorker) {
        while (active) {
            // wait for the resource to become available.
            if (commonResource.getOwner() != this) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //ignore
                }
                continue;
            }

            // If other worker is also active let it do it's work first
            if (otherWorker.isActive()) {
                System.out.println(getName() +
                        " : handover the resource to the worker " +
                        otherWorker.getName());
                commonResource.setOwner(otherWorker);
                continue;
            }

            //now use the commonResource
            System.out.println(getName() + ": working on the common resource");
            active = false;
            commonResource.setOwner(otherWorker);
        }
    }
}

public class LiveLockDemo {
    public static void main (String[] args) {
        final Worker worker1 = new Worker("Worker 1 ", true);
        final Worker worker2 = new Worker("Worker 2", true);

        final CommonResource s = new CommonResource(worker1);

        new Thread(() -> {
            worker1.work(s, worker2);
        }).start();

        new Thread(() -> {
            worker2.work(s, worker1);
        }).start();
    }
}
