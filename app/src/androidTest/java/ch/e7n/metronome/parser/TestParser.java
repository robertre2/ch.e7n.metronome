package ch.e7n.metronome.parser;

import android.app.Application;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.junit.Test;

/**
 * Created by robertre on 9/2/14.
 */
public class TestParser extends AndroidTestCase {

    @Test
    public void testParser() {
        // Parser p = new Parser("t6/8 n1/8*4 r1/4 n12/18r1/1*2");
        // Parser p = new Parser("((n1/2 n1/4 )*3n1/8)*2");
        Parser p = new Parser("n1/4~n1/4~n1/4~n1/4 r1/6~r1/4*2");
        MusicObject o = p.parse();
        MusicIterator it = o.getIterator();
        while(true) {
            ConcreteMusicObject cmo = it.get();
            Log.v("***Parser***", cmo.display());
            if(it.hasNext()) {
                it = it.next();
            } else {
                break;
            }
        }
    }
}
