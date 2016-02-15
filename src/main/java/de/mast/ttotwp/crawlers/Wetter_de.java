package de.mast.ttotwp.crawlers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;

import de.mast.ttotwp.*;
import de.mast.ttotwp.crawlers.util.TextStream;

public class Wetter_de extends Crawler {

    @Override
    public void addTasks(List<Callable<List<Entry>>> tasks) {
        tasks.add(new Wetter_de_Crawler(0, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/wetterbericht-aktuell.html"));
        tasks.add(new Wetter_de_Crawler(1, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/wetterbericht-morgen.html"));
        tasks.add(new Wetter_de_Crawler(2, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/wetterbericht-uebermorgen.html"));
        tasks.add(new Wetter_de_Crawler(3, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/wetter-bericht.html"));
        tasks.add(new Wetter_de_Crawler(4, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/wettervorhersage.html"));
        tasks.add(new Wetter_de_Crawler(5, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/wetter-vorhersage.html"));
        tasks.add(new Wetter_de_Crawler(6, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/wettervorschau.html"));
        tasks.add(new Wetter_de_Crawler(7, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/wetter-vorschau.html"));
        tasks.add(new Wetter_de_Crawler(8, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/tag-9.html"));
        tasks.add(new Wetter_de_Crawler(9, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/tag-10.html"));
        tasks.add(new Wetter_de_Crawler(10, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/tag-11.html"));
        tasks.add(new Wetter_de_Crawler(11, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/tag-12.html"));
        tasks.add(new Wetter_de_Crawler(12, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/tag-13.html"));
        tasks.add(new Wetter_de_Crawler(13, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/tag-14.html"));
        tasks.add(new Wetter_de_Crawler(14, "http://www.wetter.de/deutschland/wetter-angermuende-18230230/tag-15.html"));
    }

    private class Wetter_de_Crawler implements Callable<List<Entry>> {
        private String url;
        private int daysInFuture;

        Wetter_de_Crawler(int daysInFuture, String url) {
            this.daysInFuture = daysInFuture;
            this.url = url;
        }

        @Override
        public List<Entry> call() throws Exception {
            List<Entry> list = new ArrayList<>();
            String htmlContent = getHtmlContent(url);
            TextStream stream = new TextStream(htmlContent);

            while (true) {
                boolean exists = stream.goBehind("<div class=\"forecast-date wt-font-semibold\">");
                if (!exists) break;

                String time = stream.getUntil("</div>").substring(0, 2); // read hour
                if("24".equals(time)) continue;
                Entry entry = getEntry(Integer.parseInt(time));

                stream.goBehind("<span class=\"temperature\">");
                entry.targetTemp = stream.getUntil("&deg;</span>");

                stream.goBehind("<span>Risiko</span>");
                stream.goBehind("<span class=\"wt-font-semibold\">");
                entry.targetRainPercent = stream.getUntil("%</span>");

                if("0".equals(entry.targetRainPercent)){
                    entry.targetRainVolume = "0";
                }else{
                    stream.goBehind("<span class=\"wt-font-semibold\">");
                    entry.targetRainVolume = stream.getUntil(" l/m²</span>");     // z.B. "0,04"
                }

                list.add(entry);
            }

            return list;
        }

        private Entry getEntry(int hour) {
            Entry entry = new Entry();
            entry.site = "wetter.de";
            entry.location = "Angermünde";
            entry.targetTime = LocalDateTime.now()
                    .plusDays(daysInFuture)
                    .withHour(hour)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
            return entry;
        }
    }
}
