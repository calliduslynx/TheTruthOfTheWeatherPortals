package de.mast.ttotwp;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import de.mast.ttotwp.crawlers.*;

public class Main {
    public static final int NUMBER_OF_THREADS = 500;
    public static final LocalDateTime centralTimeOfRecord = LocalDateTime.now();

    public static void main(String[] args) throws Exception {
        // ***** create list of services
        List<WeatherService> weatherServices = new ArrayList<>();
        weatherServices.add(new Wetter_de());
        weatherServices.add(new Wetter_com());

        // ***** retrieve crawlers from services
        List<Callable<List<Entry>>> tasks = new ArrayList<>();
        weatherServices.forEach(weatherService -> {
            int sizeBefore = tasks.size();
            weatherService.addTasks(tasks);
            Log.info("found weather service '" + weatherService.getServiceName() + "', "
                     + (tasks.size() - sizeBefore) + " crawlers added");
        });

        // ***** start all services
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        List<Future<List<Entry>>> futures = executor.invokeAll(tasks, 60, SECONDS);

        // ***** get all results and shut down
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

        // ***** write results into file
        EntryWriter writer = new EntryWriter("'result_'yyyy-MM-dd__HH-mm-ss'.json'");
        entries.forEach(entry -> writer.write(entry));
        writer.close();
    }
}
