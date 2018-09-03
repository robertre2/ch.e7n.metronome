package ch.e7n.metronome.parser;

/**
 * Created by robertre on 9/6/14.
 */
class RepeatIterator implements MusicIterator{

    private final int repeat;
    private final MusicObject object;
    private final MusicIterator it;

    public RepeatIterator(int repeat, MusicObject object) {
        this.object = object;
        this.repeat = repeat;
        this.it = object.getIterator();
    }

    private RepeatIterator(int repeat, MusicObject object, MusicIterator it) {
        this.repeat = repeat;
        this.object = object;
        this.it = it;
    }

    @Override
    public ConcreteMusicObject get() {
        return it.get();
    }

    @Override
    public boolean hasNext() {
        return repeat > 1 || it.hasNext();
    }

    @Override
    public MusicIterator next() {
        if(it.hasNext()) {
          return new RepeatIterator(repeat, object, it.next());
        } else if(repeat > 1) {
            return new RepeatIterator(repeat-1, object);
        } else {
            throw new RuntimeException("Cannot go past last element");
        }
    }
}
