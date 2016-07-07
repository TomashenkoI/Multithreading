import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 7 on 06.07.2016.
 */
public class Locks {

    private final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Locks locks = new Locks();
//        IntStream.range(0, 10).forEach(i -> new Thread(locks::testTryLock));
        for (int i = 0; i < 10; i++) {
            new Thread(locks::testTryLock).start();
        }
    }

    public void testLock() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " tries lock");
        lock.lock();
        try {
            System.out.println(threadName + " executing critical section");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(threadName + " releasing lock");
            lock.unlock();
        }
    }

    public void testTryLock() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " tries lock");
        try {
            if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
                try {
                    System.out.println(threadName + " executing critical section");
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(threadName + " releasing lock");
                    lock.unlock();
                }
            } else {
                System.out.println(threadName + " Unable acquire lock");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
