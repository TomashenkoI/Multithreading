/**
 * Created by 7 on 05.07.2016.
 */
public class LatchBootstrap {

    public static void main(String[] args) {
        new LatchBootstrap().test();
    }

    private CountDownLatch countDownLatch;

    public void test () {
        countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(new Worker()).start();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (countDownLatch.getCounter() > 0) {
                    countDownLatch.countDown();
                }
            }
        }).start();
    }

    public class Worker implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Thread " + Thread.currentThread().getName() + " start waiting");
                countDownLatch.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " stop waiting");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
