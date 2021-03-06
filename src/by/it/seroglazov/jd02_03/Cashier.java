package by.it.seroglazov.jd02_03;

import java.util.concurrent.atomic.AtomicBoolean;

public class Cashier implements Runnable {

    private final Shop shop;
    //private Thread thread;
    private AtomicBoolean endWork = new AtomicBoolean(false);
    private boolean isWaiting = false;

    public String getName() {
        return name;
    }

    private final String name;

    int getNumber() {
        return number;
    }

    private final int number;
    //private final Object monitor = new Object();

    Cashier(int num, Shop shop) {
        this.shop = shop;
        number = num;
        name = "CashierN" + num;
        //thread = new Thread(this, "CashierN" + num);
        //thread.start();
    }

    @Override
    public void run() {
        while (!endWork.get()) {
            Buyer buyer = shop.takeFromLine();
            if (buyer != null) {
                int len = shop.lineLength();
                service(buyer, len);
            } else {
                if (Runner.FULL_LOG) System.out.println(this + " касса закрылась");
                pause();
            }
        }
    }

    // Обслуживание покупателя
    private void service(Buyer b, int lineLength) {
        b.lock.lock();
        try {
            if (Runner.FULL_LOG) System.out.println(this + " обслуживает " + b);
            shop.check(b, this, lineLength);
            SleepCases.sleepRandom(2000, 5000);
            b.setFalseStayingInLine();
            b.servedByCashier.signal();
        } finally {
            b.lock.unlock();
        }

    }

    @Override
    public String toString() {
        return "   " + getName();
    }

    synchronized void endOfWorkDay() {
        endWork.set(true);
        isWaiting = false;
        notify();
    }

    private synchronized void pause() {
        isWaiting = true;
        while (isWaiting) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("InterruptedException " + e.getMessage());
            }
        }
    }

    // Разбуть кассира если он отдыхает и заставить работать
    // Возвращает true если кассир отдыхал
    synchronized boolean wakeUp() {
        if (isWaiting) {
            isWaiting = false;
            notify();
            if (Runner.FULL_LOG) System.out.println(this + " приступил к работе!");
            return true;
        } else return false;
    }


    synchronized boolean isWaiting() {
        return isWaiting;
    }

    /*Thread getThread(){
        return thread;
    }*/
}
