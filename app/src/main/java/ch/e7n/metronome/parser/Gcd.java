package ch.e7n.metronome.parser;

/**
 * Created by robertre on 9/6/14.
 */
public class Gcd {
    public static int gcd(int a, int b)
    {
        while (b > 0)
        {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
