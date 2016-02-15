package de.mast.ttotwp;

import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

import de.mast.ttotwp.util.TextStream;

public abstract class WeatherService {
    public abstract void addTasks(List<Callable<List<Entry>>> tasks);

    public String getServiceName(){
        return getClass().getSimpleName().replace("_", ".");
    }

    protected abstract class Crawler implements Callable<List<Entry>>{
        private String url;
        private int daysInFuture;

        public Crawler(int daysInFuture, String url) { this.daysInFuture = daysInFuture; this.url = url; }

        @Override
        public List<Entry> call() throws Exception {
            String htmlContent = getHtmlContent(url);
            List<Entry> list = new ArrayList<>();
            parse(new TextStream(htmlContent), list);
            return list;
        }

        protected abstract void parse(TextStream stream, List<Entry> list);

        protected Entry getEntry(int hour) {
            Entry entry = new Entry();
            entry.site = getServiceName();
            entry.location = "Angermünde";
            entry.targetTime = LocalDateTime.now()
                    .plusDays(daysInFuture)
                    .withHour(hour)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
            return entry;
        }

        protected String getHtmlContent(String urlAdress) throws Exception{
            Log.debug("lade " + urlAdress);
            HttpURLConnection conn = (HttpURLConnection) new URL(urlAdress).openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            String html = IOUtils.toString(conn.getInputStream());
            conn.disconnect();
            Log.debug("fertig");
            return html;
        }
    }
}
