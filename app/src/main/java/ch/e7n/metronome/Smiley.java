package ch.e7n.metronome;

/**
 * Created by robertre on 8/28/14.
 */
public class Smiley {
    private final int keyCode;
    private final int drawableId;
    private final String text;

    public Smiley(int keyCode, int drawableId, String text) {
        this.keyCode = keyCode;
        this.drawableId = drawableId;
        this.text = text;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public String getText() {
        return text;
    }
}