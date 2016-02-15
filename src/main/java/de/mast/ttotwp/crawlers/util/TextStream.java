package de.mast.ttotwp.crawlers.util;

public class TextStream {
    private String content;
    private int cursor;

    public TextStream(String content) {
        this.content = content;
    }

    public boolean goBehind(String target) {
        int index = content.indexOf(target, cursor);
        cursor = index + target.length();
        return index != -1;
    }

    public String getUntil(String target){
        int index = content.indexOf(target, cursor);
        if(index == -1) return null;
        String result = content.substring(cursor, index);
        cursor = index + target.length();
        return result;
    }

}
