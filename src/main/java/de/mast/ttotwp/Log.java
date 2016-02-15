package de.mast.ttotwp;

public class Log {
    public static final void debug(Object o){  print("[DEBUG]  " + o); }
    public static final void info(Object o) {  print("[INFO]   " + o); }

    private static final void print(Object o){
        String id = "" + Thread.currentThread().getId();
        while(id.length() < 4) id = " " + id;
        System.out.println("Thead: " + id + " " + o);
    }
}
