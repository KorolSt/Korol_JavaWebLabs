
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter directory -> ");
        Path dir = Paths.get(sc.next());
        try {
            CountDownLatch cdl = new CountDownLatch(Objects.requireNonNull(dir.toFile().list()).length);
            ExecutorService executorService = Executors.newFixedThreadPool(9);
            Searcher searcher = new Searcher(dir, executorService, cdl);
            executorService.execute(searcher);
            cdl.await();
            executorService.shutdown();
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted during CountDownLatch waiting");
        } catch (NullPointerException e){
            System.err.println("Can`t get this file, try another path");
        }
    }
}
