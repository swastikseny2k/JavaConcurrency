package com.sen.concurrency;

import java.util.concurrent.*;

public class ForkJoinPoolExample {

    private BlockingQueue<Runnable> tasks = new LinkedBlockingDeque<>();

    public static void main(String[]  args) {
        ForkJoinPoolExample fjp = new ForkJoinPoolExample();
        fjp.runForkJoinPool();
    }

    public void runForkJoinPool() {
        tasks.offer(() -> factorial(8)); // add your new runnable task here
        tasks.offer(() -> factorial(6)); // add your new runnable task here
        tasks.offer(() -> factorial(11)); // add your new runnable task here

        new ForkJoinPool(4).execute(new Consumer());

        try {
            Thread.sleep(1000000000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void factorial(int n) {
        int num = n;
        int result = 1;
        while(num>=2) {
            result = result * num--;
        }

        System.out.println("Factorial of " + n + " is : " + result);
    }

    private class Consumer extends RecursiveAction {

        @Override
        protected void compute() {
            try {
                Runnable r = tasks.take(); // the take method of the queue will wait for any tasks to be added in the queue
                new Consumer().fork(); // create a new consumer which will consume the next task recursively
                r.run(); // call run directly as we are using ForkJoinPool's worker
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
