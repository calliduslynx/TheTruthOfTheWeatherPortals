package de.mast.ttotwp;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import de.mast.ttotwp.crawlers.Wetter_de;

public class Main {
    public static final int NUMBER_OF_THREADS = 500;

    public static void main(String[] args) throws Exception {
        List<Crawler> crawlers = new ArrayList<>();
        crawlers.add(new Wetter_de());

        List<Callable<List<Entry>>> tasks = new ArrayList<>();
        crawlers.forEach(crawler -> crawler.addTasks(tasks));

        Entry.centralTimeOfRecord = LocalDateTime.now();

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        List<Future<List<Entry>>> futures = executor.invokeAll(tasks, 60, SECONDS);

        List<Entry> entries = new ArrayList<>();
        futures.stream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).filter(list -> list != null)
          .forEach(list -> entries.addAll(list));
        executor.shutdown();

        EntryWriter writer = new EntryWriter("'/tmp/result_'yyyy-MM-dd__HH-mm-ss'.json'");
        entries.forEach(entry -> writer.write(entry));
        writer.close();
    }
}
