package ch.e7n.metronome;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertre on 8/28/14.
 */
public class Smileys {
    private final Map<Integer, Smiley> keyCodeToSmiley;
    private final Map<String, Smiley> stringToSmiley;

    public Smileys() {
       keyCodeToSmiley = new HashMap<Integer, Smiley>();
       stringToSmiley = new HashMap<String, Smiley>();
    }

    public void add(int keyCode, int drawableId, String text) {
        Smiley s = new Smiley(keyCode, drawableId, text);
        keyCodeToSmiley.put(keyCode, s);
        stringToSmiley.put(text, s);
    }

    public Smiley getFromKeyCode(int keyCode) {
        return keyCodeToSmiley.get(keyCode);
    }

    public Smiley getFromString(String text) {
        return stringToSmiley.get(text);
    }
}

