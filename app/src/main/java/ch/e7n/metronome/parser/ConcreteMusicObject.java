package ch.e7n.metronome.parser;

/**
 * Created by robertre on 9/6/14.
 */
public interface ConcreteMusicObject extends MusicObject {
    public SingletonIterator getIterator();

    public String display();

    double length();
}
