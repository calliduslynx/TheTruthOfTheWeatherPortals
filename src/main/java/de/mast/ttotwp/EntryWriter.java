package de.mast.ttotwp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class EntryWriter {
    public static final String NL = "\n";
    StringBuilder b = new StringBuilder();
    private boolean isFirst = true;
    private File file;

    public EntryWriter(String fileNamePattern) {
        String filename = new SimpleDateFormat(fileNamePattern).format(new Date());
        file = new File(filename);
        b.append("[");
    }

    public void write(Entry entry) {
        if (!isFirst) b.append(",");
        isFirst = false;
        b.append(NL);
        b.append("   { "

                + "timeOfRecord : " + entry.timeOfRecord + ", "
                + "site : '" + entry.site + "', "
                + "location : '" + entry.location + "', "
                + "targetTime : " + entry.targetTime + ", "
                + "targetTemp : " + entry.targetTemp + ", "
                + "targetRainPercent : " + entry.targetRainPercent + ", "
                + "targatRainVolume : " + entry.targetRainVolume
                + " }");
    }

    public void close() {
        b.append(NL + "]");
        try {
            FileUtils.write(file, b.toString());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
