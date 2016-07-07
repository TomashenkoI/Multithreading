import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 7 on 04.07.2016.
 */
public class SynchronizedExample {

    private int counter;

    public static void main(String[] args) throws InterruptedException {
        SynchronizedExample synchronizedExample = new SynchronizedExample();
        synchronizedExample.test();
    }

    public int increment () {
        synchronized (this) {
            return counter++;
        }
    }

    public void test () throws InterruptedException {
        List<Aggregator> aggregators = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Aggregator aggregator = new Aggregator();
            aggregators.add(aggregator);
            new Thread(aggregator).start();
        }

        Thread.sleep(100);

        Set<Integer> integerSet = new HashSet<>();
        for (Aggregator aggregator : aggregators) {
            for (Integer anInt : aggregator.ints) {
                if (!integerSet.add(anInt)) {
                    System.out.println("Error, duplicate found :" + anInt);
                }
            }
        }
    }

    public class Aggregator implements Runnable {
        private List<Integer> ints = new ArrayList<>();

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                increment();
                ints.add(increment());
            }
        }
    }
}
