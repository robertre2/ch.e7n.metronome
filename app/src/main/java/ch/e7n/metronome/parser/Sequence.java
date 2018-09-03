package ch.e7n.metronome.parser;

import java.util.List;

/**
 * Created by robertre on 9/2/14.
 */
public class Sequence implements MusicObject {

    private final List<MusicObject> list;

    public Sequence(List<MusicObject> list) {
        this.list = list;
    }

    @Override
    public MusicIterator getIterator() {
        if(list.size() > 0) {
            return new SequenceIterator(0, list);
        } else {
            return new SingletonIterator(new Rest(0,4));
        }
    }
}
