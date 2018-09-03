package ch.e7n.metronome;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by robertre on 8/29/14.
 */
public class SmileySpan extends ImageSpan {

    private final Smiley smiley;

    public SmileySpan(Drawable d, int verticalAlignment, Smiley smiley) {
        super(d, verticalAlignment);
        this.smiley = smiley;
    }

    public Smiley getSmiley() {
        return smiley;
    }
}
