package ch.e7n.metronome.parser;

/**
 * Created by robertre on 9/2/14.
 */
public class Note implements ConcreteMusicObject {
    private final int numerator;
    private final int denominator;
    private final double time;

    public Note(int numerator, int denominator) {
        int gcd = Gcd.gcd(numerator, denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
        this.time = numerator * 1.0 / denominator;
    }

    public SingletonIterator getIterator() {
        return new SingletonIterator(this);
    }

    @Override
    public String display() {
        return "(Note "+numerator+"/"+denominator+") ";
    }

    @Override
    public double length() {
        return time;
    }

    Note extend(Note additionalNote) {
        int newDenominator = this.denominator * additionalNote.denominator;
        int newNumerator = this.numerator * additionalNote.denominator + additionalNote.numerator * this.denominator;
        return new Note(newNumerator, newDenominator);
    }
}

