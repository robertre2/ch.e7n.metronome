package ch.e7n.metronome.parser;

/**
 * Created by robertre on 9/6/14.
 */
public interface MusicIterator {
    public ConcreteMusicObject get();
    public boolean hasNext();
    public MusicIterator next();
}
