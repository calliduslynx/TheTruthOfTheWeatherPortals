package de.mast.ttotwp;

import java.time.LocalDateTime;

public class Entry {
    public LocalDateTime timeOfRecord = Main.centralTimeOfRecord;
    public String        site;
    public String        location;
    public LocalDateTime targetTime;
    public String        targetTemp;
    public String        targetRainPercent;
    public String        targetRainVolume;
}
