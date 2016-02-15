package de.mast.ttotwp.crawlers;

import java.util.List;
import java.util.concurrent.Callable;

import de.mast.ttotwp.*;
import de.mast.ttotwp.util.TextStream;

public class Wetter_de extends WeatherService {

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

    private class Wetter_de_Crawler extends Crawler {
        Wetter_de_Crawler(int daysInFuture, String url) { super(daysInFuture, url); }

        @Override
        protected void parse(TextStream stream, List<Entry> list) {
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
        }
    }
}
