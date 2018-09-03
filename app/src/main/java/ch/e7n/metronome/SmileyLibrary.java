package ch.e7n.metronome;

/**
 * Created by robertre on 9/10/14.
 */
public class SmileyLibrary {
    public static final Smileys smileys;
    static {
        smileys = new Smileys();
        smileys.add(1024, R.drawable.time_2_4,  "t2/4");
        smileys.add(1034, R.drawable.time_3_4,  "t3/4");
        smileys.add(1044, R.drawable.time_4_4,  "t4/4");
        smileys.add(1054, R.drawable.time_5_4,  "t5/4");
        smileys.add(1064, R.drawable.time_6_4,  "t6/4");
        smileys.add(1068, R.drawable.time_6_8,  "t6/8");
        smileys.add(1098, R.drawable.time_9_8,  "t9/8");
        smileys.add(1128, R.drawable.time_12_8, "t12/8");

        smileys.add(2001, R.drawable.note_1dot,  "n3/2");
        smileys.add(2002, R.drawable.note_2dot,  "n3/4");
        smileys.add(2004, R.drawable.note_4dot,  "n3/8");
        smileys.add(2008, R.drawable.note_8dot,  "n3/16");
        smileys.add(2016, R.drawable.note_16dot, "n3/32");
        smileys.add(2032, R.drawable.note_32dot, "n3/64");
        smileys.add(2101, R.drawable.rest_1dot,  "r3/2");
        smileys.add(2102, R.drawable.rest_2dot,  "r3/4");
        smileys.add(2104, R.drawable.rest_4dot,  "r3/8");
        smileys.add(2108, R.drawable.rest_8dot,  "r3/16");
        smileys.add(2116, R.drawable.rest_16dot, "r3/32");
        smileys.add(2132, R.drawable.rest_32dot, "r3/64");

        smileys.add(3001, R.drawable.note_1,  "n1/1");
        smileys.add(3002, R.drawable.note_2,  "n1/2");
        smileys.add(3004, R.drawable.note_4,  "n1/4");
        smileys.add(3008, R.drawable.note_8,  "n1/8");
        smileys.add(3016, R.drawable.note_16, "n1/16");
        smileys.add(3032, R.drawable.note_32, "n1/32");
        smileys.add(3101, R.drawable.rest_1,  "r1/1");
        smileys.add(3102, R.drawable.rest_2,  "r1/2");
        smileys.add(3104, R.drawable.rest_4,  "r1/4");
        smileys.add(3108, R.drawable.rest_8,  "r1/8");
        smileys.add(3116, R.drawable.rest_16, "r1/16");
        smileys.add(3132, R.drawable.rest_32, "r1/32");

        smileys.add(4001, R.drawable.note_1t,  "n2/3");
        smileys.add(4002, R.drawable.note_2t,  "n1/3");
        smileys.add(4004, R.drawable.note_4t,  "n1/6");
        smileys.add(4008, R.drawable.note_8t,  "n1/12");
        smileys.add(4016, R.drawable.note_16t, "n1/24");
        smileys.add(4032, R.drawable.note_32t, "n1/48");
        smileys.add(4101, R.drawable.rest_1t,  "r2/3");
        smileys.add(4102, R.drawable.rest_2t,  "r1/3");
        smileys.add(4104, R.drawable.rest_4t,  "r1/6");
        smileys.add(4108, R.drawable.rest_8t,  "r1/12");
        smileys.add(4116, R.drawable.rest_16t, "r1/24");
        smileys.add(4132, R.drawable.rest_32t, "r1/48");

        smileys.add(5001, R.drawable.tie, "~");
    }
}
