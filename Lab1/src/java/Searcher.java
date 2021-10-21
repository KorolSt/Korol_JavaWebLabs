import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Searcher implements Runnable {
    private Path path;
    private ExecutorService pool;
    private CountDownLatch countDownLatch;

    public Searcher(Path path, ExecutorService pool, CountDownLatch countDownLatch) {
        this.path = path;
        this.pool = pool;
        this.countDownLatch = countDownLatch;
    }

    public void fileCheck(Path dir){
        if (dir.toString().toLowerCase().endsWith(".c")) {
            Integer counter = 0;
            Pattern pattern = Pattern.compile("for");
            try (Scanner scanner = new Scanner(new FileInputStream(dir.toFile()))) {
                while (scanner.hasNextLine()) {
                    String str = scanner.nextLine();
                    Matcher matcher = pattern.matcher(str);
                    while (matcher.find()) {
                        counter++;
                    }
                }
                System.out.println(dir.getFileName() + ": " + counter);
            } catch (FileNotFoundException | NullPointerException e) {
                System.err.println("Can`t get this directory, try another");
            }
        }
    }

    @Override
    public void run() {
        File[] files = path.toFile().listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                Searcher searcher = new Searcher(file.toPath(), pool, countDownLatch);
                pool.execute(searcher);
                countDownLatch.countDown();
            } else {
                fileCheck(file.toPath());
            }
        }

    }
}
