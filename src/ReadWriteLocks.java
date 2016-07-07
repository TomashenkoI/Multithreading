import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by 7 on 06.07.2016.
 */
public class ReadWriteLocks {

    public static final int ARRAY_LENGHT = 10;

    public static void main(String[] args) {
        ConcurrentArray<Integer> array = new ConcurrentArray<>(ARRAY_LENGHT);
        IntStream.range(0,3).forEach((i) -> new Thread(() -> {
            while (true) {
                System.out.println("Reading: " + Arrays.toString(array.read()));
            }
        }).start());
        new Thread(() -> {
            Random random = new Random();
            while (true) {
                Integer[] ints = Stream.generate(random::nextInt).limit(random.nextInt(ARRAY_LENGHT + 1)).toArray(Integer[]::new);
//                int size = random.nextInt(ARRAY_LENGHT + 1);
//                Integer[] ints = new Integer[size];
//                for (int i = 0; i < size; i++) {
//                    ints[i] = random.nextInt();
//                }
                array.write(ints, ARRAY_LENGHT - ints.length);
            }
        }).start();
    }


    public static class ConcurrentArray<T> {

        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private Object[] items;
        private Random random = new Random();

        public ConcurrentArray(int capacity) {
             items = new Object[capacity];
        }

        public void write(T[] values, int index) {
            readWriteLock.writeLock().lock();
            try {
                if (values.length + index > items.length) {
                    throw new RuntimeException("Not enough space");
                }
                System.arraycopy(values, 0, items, index, values.length);
                Thread.sleep(random.nextInt(1000));
                System.out.println("Array updated: " + Arrays.toString(items));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }

        public T[] read () {
            readWriteLock.readLock().lock();
            try {
                Object[] result = Arrays.copyOf(items, items.length);
                Thread.sleep(random.nextInt(100));
                return (T[]) result;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                readWriteLock.readLock().unlock();
            }
        }
    }
}
