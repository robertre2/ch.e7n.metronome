package ch.e7n.metronome.parser;

/**
 * Created by robertre on 9/6/14.
 */
class SingletonIterator implements MusicIterator {

    private final ConcreteMusicObject note;

    public SingletonIterator(ConcreteMusicObject note) {
        this.note = note;
    }

    @Override
    public ConcreteMusicObject get() {
        return note;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public MusicIterator next() {
        throw new RuntimeException("No more elements");
    }
}