package de.mast.ttotwp;

import java.net.*;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

public abstract class Crawler {
    public abstract void addTasks(List<Callable<List<Entry>>> tasks);

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
