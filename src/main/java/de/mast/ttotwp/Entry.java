package de.mast.ttotwp;

import java.time.LocalDateTime;

public class Entry {
    public static LocalDateTime centralTimeOfRecord;

    public LocalDateTime timeOfRecord = centralTimeOfRecord;
    public String        site;
    public String        location;
    public LocalDateTime targetTime;
    public String        targetTemp;
    public String        targetRainPercent;
    public String        targetRainVolume;
}
