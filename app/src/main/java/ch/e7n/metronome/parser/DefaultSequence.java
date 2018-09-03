package ch.e7n.metronome.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertre on 9/6/14.
 */
public class DefaultSequence extends Sequence {
    public DefaultSequence() {
        super(defaultSequence());
    }

    private static List<MusicObject> defaultSequence() {
        List<MusicObject> res = new ArrayList<MusicObject>();
        res.add(new TimeSignature(4, 4));
        res.add(new Note(1,4));
        res.add(new Note(1,4));
        res.add(new Note(1,4));
        res.add(new Note(1,4));
        return res;
    }
}
