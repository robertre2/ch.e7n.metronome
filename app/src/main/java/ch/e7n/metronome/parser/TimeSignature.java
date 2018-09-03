package ch.e7n.metronome.parser;

/**
 * Created by robertre on 9/2/14.
 */
public class TimeSignature implements ConcreteMusicObject {
    private final int numerator;
    private final int denominator;
    private final double time;
    private final double beat;
    private final double strongBeat;

    public TimeSignature(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.time = numerator * 1.0 / denominator;
        // Special case of compound time signatures 6/8 9/8 and 12/8 etc.
        if(denominator == 8 && numerator > 3 && numerator % 3 == 0) {
            this.beat = 3.0 / denominator;
            this.strongBeat = 3.0 / denominator;
        } else {
            this.beat = 1.0 / denominator;
            if(numerator%2==0) {
                this.strongBeat = 2.0 / denominator;
            } else {
                this.strongBeat = time;
            }
        }
    }

    public SingletonIterator getIterator() {
        return new SingletonIterator(this);
    }

    @Override
    public String display() {
        return "(TSig "+numerator+"/"+denominator+") ";
    }

    @Override
    public double length() {
        return 0;
    }

    public double getBeat() {
        return beat;
    }

    public double getStrongBeat() {
        return strongBeat;
    }

    public double getTime() {
        return time;
    }


}
