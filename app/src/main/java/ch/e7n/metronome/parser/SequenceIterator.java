package ch.e7n.metronome.parser;

import java.util.List;

/**
 * Created by robertre on 9/6/14.
 */
class SequenceIterator implements MusicIterator {

    private final int position;
    private final List<MusicObject> list;
    private final MusicIterator it;

    public SequenceIterator(int position, List<MusicObject> list) {
        this.position = position;
        this.list = list;
        this.it = list.get(position).getIterator();
    }

    private SequenceIterator(int position, List<MusicObject> list, MusicIterator it) {
        this.position = position;
        this.list = list;
        this.it = it;
    }

    @Override
    public ConcreteMusicObject get() {
        return it.get();
    }

    @Override
    public boolean hasNext() {
        return (position+1) < list.size() || it.hasNext();
    }

    @Override
    public MusicIterator next() {
        if(it.hasNext()) {
          return new SequenceIterator(position, list, it.next());
        } else if((position+1) < list.size()) {
            return new SequenceIterator(position+1, list);
        } else {
            throw new RuntimeException("Cannot go past last element");
        }
    }
}
