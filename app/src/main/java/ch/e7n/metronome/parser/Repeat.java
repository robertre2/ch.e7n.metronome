package ch.e7n.metronome.parser;

/**
 * Created by robertre on 9/2/14.
 */
public class Repeat implements MusicObject {
    MusicObject mo;
    int times;

    public Repeat(MusicObject mo, int times) {
        this.mo = mo;
        this.times = times;
    }

    @Override
    public RepeatIterator getIterator() {
        return new RepeatIterator(times, mo);
    }
}
